package com.example.yoony.opensourceandroidproject.db;


import com.example.yoony.opensourceandroidproject.db.model.GoalSub;

import java.util.List;


/**
 * db를 비동기로 가져오기 위함
 */
public class GoalSubDataTask extends BaseAsyncTask<Void, Void, List<GoalSub>> {
    private TaskListener mTaskListener;
    private DataFetcher mFetcher;

    @Override
    protected List<GoalSub> doInBackground(Void... params) {
        List<GoalSub> data = mFetcher.getData();
        return data;
    }

    @Override
    protected void onPostExecute(List<GoalSub> data) {
        if (mTaskListener != null) {
            mTaskListener.onComplete(data);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public interface TaskListener {
        void onComplete(List<GoalSub> data);
    }

    public interface DataFetcher {
        List<GoalSub> getData();
    }

    public static class Builder {

        private DataFetcher mFetcher;
        private TaskListener mCallback;

        public Builder setFetcher(DataFetcher fetcher) {
            mFetcher = fetcher;
            return this;
        }

        public Builder setCallback(TaskListener callback) {
            mCallback = callback;
            return this;
        }

        public GoalSubDataTask build() {
            GoalSubDataTask cursorTask = new GoalSubDataTask();
            cursorTask.mTaskListener = mCallback;
            cursorTask.mFetcher = mFetcher;
            return cursorTask;
        }
    }
}
