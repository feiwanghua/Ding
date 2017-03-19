package com.feiwanghua.ding;

import android.app.Activity;
import android.os.Bundle;

import com.feiwanghua.ding.baidumap.BaiduMapView;

public class MapActivity extends Activity {

    BaiduMapView mBaiduMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mBaiduMapView = new BaiduMapView(this);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBaiduMapView.onPause();
    }
}
