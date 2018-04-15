package com.eddy.androidstudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eddy.androidstudy.daemon.DaemonService;
import com.eddy.androidstudy.greendao.GreenTestActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("study");
    }

    private Button mBtnGreenDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initial();

        // Example of a call to a native method
        Log.d("eddy", stringFromJNI());

        mBtnGreenDao = findViewById(R.id.btn_green_dao);
        mBtnGreenDao.setOnClickListener(this);

    }

    private void initial() {
        if (Constants.IS_NEED_DAEMON_PROCESS) {
            Intent intent = new Intent(this, DaemonService.class);
            startService(intent);
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_green_dao:
                startActivity(new Intent(this, GreenTestActivity.class));
                break;
        }
    }
}
