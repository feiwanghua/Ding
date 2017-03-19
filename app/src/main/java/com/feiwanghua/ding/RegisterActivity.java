package com.feiwanghua.ding;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

public class RegisterActivity extends Activity {

    private EditText mID;
    private EditText mName;
    private EditText mPassword;
    private EditText mConPassword;
    private EditText mEmail;
    private EditText mPhone;
    private Button mRegiester;
    private TextView mMsg;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView(){
        mID = (EditText) findViewById(R.id.no);
        mName = (EditText) findViewById(R.id.name);
        mPassword = (EditText) findViewById(R.id.password);
        mConPassword = (EditText) findViewById(R.id.con_password);
        mEmail = (EditText) findViewById(R.id.email);
        mPhone = (EditText) findViewById(R.id.phone);
        mRegiester = (Button) findViewById(R.id.register);
        mMsg = (TextView) findViewById(R.id.msg);
        mRegiester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final UserInfo userInfo = new UserInfo(mID.getText().toString(),
                                                        mName.getText().toString(),
                                                        mPassword.getText().toString(),
                                                        mEmail.getText().toString(),
                                                        mPhone.getText().toString(),
                                                        null);
                UserHelper.getInstance().checkUserRegisterStatus(userInfo,
                    new UserHelper.Callback(){

                        @Override
                        public void succeed(UserInfo user) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    UserHelper.getInstance().register(userInfo, new UserHelper.Callback() {
                                        @Override
                                        public void succeed(UserInfo u) {
                                            Log.v("albert", "regiester succeed");
                                            mMsg.setText("注册成功");
                                            mMsg.setVisibility(View.VISIBLE);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                  finish();
                                                }
                                            },1000);
                                        }

                                        @Override
                                        public void failed() {
                                            Log.v("albert", "regiester failed");
                                            showMsg("注册失败");
                                        }

                                    });
                                }
                            },1000);
                        }

                        @Override
                        public void failed() {
                            Log.v("albert","already regiester");
                            Toast.makeText(getApplicationContext(),"already regiester",Toast.LENGTH_LONG);
                        }

                    });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                UploadUtil.imageUpload(uri, new UploadUtil.Callback() {
                    @Override
                    public void succeed(Uri uri) {

                    }

                    @Override
                    public void failed() {

                    }
                });
            }

        }
    }

    private void showMsg(String msg){
        mMsg.setText(msg);
        mMsg.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMsg.setVisibility(View.GONE);
            }
        },500);
    }

}
