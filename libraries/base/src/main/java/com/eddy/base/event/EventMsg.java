package com.eddy.base.event;

import java.io.Serializable;

/**
 * Created by eddyli on 2016/8/25.
 */
public class EventMsg implements Serializable {

    public int    what;
    public int    arg1;
    public int    arg2;
    public Object obj;

    public EventMsg() {

    }

    public EventMsg(int what) {
        this.what = what;
    }

    public EventMsg(int what, Object obj) {
        this.what = what;
        this.obj = obj;
    }

    public EventMsg(int what, int arg1) {
        this.what = what;
        this.arg1 = arg1;
    }

    public EventMsg(int what, int arg1, Object obj) {
        this.what = what;
        this.arg1 = arg1;
        this.obj = obj;
    }

    public EventMsg(int what, int arg1, int arg2) {
        this.what = what;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public EventMsg(int what, int arg1, int arg2, Object obj) {
        this.what = what;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.obj = obj;
    }
}
