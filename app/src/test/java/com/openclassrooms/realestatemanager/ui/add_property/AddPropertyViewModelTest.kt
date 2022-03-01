package com.openclassrooms.realestatemanager.ui.add_property

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.FakeDatas
import com.openclassrooms.realestatemanager.FakePropertyRepository
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.TestUtils.NinoCoroutineRule
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class AddPropertyViewModelTest : TestCase() {

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = NinoCoroutineRule()

    private val testDispatcher = StandardTestDispatcher()

    lateinit var viewModel: AddPropertyViewModel

    private val fakePropertyRepository = FakePropertyRepository()

    @Mock
    lateinit var application: MyApplication


    @Before
    override fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(application.getString(Mockito.anyInt())).thenReturn(" ")

    }

    @ExperimentalCoroutinesApi
    fun testAddNewProperty()= runBlockingTest {
            viewModel = AddPropertyViewModel(fakePropertyRepository, application)
//        Mockito.`when`(viewModel.formFinished).thenReturn(fakeFormLiveData)
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
                "01/01/2022",
                null
            )

        viewModel.validAdress.observeForever {
                assertTrue(viewModel.validAdress.value != null)

        }

    }
    }