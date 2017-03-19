package com.feiwanghua.ding;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.albert.firebase.UploadUtil;
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
    private TextView mMsg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        mNo = (EditText) findViewById(R.id.no);
        mPassword = (EditText) findViewById(R.id.password);
        mMsg = (TextView) findViewById(R.id.msg);
        mLogin = (Button) findViewById(R.id.login);
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
                UserHelper.getInstance().login(new UserInfo(mNo.getText().toString(),null,mPassword.getText().toString(),null,null,null),
                    new UserHelper.Callback(){

                        @Override
                        public void succeed(UserInfo userInfo) {
                            Log.v("albert","login succeed");
                            LoginInfo.setUserInfo(getApplicationContext(),userInfo);
                            finish();
                        }

                        @Override
                        public void failed() {
                            Log.v("albert","login failed");
                            mMsg.setText("登录失败");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mMsg.setText("");
                                }
                            },2000);
                            mLogin.setEnabled(true);

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
