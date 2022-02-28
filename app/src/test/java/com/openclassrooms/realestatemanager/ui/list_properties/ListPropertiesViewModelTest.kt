package com.openclassrooms.realestatemanager.ui.list_properties

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.openclassrooms.realestatemanager.FakeDatas
import com.openclassrooms.realestatemanager.FakePropertyRepository
import com.openclassrooms.realestatemanager.TestUtils.MainCoroutineRule
import com.openclassrooms.realestatemanager.datas.database.PrepopulateDatas
import com.openclassrooms.realestatemanager.datas.model.Filter
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import com.openclassrooms.realestatemanager.datas.repository.DefaultPropertyRepository
import com.openclassrooms.realestatemanager.ui.maps.MapsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertTrue
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.openMocks

@ExperimentalCoroutinesApi
class ListPropertiesViewModelTest {


    private val testDispatcher = StandardTestDispatcher()

    private val fakePropertyRepository = FakePropertyRepository()


    private val fakeLiveData = MutableLiveData<List<PropertyWithProximity>>()
    private val filterLiveData = MutableLiveData<Filter>()
    private val previousIdsLiveData = MutableLiveData<MutableList<Int>>()

    lateinit var viewModel: ListPropertiesViewModel

//    @get:Rule
//    val testCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var navigationRepository: NavigationRepository

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        openMocks(this)
 }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
//        testDispatcher.cleanupTestCoroutines()
    }


    @Test
    fun getAllPropertiesLiveData() = runTest {
        viewModel = ListPropertiesViewModel(fakePropertyRepository, navigationRepository)
        val noFilter = Filter()
        val mockFilterLiveData = MutableLiveData<Filter>()
        mockFilterLiveData.value = noFilter
        Mockito.`when`(navigationRepository.filterLiveData).thenReturn(mockFilterLiveData)

        val mockLastIdsLiveData = MutableLiveData<MutableList<Int>>()
        mockLastIdsLiveData.value = mutableListOf(1, 2)
        Mockito.`when`(navigationRepository.propertiesConsultedIdsLiveData).thenReturn(mockLastIdsLiveData)


        viewModel.mediatorLiveData.observeForTesting {
            val value = assertNonNullLiveDataValue<List<PropertyViewStateItem>>(it)
            Assert.assertEquals(FakeDatas.fakePropertiesCompletes.size +1, value.size)
        }
    }


    internal fun <T> LiveData<T>.observeForTesting(block: (LiveData<T>) -> Unit) {
        val observer = Observer<T> { }
        try {
            observeForever(observer)
            block(this)
        } finally {
            removeObserver(observer)
        }
    }

    inline fun <reified T> assertNonNullLiveDataValue(liveData: LiveData<*>): T = liveData.value.let {
        it ?: throw AssertionError("LiveData value is NULL !")
    }.let {
        it as? T ?: throw AssertionError("LiveData value IS NOT of type [ ${T::class.java} ] (current type is = [ ${it::class.java} ])")
    }
}