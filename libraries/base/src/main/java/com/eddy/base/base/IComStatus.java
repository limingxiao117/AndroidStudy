package com.eddy.base.base;

public interface IComStatus {
    int INITIAL = 0;
    int CREATED = 1;
    int STARTED = 2;
    int RUNNING = 3;
    int DESTROYED = 4;

    int getStatus();
}
