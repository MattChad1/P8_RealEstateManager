package com.openclassrooms.realestatemanager.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.FakeDatas
import com.openclassrooms.realestatemanager.datas.model.Filter
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import com.openclassrooms.realestatemanager.datas.repository.DefaultPropertyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    lateinit var viewModel: SearchViewModel

    @Mock
    lateinit var propertyRepository: DefaultPropertyRepository

    @Mock
    lateinit var navigationRepository: NavigationRepository

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val fakeLiveData = MutableLiveData<List<PropertyWithProximity>>()
    private val fakeFilterLiveData = MutableLiveData<Filter>()


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)


        fakeLiveData.value = FakeDatas.fakePropertiesCompletes
        fun fakeFlowProperties() = flow {
            emit(FakeDatas.fakePropertiesCompletes)
        }
        Mockito.`when`(propertyRepository.getAllPropertiesComplete()).thenReturn(fakeFlowProperties())

        val noFilter = Filter()
        fakeFilterLiveData.value = noFilter

        viewModel = SearchViewModel(propertyRepository, navigationRepository)
        Mockito.`when`(navigationRepository.filterLiveData).thenReturn(fakeFilterLiveData)
        Mockito.`when`(navigationRepository.filter).thenReturn(fakeFilterLiveData.value)

    }


    @Test
    fun updateFilter() {
        val test1Name = SearchViewModel.PRICE
        val test1Value1 = 100000L
        val test1Value2 = 200000L


        viewModel.updateFilter(test1Name, test1Value1.toString(), test1Value2.toString())
        assertTrue(fakeFilterLiveData.value?.price == Pair(test1Value1, test1Value2))

    }
}