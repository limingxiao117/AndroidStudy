# AndroidStudy

## 主要功能：

    1）Gradle公共变量提取
    2）双进程守护JNI实现
    3）GreenDao集成学习
    4）沉浸式兼容
    5）Volley框架集成
    6）Retrofit框架集成
    7）多Dex
    8）LeakCanary

## 待完成功能

    1）缓存 -- DiskLruCache    Java实现基于LRU的磁盘缓存
    2）图片加载 -- Fresco  一个用于管理图像和他们使用的内存的库
    3）网络解析 -- Gson    一个Java序列化/反序列化库，可以将JSON和java对象互相转换
    4）事件总线 -- EventBus    安卓优化的事件总线，简化了活动、片段、线程、服务等的通信
    6）性能优化 -- LeakCanary  内存泄漏检测工具
        BlockCanary
    7）Adapter复用 BaseRecyclerViewAdapterHelper Adapter复用工具类

## 详细介绍：
#### 1）Gradle公共变量提取

在跟目录下的build.gradle中增加如下内容
```
allprojects {
    apply from: "${project.rootDir}/common_build.gradle"
}
```

#### 2）双进程守护JNI实现
sigaction用于信号处理，sa.sa_flags=SA_RESTART：使被信号打断的系统调用自动重新发起
信号处理交给sig_handler处理的，当子进程挂了的时候会向其父进程发送一个SIGCHLD信号，
父进程就会收到SIGCHLD信号，并且开始执行sig_handler方法，重生成子进程

 调用am.startservice重新开启服务 要制定服务的进程id
 ```
 execlp("am", "am", "startservice", "--user", user_id,
      "com.pybeta.daymatter.service/com.pybeta.daymatter.service.AutoRefreshService",
      (char *) NULL);
 ```

#### 3）GreenDao集成学习


#### 4）沉浸式兼容

android 4.4以上沉浸式以及bar的管理

* 1）基础用法：
```
    ImmersionBar.with(this).init();
```

* 2）高级用法：
```
ImmersionBar.with(this)
    .transparentStatusBar()                      // 透明状态栏，不写默认透明色
    .transparentNavigationBar()                  // 透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
    .transparentBar()                            // 透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
    .statusBarColor(R.color.colorPrimary)        // 状态栏颜色，不写默认透明色
    .navigationBarColor(R.color.colorPrimary)    // 导航栏颜色，不写默认黑色
    .barColor(R.color.colorPrimary)              // 同时自定义状态栏和导航栏颜色，不写默认状态栏为透明色，导航栏为黑色
    .statusBarAlpha(0.3f)                        // 状态栏透明度，不写默认0.0f
    .navigationBarAlpha(0.4f)                    // 导航栏透明度，不写默认0.0F
    .barAlpha(0.3f)                              // 状态栏和导航栏透明度，不写默认0.0f
    .statusBarDarkFont(true)                     // 状态栏字体是深色，不写默认为亮色
    .flymeOSStatusBarFontColor(R.color.btn3)     // 修改flyme OS状态栏字体颜色
    .fullScreen(true)                            // 有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
    .hideBar(BarHide.FLAG_HIDE_BAR)              // 隐藏状态栏或导航栏或两者，不写默认不隐藏
    .addViewSupportTransformColor(toolbar)       // 设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
    .titleBar(view)                              // 解决状态栏和布局重叠问题，任选其一
    .titleBarMarginTop(view)                     // 解决状态栏和布局重叠问题，任选其一
    .statusBarView(view)                         // 解决状态栏和布局重叠问题，任选其一
    .fitsSystemWindows(true)                     // 解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
    .supportActionBar(true)                      // 支持ActionBar使用
    .statusBarColorTransform(R.color.orange)     // 状态栏变色后的颜色
    .navigationBarColorTransform(R.color.orange) // 导航栏变色后的颜色
    .barColorTransform(R.color.orange)           // 状态栏和导航栏变色后的颜色
    .removeSupportView(toolbar)                  // 移除指定view支持
    .removeSupportAllView()                      // 移除全部view支持
    .navigationBarEnable(true)                   // 是否可以修改导航栏颜色，默认为true
    .navigationBarWithKitkatEnable(true)         // 是否可以修改安卓4.4和emui3.1手机导航栏颜色，默认为true
    .fixMarginAtBottom(true)                     // 已过时，当xml里使用android:fitsSystemWindows="true"属性时,解决4.4和emui3.1手机底部有时会出现多余空白的问题，默认为false，非必须
    .addTag("tag")                               // 给以上设置的参数打标记
    .getTag("tag")                               // 根据tag获得沉浸式参数
    .reset()                                     // 重置所以沉浸式参数
    .keyboardEnable(true)                        // 解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
    .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)// 单独指定软键盘模式
    .setOnKeyboardListener(new OnKeyboardListener() {// 软键盘监听回调

        @Override
        public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
            LogUtils.e(isPopup);                 // isPopup为true，软键盘弹出，为false，软键盘关闭
        }
    })
    .init();                                     // 必须调用方可沉浸式
```
#### 5）Volley框架集成
Project/libraries/volley
同时导入sdk/platforms/android-25/optional/org.apache.http.legacy.jar

#### 6）Retrofit框架集成



#### 7）多Dex
在对应Model中增加如下配置
```
android {
    ...

    defaultConfig {
        ...

        // 多Dex
        multiDexEnabled true
    }
}

dependencies {
    ...
    implementation 'com.android.support:multidex:1.0.0'
}
```
```
public class App extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
```

#### 8）LeakCanary
```
In your build.gradle:
dependencies {
  debugImplementation 'com.squareup.leakcanary:leakcanary-android:1.5.4'
  releaseImplementation 'com.squareup.leakcanary:leakcanary-android-no-op:1.5.4'
}


In your Application class:
public class ExampleApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);
    // Normal app init code...
  }
}
```

## Contact Me

* Email: limingxiao117@126.com
* WeChat:

![image](https://github.com/limingxiao117/AndroidStudy/blob/master/docs/my_mmqrcode.png)
