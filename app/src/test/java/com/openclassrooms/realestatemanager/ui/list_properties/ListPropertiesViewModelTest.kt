package com.openclassrooms.realestatemanager.ui.list_properties

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.FakeDatas
import com.openclassrooms.realestatemanager.TestUtils.LiveDataTestUtils.getOrAwaitValue
import com.openclassrooms.realestatemanager.datas.database.PrepopulateDatas
import com.openclassrooms.realestatemanager.datas.model.Filter
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations.openMocks

@ExperimentalCoroutinesApi
class ListPropertiesViewModelTest {

//    val testScope = TestCoroutineScope()

private val fakeLiveData = MutableLiveData<List<PropertyWithProximity>>()
private val filterLiveData = MutableLiveData<Filter>()
private val previousIdsLiveData = MutableLiveData<MutableList<Int>>()

    lateinit var viewModel: ListPropertiesViewModel

    @Mock
    lateinit var repository: PropertyRepository

    @Mock
    lateinit var navigationRepository: NavigationRepository


    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        openMocks(this)
        Dispatchers.setMain(dispatcher)

        fakeLiveData.value = FakeDatas.fakePropertiesCompletes
        fun fakeFlowProperties() = flow {
            emit(FakeDatas.fakePropertiesCompletes)
        }

        fun fakeFlowTypes() = flow { emit(PrepopulateDatas.preTypes) }
        val noFilter = Filter()
        filterLiveData.value = noFilter
        previousIdsLiveData.value = mutableListOf(1, 2)


        Mockito.`when`(navigationRepository.propertiesConsultedIdsLiveData).thenReturn(previousIdsLiveData)
        Mockito.`when`(repository.allPropertiesComplete).thenReturn(fakeFlowProperties())
        Mockito.`when`(repository.allTypes).thenReturn(fakeFlowTypes())

        viewModel = Mockito.spy(ListPropertiesViewModel(repository, navigationRepository))
        Mockito.`when`(viewModel.filterLiveData).thenReturn(filterLiveData)



    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
//        testScope.cleanupTestCoroutines()
    }

    @Test
    fun getFilterLiveData() {
        val allPropertiesLiveDataTest = getOrAwaitValue(viewModel.allPropertiesLiveData)
        assertTrue(fakeLiveData.value!!.size==allPropertiesLiveDataTest.size)
    }
}