package com.openclassrooms.realestatemanager.ui.add_property

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.FakeDatas
import com.openclassrooms.realestatemanager.FakePropertyRepository
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.TestUtils.MainCoroutineRule
import com.openclassrooms.realestatemanager.datas.database.PrepopulateDatas
import com.openclassrooms.realestatemanager.datas.repository.DefaultPropertyRepository
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class AddPropertyViewModelTest : TestCase() {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    lateinit var viewModel: AddPropertyViewModel

    private val fakePropertyRepository = FakePropertyRepository()

    @Mock
    lateinit var application: MyApplication


    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    override fun setUp() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(application.getString(Mockito.anyInt())).thenReturn(" ")


    }




    @ExperimentalCoroutinesApi
    fun testAddNewProperty()= runTest {
            viewModel = AddPropertyViewModel(fakePropertyRepository, application)
            viewModel.addNewProperty(
                FakeDatas.fakeTypes[0],
                FakeDatas.fakeAgents[0],
                1500000L,
                100.00,
                6,
                2,
                2,
                null,
                null,
                listOf(1, 2),
                "2022-01-01",
                null
            )

        viewModel.validAdress.observeForever {
            assertTrue(viewModel.validAdress.value != null)
        }


    }

    }