package com.eddy.androidstudy.volley;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eddy.androidstudy.App;
import com.eddy.androidstudy.R;
import com.eddy.base.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建 ：eddyli@zmodo.com
 * 时间 ：16:25 on 2018/4/23.
 * 描述 ：//TODO 描述本文件的功能
 * 修改 ：//TODO 每次修改的时候描述修改的地方或才要点
 */
public class VolleyTestActivity extends AppCompatActivity implements View.OnClickListener {

    private static RequestQueue requestQueue = Volley.newRequestQueue(App.getInstance().getApplicationContext());

    private TextView mTvContent;
    private Button   mBtnVolley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        mTvContent = findViewById(R.id.txt_content);

        mBtnVolley = findViewById(R.id.btn_test_volley);
        mBtnVolley.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_test_volley) {
            request();
        }
    }

    private void request() {
        String url = "https://13-appaccess.myzmodo.com/meshare/user/usrlogin";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.d(response.toString());
                mTvContent.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d(error.toString());
                mTvContent.setText(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("username", "eddyli@zmodo.com");
                map.put("password", "e10adc3949ba59abbe56e057f20f883e");
                map.put("clienttype", "0");
                map.put("language", "en");
                map.put("platform", "1");
                map.put("offset_second", "0");
                map.put("app_version", "3.3");
                JSONObject mobileInfo = new JSONObject();
                try {
                    mobileInfo.put("version_code", 342);
                    mobileInfo.put("version_name", "4.0.1.00117");
                    mobileInfo.put("MODEL", "PREVIEW - Google Pixel - 8.0 - API 26 - 1080x1920");
                    mobileInfo.put("SYS_SDK", 26);
                    mobileInfo.put("SYS_RELEASE", "8.0.0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.put("app_info", mobileInfo.toString());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
