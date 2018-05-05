package com.eddy.androidstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.eddy.androidstudy.daemon.DaemonService;
import com.eddy.androidstudy.greendao.GreenTestActivity;
import com.eddy.androidstudy.recyclerview.WelcomeActivity;
import com.eddy.androidstudy.retrofit.RetrofitTestActivity;
import com.eddy.androidstudy.volley.VolleyTestActivity;
import com.eddy.base.Logger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("study");
    }

    private Button mBtnGreenDao;
    private Button mBtnVolley;
    private Button mBtnRetrofit;
    private Button mBtnBaseRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initial();

        // Example of a call to a native method
        Logger.d(stringFromJNI());

        mBtnGreenDao = findViewById(R.id.btn_green_dao);
        mBtnGreenDao.setOnClickListener(this);

        mBtnVolley = findViewById(R.id.btn_volley_test);
        mBtnVolley.setOnClickListener(this);

        mBtnRetrofit = findViewById(R.id.btn_retrofit_test);
        mBtnRetrofit.setOnClickListener(this);

        mBtnBaseRecyclerView = findViewById(R.id.btn_base_recycler_adapter_test);
        mBtnBaseRecyclerView.setOnClickListener(this);


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
            case R.id.btn_volley_test:
                startActivity(new Intent(this, VolleyTestActivity.class));
                break;
            case R.id.btn_retrofit_test:
                startActivity(new Intent(this, RetrofitTestActivity.class));
                break;
            case R.id.btn_base_recycler_adapter_test:
                startActivity(new Intent(this, WelcomeActivity.class));
                break;
        }
    }
}
