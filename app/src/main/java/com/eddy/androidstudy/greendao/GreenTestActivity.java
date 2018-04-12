package com.eddy.androidstudy.greendao;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eddy.androidstudy.R;
import com.eddy.androidstudy.greendao.db.StudentDaoOpe;
import com.eddy.androidstudy.greendao.entity.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建 ：eddyli@zmodo.com
 * 时间 ：14:08 on 2018/4/12.
 * 描述 ：//TODO 描述本文件的功能
 * 修改 ：//TODO 每次修改的时候描述修改的地方或才要点
 */

public class GreenTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Button   mBtnAdd;
    private Button   mBtnDelete;
    private Button   mBtnModify;
    private Button   mBtnQuery;
    private Button   mBtnQueryAll;
    private Button   mBtnDeleteAll;
    private TextView mTvContent;
    //
    private List<Student> studentList = new ArrayList<>();
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_green_dao);

        mBtnAdd = findViewById(R.id.btn_add);
        mBtnDelete = findViewById(R.id.btn_delete);
        mBtnModify = findViewById(R.id.btn_modify);
        mBtnQuery = findViewById(R.id.btn_query);
        mBtnQueryAll = findViewById(R.id.btn_query_all);
        mBtnDeleteAll = findViewById(R.id.btn_delete_all);
        mTvContent = findViewById(R.id.tv_content);

        mBtnAdd.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mBtnModify.setOnClickListener(this);
        mBtnQuery.setOnClickListener(this);
        mBtnQueryAll.setOnClickListener(this);
        mBtnDeleteAll.setOnClickListener(this);

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        for (int i = 0; i < 100; i++) {
            Student student = new Student((long) i, "huang" + i, 25, "666" + i);
            studentList.add(student);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                StudentDaoOpe.insertData(this, studentList);
                break;
            case R.id.btn_delete:
                Student student = new Student((long) 5, "haung" + 5, 25, "123456");
                /**
                 * 根据特定的对象删除
                 */
                StudentDaoOpe.deleteData(this, student);
                /**
                 * 根据主键删除
                 */
                StudentDaoOpe.deleteByKeyData(this, 7);
                StudentDaoOpe.deleteAllData(this);
                break;
            case R.id.btn_modify:
                student = new Student((long) 2, "caojin", 1314, "888888");
                StudentDaoOpe.updateData(this, student);
                break;
            case R.id.btn_query:
                List<Student> students = StudentDaoOpe.queryAll(this);
                mTvContent.setText(students.toString());
                for (int i = 0; i < students.size(); i++) {
                    Log.i("Log", students.get(i).getName());
                }

                break;
            case R.id.btn_delete_all:
                StudentDaoOpe.deleteAllData(this);
                break;
            case R.id.btn_query_all:
                List<Student> students2 = StudentDaoOpe.queryPaging(page, 20, this);

                if (students2.size() == 0) {
                    Toast.makeText(this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                }
                for (Student st : students2) {
                    Log.e("TAG", "onViewClicked: ==" + st);
                    Log.e("TAG", "onViewClicked: == num = " + st.getNum());
                }
                page++;
                mTvContent.setText(students2.toString());
                break;
        }
    }

}
