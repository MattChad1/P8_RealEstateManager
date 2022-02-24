package com.openclassrooms.realestatemanager.ui.add_property

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.FakeDatas
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.datas.database.PrepopulateDatas
import com.openclassrooms.realestatemanager.datas.repository.DefaultPropertyRepository
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class AddPropertyViewModelTest : TestCase() {

    private val testDispatcher = StandardTestDispatcher()
    lateinit var viewModel: AddPropertyViewModel

    @Mock
    lateinit var propertyRepository: DefaultPropertyRepository

    @Mock
    lateinit var application: MyApplication




    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    override fun setUp() {
        Dispatchers.setMain(testDispatcher)


        MockitoAnnotations.openMocks(this)
        Mockito.`when`(application.getString(Mockito.anyInt())).thenReturn(" ")

        fun fakeFlowTypes() = flow { emit(PrepopulateDatas.preTypes) }
        Mockito.`when`(propertyRepository.getAllTypes()).thenReturn(fakeFlowTypes())

    }

    @After
    override fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
//        testDispatcher.cleanupTestCoroutines()
    }


    fun testAddNewProperty()= runTest {
            `when`(propertyRepository.getMaxId()).thenReturn(0)
            `when`(propertyRepository.getAllAgents()).thenReturn(FakeDatas.fakeAgents)
            viewModel = AddPropertyViewModel(propertyRepository, application)


            Mockito.doNothing().`when`(propertyRepository.deletePhoto(Mockito.anyInt()))
            Mockito.doReturn(1).`when`(propertyRepository.insert(Mockito.any()))
            Mockito.doNothing().`when`(propertyRepository.addPhoto(Mockito.anyInt(), Mockito.anyString(), Mockito.anyString()))
            Mockito.doNothing().`when`(propertyRepository.updateProximityForProperty(Mockito.anyInt(), Mockito.anyList()))

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


            assertTrue(viewModel.validAdress.value != null)


    }

    }