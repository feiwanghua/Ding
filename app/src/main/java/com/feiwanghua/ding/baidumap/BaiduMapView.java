package com.feiwanghua.ding.baidumap;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.feiwanghua.ding.R;

/**
 * Created by feiwanghua on 2017/3/17.
 */

public class BaiduMapView implements View.OnClickListener{

    private Activity mActivity;
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LatLng mPointDes = new LatLng(39.963175, 116.400244);
    private boolean focusable = true;
    private TextView mDesLocation;
    private TextView mDesLocationInfo;
    private TextView mPresentLocation;
    private TextView mPresentLocationInfo;
    private TextView mPresentDistance;
    private ImageView mFocusView;

    public BaiduMapView(Activity activity) {
        mActivity = activity;
        initViews();
        initBaiduMap();
        initLocation();
        addMark();
    }

    private void initViews() {
        //获取地图控件引用
        mMapView = (MapView) mActivity.findViewById(R.id.bmapView);
        mDesLocation = (TextView) mActivity.findViewById(R.id.desLocation);
        mDesLocationInfo = (TextView) mActivity.findViewById(R.id.desLocationInfo);
        mPresentLocation = (TextView) mActivity.findViewById(R.id.presentLocation);
        mPresentLocationInfo = (TextView) mActivity.findViewById(R.id.presentLocationInfo);
        mPresentDistance = (TextView) mActivity.findViewById(R.id.presentDistance);
        mFocusView = (ImageView) mActivity.findViewById(R.id.focus);
        mFocusView.setOnClickListener(this);
    }

    private void initBaiduMap() {
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //卫星地图
        // mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //空白地图, 基础地图瓦片将不会被渲染。在地图类型中设置为NONE，将不会使用流量下载基础地图瓦片图层。使用场景：与瓦片图层一起使用，节省流量，提升自定义瓦片图下载速度。
        //mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
    }

    private void initLocation() {
        mLocationClient = new LocationClient(mActivity.getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mLocationClient.start();
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.focus:
                focusable = true;
                break;
        }
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            Address address = location.getAddress();
            if (location != null) {
                Log.v("albert", "country:" + address.country);
                Log.v("albert", "province:" + address.province);
                Log.v("albert", "city:" + address.city);
                Log.v("albert", "street:" + address.street);
                String s = "详细信息：" + address.country + " ";
                s += address.province + " ";
                s += address.city + " ";
                s += address.street;
                mPresentLocationInfo.setText(s);
            }
            updatePosition(location);
            mPresentLocation.setText("当前坐标：( " + location.getLatitude() + " , " + location.getLongitude() + " )");
        }
    }

    private void updatePosition(BDLocation location) {
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_marka);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, false, null);
        mBaiduMap.setMyLocationConfigeration(config);
        // 当不需要定位图层时关闭定位图层
        //mBaiduMap.setMyLocationEnabled(false)
        if (focusable) {
            focusCenterLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        }
        mPresentDistance.setText("相距：" + (int) DistanceUtil.getDistance(new LatLng(location.getLatitude(), location.getLongitude()), mPointDes) + "米");
    }

    /*
    * 标记目的地坐标
    * */
    private void addMark() {
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(mPointDes)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        mDesLocation.setText("签到坐标：( " + mPointDes.latitude + " , " + mPointDes.longitude + " )");
        mDesLocationInfo.setText("详细信息：" + "中国 北京 天安门");
    }


    /*
    * 聚焦到中点
    * */
    private void focusCenterLocation(LatLng point) {
        //设定中心点坐标
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(12)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        focusable = false;
    }

    public void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    public void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    public void onPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
