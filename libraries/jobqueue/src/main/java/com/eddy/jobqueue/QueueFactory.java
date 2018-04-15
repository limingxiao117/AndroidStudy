package com.eddy.jobqueue;


import com.eddy.jobqueue.JobQueue;
import com.eddy.jobqueue.config.Configuration;

public interface QueueFactory {
    JobQueue createPersistentQueue(Configuration configuration, long sessionId);
    JobQueue createNonPersistent(Configuration configuration, long sessionId);
}