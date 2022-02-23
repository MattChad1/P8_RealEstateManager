package com.openclassrooms.realestatemanager.TestUtils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@ExperimentalCoroutinesApi
    class MainCoroutineRule(val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()):
        TestWatcher(),
        TestCoroutineScope by TestCoroutineScope(dispatcher) {
        override fun starting(description: Description?) {
            super.starting(description)
            Dispatchers.setMain(dispatcher)
        }

        override fun finished(description: Description?) {
            super.finished(description)
            cleanupTestCoroutines()
            Dispatchers.resetMain()
        }
}

//    @Throws(InterruptedException::class)
//    fun <T> getOrAwaitValue(liveData: LiveData<T>): T? {
//        val data = arrayOfNulls<Any>(1)
//        val latch = CountDownLatch(1)
//        val observer: Observer<T?> = object : Observer<T?> {
//            override fun onChanged(o: T?) {
//                data[0] = o
//                latch.countDown()
//                liveData.removeObserver(this)
//            }
//        }
//        liveData.observeForever(observer)
//        // Don't wait indefinitely if the LiveData is not set.
//        if (!latch.await(2, TimeUnit.SECONDS)) {
//            throw RuntimeException("LiveData value was never set.")
//        }
//        return data[0] as T?
//    }

