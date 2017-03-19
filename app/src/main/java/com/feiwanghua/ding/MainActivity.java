package com.feiwanghua.ding;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.albert.firebase.UploadUtil;
import com.albert.firebase.UserInfo;
import com.feiwanghua.ding.baidumap.BaiduMapView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    BaiduMapView mBaiduMapView;
    NavigationView mNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mBaiduMapView = new BaiduMapView(this);

        mNavigationView.getHeaderView(0).setOnClickListener(mLoginListener);
    }

    View.OnClickListener mLoginListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            startActivityForResult(new Intent(MainActivity.this,LoginActivity.class),100);
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                UploadUtil.imageUpload(uri,new UploadUtil.Callback(){
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaiduMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBaiduMapView.onResume();
        resumeLogin();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBaiduMapView.onPause();
    }

    private void resumeLogin(){

        if(!LoginInfo.getName(getApplicationContext()).equals("")){
            ((TextView)mNavigationView.getHeaderView(0).findViewById(R.id.name)).setText(LoginInfo.getName(getApplicationContext()));
        }

        if(!LoginInfo.getPhoto(getApplicationContext()).equals("")){
            ImageView imageview = ((ImageView)mNavigationView.getHeaderView(0).findViewById(R.id.photo));
        }

        if(!LoginInfo.getEmail(getApplicationContext()).equals("")){
            ((TextView)mNavigationView.getHeaderView(0).findViewById(R.id.email)).setText(LoginInfo.getEmail(getApplicationContext()));

        }
    }
}
