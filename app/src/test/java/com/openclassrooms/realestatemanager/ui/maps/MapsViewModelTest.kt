package com.openclassrooms.realestatemanager.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.FakeDatas
import com.openclassrooms.realestatemanager.FakePropertyRepository
import com.openclassrooms.realestatemanager.TestUtils.LiveDataTestUtils
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import com.openclassrooms.realestatemanager.datas.repository.DefaultPropertyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
    val fakePropertyRepository = FakePropertyRepository()

    @Mock
    lateinit var navigationRepository: NavigationRepository

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun getAllPropertiesLiveData() = runTest {
        val flow = MutableStateFlow(FakeDatas.fakePropertiesCompletes)
        viewModel = MapsViewModel(fakePropertyRepository, navigationRepository)
        flow.emit(FakeDatas.fakePropertiesCompletes)
        val values = LiveDataTestUtils.getOrAwaitValue((viewModel.allPropertiesLiveData))
        assertEquals(FakeDatas.fakePropertiesCompletes.size,values.size)
    }


    }
