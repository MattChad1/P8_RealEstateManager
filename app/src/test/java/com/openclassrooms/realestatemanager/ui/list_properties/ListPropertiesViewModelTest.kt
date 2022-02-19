package com.openclassrooms.realestatemanager.ui.list_properties

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.FakeDatas
import com.openclassrooms.realestatemanager.TestUtils.LiveDataTestUtils.getOrAwaitValue
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ListPropertiesViewModelTest {

private val fakeLiveData = MutableLiveData<List<PropertyWithProximity>>()

    lateinit var viewModel: ListPropertiesViewModel

    @Mock
    lateinit var repository: PropertyRepository

    @Mock
    lateinit var navigationRepository: NavigationRepository


    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        fakeLiveData.value = FakeDatas.fakePropertiesCompletes
        Mockito.`when`(repository.allPropertiesComplete.asLiveData()).thenReturn(fakeLiveData)

        viewModel = ListPropertiesViewModel(repository, navigationRepository)



    }

    @After
    fun tearDown() {
    }

    @Test
    fun getFilterLiveData() {
        val mediatorLiveDataTest = getOrAwaitValue(viewModel.mediatorLiveData)
        assertEquals(fakeLiveData.value!!.size, mediatorLiveDataTest.size)
        assertTrue(fakeLiveData.value!!.size==mediatorLiveDataTest.size)
    }
}