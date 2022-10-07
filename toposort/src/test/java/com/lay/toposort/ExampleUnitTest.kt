package com.lay.toposort

import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        val lock = Object()
        val t1 = Thread{
            synchronized(lock){
                print("开始执行线程1第一步")
                print("开始执行线程1第二步")
                lock.notify()
                Thread.sleep(2000)
                print("开始执行线程1第三步")
            }
        }

        val t2 = Thread{
            synchronized(lock){
                lock.wait()
                Thread.sleep(1000)
                print("开始执行线程2")
            }
        }
        t2.start()
        t1.start()
        t2.join()
        t1.join()
    }
}