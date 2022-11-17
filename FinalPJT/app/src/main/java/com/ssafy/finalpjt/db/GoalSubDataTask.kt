package com.ssafy.finalpjt.db

import com.ssafy.finalpjt.db.model.GoalSub

/**
 * db를 비동기로 가져오기 위함
 */
class GoalSubDataTask : BaseAsyncTask<Void?, Void?, List<GoalSub?>?>() {
    private var mTaskListener: TaskListener? = null
    private var mFetcher: DataFetcher? = null
    protected override fun doInBackground(vararg params: Void): List<GoalSub?>? {
        return mFetcher!!.data
    }

    override fun onPostExecute(data: List<GoalSub?>?) {
        if (mTaskListener != null) {
            mTaskListener!!.onComplete(data)
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    interface TaskListener {
        fun onComplete(data: List<GoalSub?>?)
    }

    interface DataFetcher {
        val data: List<GoalSub?>?
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

        fun build(): GoalSubDataTask {
            val cursorTask = GoalSubDataTask()
            cursorTask.mTaskListener = mCallback
            cursorTask.mFetcher = mFetcher
            return cursorTask
        }
    }
}