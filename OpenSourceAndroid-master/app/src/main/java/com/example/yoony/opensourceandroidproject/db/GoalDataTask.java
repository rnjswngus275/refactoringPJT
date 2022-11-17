package com.example.yoony.opensourceandroidproject.db;


import com.example.yoony.opensourceandroidproject.db.model.Goal;

import java.util.List;


/**
 * db를 비동기로 가져오기 위함
 */
public class GoalDataTask extends BaseAsyncTask<Void, Void, List<Goal>> {
    private TaskListener mTaskListener;
    private DataFetcher mFetcher;

    @Override
    protected List<Goal> doInBackground(Void... params) {
        List<Goal> data = mFetcher.getData();
        return data;
    }

    @Override
    protected void onPostExecute(List<Goal> data) {
        if (mTaskListener != null) {
            mTaskListener.onComplete(data);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public interface TaskListener {
        void onComplete(List<Goal> data);
    }

    public interface DataFetcher {
        List<Goal> getData();
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

        public GoalDataTask build() {
            GoalDataTask cursorTask = new GoalDataTask();
            cursorTask.mTaskListener = mCallback;
            cursorTask.mFetcher = mFetcher;
            return cursorTask;
        }
    }
}
