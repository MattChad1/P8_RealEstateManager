package com.openclassrooms.realestatemanager.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.openclassrooms.realestatemanager.FakeDatas
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MapsViewModelTest2 {

        private val testDispatcher = StandardTestDispatcher()
        lateinit var viewModel: MapsViewModel

        @Mock
        lateinit var propertyRepository: PropertyRepository

        @Mock
        lateinit var navigationRepository: NavigationRepository

        @get:Rule
        var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

        @Mock
        private lateinit var mockObserver: Observer<List<MapsViewStateItem>>


        @Before
        fun setUp() {
            Dispatchers.setMain(testDispatcher)
            MockitoAnnotations.openMocks(this)
        }


        @Test
        fun getAllPropertiesLiveData2() = runTest {
            viewModel = MapsViewModel(propertyRepository, navigationRepository)
            val flow = flow {
                emit(FakeDatas.fakePropertiesCompletes)
            }
            Mockito.`when`(propertyRepository.getAllPropertiesComplete()).thenReturn(flow)

            viewModel.allPropertiesLiveData.observeForever(mockObserver)
            assertEquals(FakeDatas.fakePropertiesCompletes.size,
                viewModel.allPropertiesLiveData.value?.size)
            viewModel.allPropertiesLiveData.removeObserver(mockObserver)
        }



}