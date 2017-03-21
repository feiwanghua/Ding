package com.feiwanghua.ding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.albert.firebase.UserHelper;
import com.albert.firebase.UserInfo;

/**
 * Created by feiwanghua on 2017/3/19.
 */

public class LoginActivity extends Activity {

    private EditText mNo;
    private EditText mPassword;
    private Button mLogin;
    private TextView mRegister;
    private TextView mLoading;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        mNo = (EditText) findViewById(R.id.no);
        mPassword = (EditText) findViewById(R.id.password);
        mLogin = (Button) findViewById(R.id.login);
        mLoading = (TextView) findViewById(R.id.loading);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(mNo.getText())){
                    Toast.makeText(getApplicationContext(),"请填学号",Toast.LENGTH_SHORT);
                    return;
                }
                if(TextUtils.isEmpty(mPassword.getText())){
                    Toast.makeText(getApplicationContext(),"请填密码",Toast.LENGTH_SHORT);
                    return;
                }
                mLogin.setEnabled(false);
                mLoading.setVisibility(View.VISIBLE);
                UserHelper.getInstance().login(new UserInfo(mNo.getText().toString(),null,mPassword.getText().toString(),null,null,null),
                    new UserHelper.Callback(){

                        @Override
                        public void succeed(UserInfo userInfo) {
                            Log.v("albert","login succeed");
                            Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                            LoginInfo.setUserInfo(getApplicationContext(),userInfo);
                            mLoading.setVisibility(View.INVISIBLE);
                            finish();
                        }

                        @Override
                        public void failed() {
                            Log.v("albert","login failed");
                            Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_SHORT).show();
                            mLogin.setEnabled(true);
                            mLoading.setVisibility(View.INVISIBLE);

                        }
                    });

            }
        });
        mRegister = (TextView)findViewById(R.id.toregister);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

}
