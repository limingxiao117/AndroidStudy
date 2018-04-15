package com.eddy.jobqueue.messaging.message;

import com.eddy.jobqueue.messaging.Message;
import com.eddy.jobqueue.messaging.Type;
import com.eddy.jobqueue.CancelResult;

public class CancelResultMessage extends Message {
    CancelResult.AsyncCancelCallback callback;
    CancelResult result;
    public CancelResultMessage() {
        super(Type.CANCEL_RESULT_CALLBACK);
    }

    @Override
    protected void onRecycled() {
        result = null;
        callback = null;
    }

    public void set(CancelResult.AsyncCancelCallback callback, CancelResult result) {
        this.callback = callback;
        this.result = result;
    }

    public CancelResult.AsyncCancelCallback getCallback() {
        return callback;
    }

    public CancelResult getResult() {
        return result;
    }
}
