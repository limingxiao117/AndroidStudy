//
// Created by it on 2018/4/14.
//

#ifndef ANDROIDSTUDY_COMMON_H
#define ANDROIDSTUDY_COMMON_H

#include <sys/select.h>
#include <unistd.h>
#include <sys/socket.h>
#include <pthread.h>
#include <signal.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <sys/un.h>
#include <errno.h>
#include <stdlib.h>
#include <linux/signal.h>
#include <string.h>

// Android LOG
#include <android/log.h>
#define LOG_TAG "eddy"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

void sig_handler(int sino);

void create_child_process();

void child_start_monitor();

#endif //ANDROIDSTUDY_COMMON_H
