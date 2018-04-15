package com.eddy.jobqueue.messaging.message;

import com.eddy.jobqueue.messaging.Message;
import com.eddy.jobqueue.messaging.Type;
import com.eddy.jobqueue.Job;

public class AddJobMessage extends Message {
    private Job job;
    public AddJobMessage() {
        super(Type.ADD_JOB);
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    protected void onRecycled() {
        job = null;
    }
}
