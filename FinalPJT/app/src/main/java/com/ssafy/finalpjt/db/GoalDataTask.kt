package com.ssafy.finalpjt.db

import com.ssafy.finalpjt.db.model.Goal

/**
 * db를 비동기로 가져오기 위함
 */
class GoalDataTask : BaseAsyncTask<Void?, Void?, List<Goal>?>() {
    private var mTaskListener: TaskListener? = null
    private var mFetcher: DataFetcher? = null

    override fun onPostExecute(data: List<Goal>?) {
        if (mTaskListener != null) {
            mTaskListener!!.onComplete(data)
        }
    }
    override fun doInBackground(vararg params: Void?): List<Goal>? {
        return mFetcher!!.data
    }

    interface TaskListener {
        fun onComplete(data: List<Goal>?)
    }

    interface DataFetcher {
        val data: List<Goal>?
    }

    class Builder {
        private var mFetcher: DataFetcher? = null
        private var mCallback: TaskListener? = null
        fun setFetcher(fetcher: DataFetcher?): Builder {
            mFetcher = fetcher
            return this
        }

        fun setCallback(callback: TaskListener?): Builder {
            mCallback = callback
            return this
        }

        fun build(): GoalDataTask {
            val cursorTask = GoalDataTask()
            cursorTask.mTaskListener = mCallback
            cursorTask.mFetcher = mFetcher
            return cursorTask
        }
    }
}