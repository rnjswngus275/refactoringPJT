package com.example.yoony.opensourceandroidproject;

public class SampleData {
    private int quest, id;
    private String job;
    private boolean ischecked;

    public SampleData(int id, int quest, String job, int ischecked) {
        this.id = id;
        this.quest = quest;
        this.job = job;
        if (ischecked == 0) this.ischecked = false;
        else this.ischecked = true;
    }

    public int getQuest() {
        return this.quest;
    }

    public int getId() {
        return this.id;
    }

    public String getJob() {
        return this.job;
    }

    public boolean isChecked() {
        return this.ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public int getisDone() {
        if (this.ischecked) return 1;
        return 0;
    }
}
