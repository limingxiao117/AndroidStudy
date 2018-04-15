#include <signal.h>
#include "common.h"
#include "daemon.h"

int uid;

/**
 * 子进程变成僵尸进程会调用这个方法
 * @param sino
 */
void sig_handler(int sino) {
    int status;

    /*
     * 进程一旦调用了wait，就立即阻塞自己，由wait自动分析是否当前进程的某个子进程已经退出，
     * 如果让它找到了这样一个已经变成僵尸的子进程，wait就会收集这个子进程的信息，
     * 并把它彻底销毁后返回；如果没有找到这样一个子进程，wait就会一直阻塞在这里，直到有一个出现为止。
     */
    LOGI("等待死亡信号");
    wait(&status);

    LOGI("重新创建子进程");
    create_child_process();
}

JNIEXPORT void JNICALL Java_com_eddy_androidstudy_daemon_DaemonWatcher_openDaemonWatcher
        (JNIEnv *env, jobject j_obj, jint j_uid) {
    uid = j_uid;

    /*
     * sigaction用于信号处理，sa.sa_flags=SA_RESTART：使被信号打断的系统调用自动重新发起
     * 信号处理交给sig_handler处理的，当子进程挂了的时候会向其父进程发送一个SIGCHLD信号，
     * 父进程就会收到SIGCHLD信号，并且开始执行sig_handler方法，重生成子进程
     */
    LOGI("开启进程");
    struct sigaction s_sa;
    s_sa.sa_flags = 0;
    s_sa.sa_handler = sig_handler;
    sigaction(SIGCHLD, &s_sa, NULL);

    create_child_process();
}

/**
 * 创建子进程：
 * 一个进程调用fork（）函数后，系统先给新的进程分配资源，例如存储数据和代码的空间。
 * 然后把原来的进程的所有值都复制到新的新进程中，只有少数值与原来的进程的值不同。
 * 相当于克隆了一个自己，pid==0 说明是子进程，pid>是父进程
 */
void create_child_process() {
    pid_t pid = fork();
    if (pid > 0) {
        // 父进程
        LOGI("父进程ID = %d", pid);
    } else if (pid == 0) {
        // 子进程
        LOGI("子进程");
        // 启用子线程
        child_start_monitor();
    }
}

void *thread_rt(void *data) {
    pid_t pid;
    /*
     * getppid获取父进程的id  getpid获取自己的id
     * 子进程反复的 循环在判断父进程是否被干掉，如果被干掉就说明小插件的服务被干掉了
     */
    while ((pid = getppid()) != 1) {
        sleep(20);
        LOGI("循环 pid = %d", pid);
    }

    // 如果父进程被干掉了，那么它的子进程就会被init托管，而这个进程的id就是1
    LOGI("重启父进程");
    /*
     * 调用am.startservice重新开启服务 要制定服务的进程id
     * execlp("am", "am", "startservice", "--user", user_id,
     * "com.pybeta.daymatter.service/com.pybeta.daymatter.service.AutoRefreshService",
     * (char *) NULL);
     */
    execlp("am", "am", "startservice", "--user", uid,
           "com.eddy.androidstudy/com.eddy.androidstudy.daemon.DaemonService",
           (char *) NULL);
}

/**
 * 开启子线程监听
 */
void child_start_monitor() {
    pthread_t pthread;
    // int pthread_create(pthread_t* __pthread_ptr, pthread_attr_t const* __attr, void* (*__start_routine)(void*), void*);
    pthread_create(&pthread, NULL, thread_rt, NULL);
}