package com.openclassrooms.realestatemanager.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.FakeDatas
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import com.openclassrooms.realestatemanager.ui.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MapsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    lateinit var viewModel: MapsViewModel

    @Mock
    lateinit var propertyRepository: PropertyRepository

    @Mock
    lateinit var navigationRepository: NavigationRepository

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val fakeLiveData = MutableLiveData<List<PropertyWithProximity>>()


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)


        fun fakeFlowProperties() = flow {
            emit(FakeDatas.fakePropertiesCompletes)
        }
//        Mockito.`when`(propertyRepository.allPropertiesComplete.asLiveData()).thenReturn(fakeLiveData)
        Mockito.doReturn(fakeFlowProperties()).`when`(propertyRepository).allPropertiesComplete
        Mockito.doReturn(fakeLiveData).`when`(propertyRepository).allPropertiesComplete.asLiveData()
        viewModel = MapsViewModel(propertyRepository, navigationRepository)
          }

    @Test
    fun getAllPropertiesLiveData() = runTest {

        delay(2000L)
        fakeLiveData.value = FakeDatas.fakePropertiesCompletes
        delay(2000L)
        assertEquals(FakeDatas.fakePropertiesCompletes.size,viewModel.allPropertiesLiveData.value?.size)
    }


    }
