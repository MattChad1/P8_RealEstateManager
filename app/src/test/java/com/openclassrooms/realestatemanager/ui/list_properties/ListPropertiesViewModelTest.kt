package com.openclassrooms.realestatemanager.ui.list_properties

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.FakeDatas
import com.openclassrooms.realestatemanager.datas.database.PrepopulateDatas
import com.openclassrooms.realestatemanager.datas.model.Filter
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import com.openclassrooms.realestatemanager.datas.repository.DefaultPropertyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations.openMocks

@ExperimentalCoroutinesApi
class ListPropertiesViewModelTest {


    private val testDispatcher = StandardTestDispatcher()

    private val fakeLiveData = MutableLiveData<List<PropertyWithProximity>>()
    private val filterLiveData = MutableLiveData<Filter>()
    private val previousIdsLiveData = MutableLiveData<MutableList<Int>>()

    lateinit var viewModel: ListPropertiesViewModel

    @Mock
    lateinit var repository: DefaultPropertyRepository

    @Mock
    lateinit var navigationRepository: NavigationRepository

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        openMocks(this)



        fakeLiveData.value = FakeDatas.fakePropertiesCompletes
        fun fakeFlowProperties() = flow {
            emit(FakeDatas.fakePropertiesCompletes)
        }

        fun fakeFlowTypes() = flow { emit(PrepopulateDatas.preTypes) }
        val noFilter = Filter()
        filterLiveData.value = noFilter
        Mockito.`when`(repository.getAllPropertiesComplete()).thenReturn(fakeFlowProperties())
        Mockito.`when`(repository.getAllTypes()).thenReturn(fakeFlowTypes())

        previousIdsLiveData.value = mutableListOf(1, 2)
        Mockito.`when`(navigationRepository.propertiesConsultedIdsLiveData).thenReturn(previousIdsLiveData)
    }






    @Test
    fun getFilterLiveData() = runTest {
            viewModel = Mockito.spy(ListPropertiesViewModel(repository, navigationRepository))

            Mockito.`when`(viewModel.filterLiveData).thenReturn(filterLiveData)
            Mockito.`when`(viewModel.lastIdsLiveData).thenReturn(previousIdsLiveData)
            val allPropertiesLiveDataTest = viewModel.allPropertiesLiveData
            assertTrue(fakeLiveData.value!!.size == allPropertiesLiveDataTest.value?.size)

        }
}