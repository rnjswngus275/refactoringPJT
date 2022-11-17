package com.ssafy.finalpjt.db

import android.os.Handler
import android.os.Message
import android.os.Process
import com.ssafy.finalpjt.db.model.Goal
import com.ssafy.finalpjt.db.DBConst.GoalTable
import com.ssafy.finalpjt.L
import com.ssafy.finalpjt.db.model.GoalSub
import com.ssafy.finalpjt.db.DBConst.SubGoalTable
import kotlin.Throws
import com.ssafy.finalpjt.db.DbHelper
import com.ssafy.finalpjt.db.dao.GoalDAO
import com.ssafy.finalpjt.db.dao.GoalSubDAO
import kotlin.jvm.Synchronized
import com.ssafy.finalpjt.db.CommonDAO
import com.ssafy.finalpjt.db.DBConst
import com.ssafy.finalpjt.db.BaseAsyncTask
import com.ssafy.finalpjt.db.GoalDataTask
import kotlin.jvm.Volatile
import com.ssafy.finalpjt.db.BaseAsyncTask.SerialExecutor
import com.ssafy.finalpjt.db.GoalSubDataTask
import com.ssafy.finalpjt.DBHelper
import com.ssafy.finalpjt.MainActivity
import com.ssafy.finalpjt.SampleData
import com.ssafy.finalpjt.MyFragment.MyAdapter
import com.ssafy.finalpjt.MyFragment
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.finalpjt.SubItemView
import com.ssafy.finalpjt.db.factory.GoalDAOFactory
import com.ssafy.finalpjt.db.factory.GoalSubDAOFactory
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpjt.FragmentMain.RecyclerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.DetailActivity
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult
import com.ssafy.finalpjt.AddActivity
import com.ssafy.finalpjt.FragmentMain.RecyclerAdapter.ItemViewHolder
import androidx.cardview.widget.CardView
import com.ssafy.finalpjt.FragmentShop.SingerAdapter
import com.ssafy.finalpjt.SingerShopItem
import com.ssafy.finalpjt.SingerViewer
import com.ssafy.finalpjt.FragmentTodo
import com.ssafy.finalpjt.AddListFragment
import androidx.core.content.ContextCompat
import com.google.android.material.navigation.NavigationView
import com.ssafy.finalpjt.MainActivity.AlarmHATT
import com.ssafy.finalpjt.FragmentMain
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import com.ssafy.finalpjt.FragmentGoals
import com.ssafy.finalpjt.FragmentShop
import com.ssafy.finalpjt.FragmentSetting
import com.ssafy.finalpjt.BroadcastD
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import com.ssafy.finalpjt.Goals_PagerAdapter
import com.google.android.material.tabs.TabLayout.ViewPagerOnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.ssafy.finalpjt.DetailActivityUpdate
import com.ssafy.finalpjt.Goals_Sub
import androidx.fragment.app.FragmentPagerAdapter
import com.ssafy.finalpjt.Goals_Fragment1
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.util.ArrayDeque
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseAsyncTask<Params, Progress, Result> {
    private val mWorker: WorkerRunnable<Params, Result>
    private val mFuture: FutureTask<Result>
    private val mCancelled = AtomicBoolean()
    private val mTaskInvoked = AtomicBoolean()

    @Volatile
    var status = Status.PENDING
        private set

    private fun postResultIfNotInvoked(result: Result?) {
        val wasTaskInvoked = mTaskInvoked.get()
        if (!wasTaskInvoked) {
            postResult(result)
        }
    }

    private fun postResult(result: Result): Result {
        val message = sHandler.obtainMessage(MESSAGE_POST_RESULT, AsyncTaskResult(this, result))
        message.sendToTarget()
        return result
    }

    protected abstract fun doInBackground(vararg params: Params): Result
    protected open fun onPreExecute() {}
    protected open fun onPostExecute(result: Result) {}
    protected fun onProgressUpdate(vararg values: Progress) {}
    protected fun onCancelled(result: Result) {
        onCancelled()
    }

    protected fun onCancelled() {}
    val isCancelled: Boolean
        get() = mCancelled.get()

    fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        mCancelled.set(true)
        return mFuture.cancel(mayInterruptIfRunning)
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    fun get(): Result {
        return mFuture.get()
    }

    @Throws(InterruptedException::class, ExecutionException::class, TimeoutException::class)
    operator fun get(timeout: Long, unit: TimeUnit?): Result {
        return mFuture[timeout, unit]
    }

    fun execute(vararg params: Params): BaseAsyncTask<Params, Progress, Result> {
        return executeOnExecutor(sDefaultExecutor, *params)
    }

    fun executeOnExecutor(
        exec: Executor,
        vararg params: Params
    ): BaseAsyncTask<Params, Progress, Result> {
        if (status != Status.PENDING) {
            when (status) {
                Status.RUNNING -> throw IllegalStateException("Cannot execute task:" + " the task is already running.")
                Status.FINISHED -> throw IllegalStateException("Cannot execute task:" + " the task has already been executed " + "(a task can be executed only once)")
            }
        }
        status = Status.RUNNING
        onPreExecute()
        mWorker.mParams = params
        exec.execute(mFuture)
        return this
    }

    protected fun publishProgress(vararg values: Progress) {
        if (!isCancelled) {
            sHandler.obtainMessage(MESSAGE_POST_PROGRESS, AsyncTaskResult(this, *values))
                .sendToTarget()
        }
    }

    private fun finish(result: Result) {
        if (isCancelled) {
            onCancelled(result)
        } else {
            onPostExecute(result)
        }
        status = Status.FINISHED
    }

    /**
     * Indicates the current status of the task. Each status will be set only once
     * during the lifetime of a task.
     */
    enum class Status {
        /**
         * Indicates that the task has not been executed yet.
         */
        PENDING,

        /**
         * Indicates that the task is running.
         */
        RUNNING,

        /**
         * Indicates that [AsyncTask.onPostExecute] has finished.
         */
        FINISHED
    }

    private class SerialExecutor : Executor {
        val mTasks = ArrayDeque<Runnable>()
        var mActive: Runnable? = null
        @Synchronized
        override fun execute(r: Runnable) {
            mTasks.offer(Runnable {
                try {
                    r.run()
                } finally {
                    scheduleNext()
                }
            })
            if (mActive == null) {
                scheduleNext()
            }
        }

        @Synchronized
        protected fun scheduleNext() {
            if (mTasks.poll().also { mActive = it } != null) {
                THREAD_POOL_EXECUTOR.execute(mActive)
            }
        }
    }

    private class InternalHandler : Handler() {
        override fun handleMessage(msg: Message) {
            val result = msg.obj as AsyncTaskResult<*>
            when (msg.what) {
                MESSAGE_POST_RESULT ->                     // There is only one result
                    result.mTask.finish(result.mData[0])
                MESSAGE_POST_PROGRESS -> result.mTask.onProgressUpdate(*result.mData)
            }
        }
    }

    private abstract class WorkerRunnable<Params, Result> : Callable<Result> {
        var mParams: Array<Params>
    }

    private class AsyncTaskResult<Data> internal constructor(
        val mTask: BaseAsyncTask<*, *, *>,
        vararg data: Data
    ) {
        val mData: Array<Data>

        init {
            mData = data
        }
    }

    companion object {
        /**
         * An [Executor] that executes tasks one at a time in serial
         * order.  This serialization is global to a particular process.
         */
        val SERIAL_EXECUTOR: Executor = SerialExecutor()
        private val TAG = BaseAsyncTask::class.java.simpleName
        private const val CORE_POOL_SIZE = 5
        private const val MAXIMUM_POOL_SIZE = 128
        private const val KEEP_ALIVE = 1
        private val sThreadFactory: ThreadFactory = object : ThreadFactory {
            private val mCount = AtomicInteger(1)
            override fun newThread(r: Runnable): Thread {
                return Thread(r, "AsyncTask #" + mCount.getAndIncrement())
            }
        }
        private val sPoolWorkQueue: BlockingQueue<Runnable> = LinkedBlockingQueue(10)

        /**
         * An [Executor] that can be used to execute tasks in parallel.
         */
        val THREAD_POOL_EXECUTOR: Executor = ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE,
            KEEP_ALIVE,
            TimeUnit.SECONDS,
            sPoolWorkQueue,
            sThreadFactory
        )
        private const val MESSAGE_POST_RESULT = 0x1
        private const val MESSAGE_POST_PROGRESS = 0x2
        private val sHandler = InternalHandler()

        @Volatile
        private var sDefaultExecutor = SERIAL_EXECUTOR

        /**
         * @hide Used to force static handler to be created.
         */
        fun init() {
            sHandler.looper
        }

        /**
         * @hide
         */
        fun setDefaultExecutor(exec: Executor) {
            sDefaultExecutor = exec
        }

        fun execute(runnable: Runnable?) {
            sDefaultExecutor.execute(runnable)
        }
    }

    /**
     * Creates a new asynchronous task. This constructor must be invoked on the UI thread.
     */
    init {
        mWorker = object : WorkerRunnable<Params, Result>() {
            @Throws(Exception::class)
            override fun call(): Result {
                mTaskInvoked.set(true)
                Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT)
                return postResult(doInBackground(*mParams))
            }
        }
        mFuture = object : FutureTask<Result>(mWorker) {
            override fun done() {
                try {
                    postResultIfNotInvoked(get())
                } catch (e: InterruptedException) {
                    L.e(e.message)
                } catch (e: ExecutionException) {
                    throw RuntimeException(
                        "An error occured while executing doInBackground()",
                        e.cause
                    )
                } catch (e: CancellationException) {
                    postResultIfNotInvoked(null)
                }
            }
        }
    }
}