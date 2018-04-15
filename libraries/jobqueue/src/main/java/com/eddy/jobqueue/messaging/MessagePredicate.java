package com.eddy.jobqueue.messaging;

public interface MessagePredicate {
    boolean onMessage(Message message);
}
