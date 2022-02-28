package com.openclassrooms.realestatemanager.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.openclassrooms.realestatemanager.FakeDatas
import com.openclassrooms.realestatemanager.FakePropertyRepository
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*

@ExperimentalCoroutinesApi
class MapsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    lateinit var viewModel: MapsViewModel
    val fakePropertyRepository = FakePropertyRepository()

    @Mock
    lateinit var navigationRepository: NavigationRepository

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Captor
    private lateinit var captor: ArgumentCaptor<List<PropertyWithProximity>>

    @Mock
    private lateinit var mockObserver: Observer<List<PropertyWithProximity>?>

    @Mock
    private lateinit var mockObserver2: Observer<List<MapsViewStateItem>>


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun getAllPropertiesLiveData() = runTest {

        viewModel = MapsViewModel(fakePropertyRepository, navigationRepository)
//        assertEquals(FakeDatas.fakePropertiesCompletes[0], fakePropertyRepository.getAllPropertiesComplete().first()?.get(0))
//        Mockito.`when`(fakePropertyRepository.getAllPropertiesComplete().asLiveData()).thenReturn(MutableLiveData(FakeDatas.fakePropertiesCompletes))

        viewModel.allPropertiesLiveData.observeForever(mockObserver2)


//        val values = LiveDataTestUtils.getOrAwaitValue((viewModel.allPropertiesLiveData))
        assertEquals(FakeDatas.fakePropertiesCompletes.size, viewModel.allPropertiesLiveData.value?.size)
        viewModel.allPropertiesLiveData.removeObserver(mockObserver2)
    }

//    @Test
//    fun getAllPropertiesLiveData2() = runTest {
//        viewModel = MapsViewModel(propertyRepository, navigationRepository)
//        val flow = flow {
//            emit(FakeDatas.fakePropertiesCompletes)
//        }
//        Mockito.`when`(fakePropertyRepository.getAllPropertiesComplete()).thenReturn(flow)
//
//        viewModel.allPropertiesLiveData.observeForever(mockObserver)
//        assertEquals(FakeDatas.fakePropertiesCompletes.size, viewModel.allPropertiesLiveData.value?.size)
//        viewModel.allPropertiesLiveData.removeObserver(mockObserver2)
//    }


}
