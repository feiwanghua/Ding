package com.feiwanghua.ding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.albert.firebase.UploadUtil;
import com.albert.firebase.UserHelper;
import com.albert.firebase.UserInfo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private ImageView mPhoto;
    private TextView mPhotoMsg;
    private Uri mUri;
    private TextView mLoading;
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
        mPhoto = (ImageView) findViewById(R.id.photo);
        mLoading = (TextView)findViewById(R.id.loading);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });
        mPhotoMsg = (TextView) findViewById(R.id.photo_msg);
        mRegiester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkUserinfo()){
                    final UserInfo userInfo = new UserInfo(mID.getText().toString(),
                                                            mName.getText().toString(),
                                                            mPassword.getText().toString(),
                                                            mEmail.getText().toString(),
                                                            mPhone.getText().toString(),
                                                            mUri.toString());
                    mLoading.setVisibility(View.VISIBLE);
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
                                                Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                                                mLoading.setVisibility(View.INVISIBLE);
                                                finish();
                                            }

                                            @Override
                                            public void failed() {
                                                Log.v("albert", "regiester failed");
                                                Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
                                                mLoading.setVisibility(View.INVISIBLE);
                                            }

                                        });
                                    }
                                },1000);
                            }

                            @Override
                            public void failed() {
                                Log.v("albert","already regiester");
                                Toast.makeText(getApplicationContext(),"该用户已经注册",Toast.LENGTH_LONG).show();
                                mLoading.setVisibility(View.INVISIBLE);
                            }

                        });

                }
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
                        mPhotoMsg.setText("头像上传成功");
                        mUri = uri;
                    }

                    @Override
                    public void failed() {
                        mPhotoMsg.setText("头像上传失败");
                    }
                });
                Picasso.with(getApplicationContext()).load(uri).into(mPhoto);
                mPhotoMsg.setText("正在上传头像");
                mPhotoMsg.setVisibility(View.VISIBLE);
            }

        }
    }

    private boolean checkUserinfo(){
        if(TextUtils.isEmpty(mID.getText())){
            Toast.makeText(getApplicationContext(),"学号不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mName.getText())){
            Toast.makeText(getApplicationContext(),"姓名不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mPassword.getText())){
            Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!mPassword.getText().toString().equals(mConPassword.getText().toString())){
            Toast.makeText(getApplicationContext(),"确认密码不正确",Toast.LENGTH_SHORT).show();
            return false;
        }
//        if(PhoneFormatCheckUtils.isPhoneLegal(mPhone.getText().toString())){
//            Toast.makeText(getApplicationContext(),"手机号不正确",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if(checkEmaile(mEmail.getText().toString())){
//            Toast.makeText(getApplicationContext(),"请填写正确的邮箱",Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }

    /**
     * 正则表达式校验邮箱
     * @param emaile 待匹配的邮箱
     * @return 匹配成功返回true 否则返回false;
     */
    private boolean checkEmaile(String emaile){
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(emaile);
        //进行正则匹配
        return m.matches();
    }


}
