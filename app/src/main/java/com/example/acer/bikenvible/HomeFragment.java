package com.example.acer.bikenvible;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by acer on 2018/3/22.
 */

public class HomeFragment extends Fragment implements OnGetGeoCoderResultListener,
        OnGetSuggestionResultListener {
    private MapView mMapView;
    //百度地图数据
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener = new MyLocationListener();
    public static double latitude, longitude;
    private boolean isFirstLocation = true;
    //顶部搜索框
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private BaiduMap mBaiduMap = null;
    private List<String> suggest;
    private AppCompatAutoCompleteTextView keyWorldsView = null;
    private ArrayAdapter<String> sugAdapter = null;
    //目的地经纬度
    public static LatLng endlatLng=null;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    int searchType = 0;  // 搜索的类型，在显示时区分


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getActivity().getApplicationContext());
//        MapView.setMapCustomEnable(true);
//        setMapCustomFile();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //引用创建好的xml布局
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        keyWorldsView = (AppCompatAutoCompleteTextView) view.findViewById(R.id.search);
        //自定义个性化地图
        MapView.setMapCustomEnable(true);
        initMap();
//        // 初始化搜索模块，注册搜索事件监听
//        mPoiSearch = PoiSearch.newInstance();
//        mPoiSearch.setOnGetPoiSearchResultListener(this);
        //建议搜索相关
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        keyWorldsView.setThreshold(2);
        keyWorldsView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {

            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                if (cs.length() <= 0) {
                    return;
                }
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(cs.toString()).city("福州"));
            }
        });
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        keyWorldsView.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    //地图定位相关
    private void initMap() {
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        mMapView. showZoomControls(false);
        mMapView.getChildAt(1).setVisibility(View.INVISIBLE);         ;
    }

    //地图改变监听事件
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
        }
            // 将获取的location信息给百度map
            MyLocationData data = new MyLocationData.Builder().accuracy(0)// location.getRadius()
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(data);
            if (isFirstLocation) {
                isFirstLocation = false;
                // 获取经纬度
                LatLng center = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLngZoom(center,12);
                mBaiduMap.animateMapStatus(status);// 动画的方式到中间
                latitude = location.getLatitude();    //获取纬度信息
                longitude = location.getLongitude();    //获取经度信息
            }


        }

    }

    //设置个性化地图
    public void setMapCustomFile() {
        FileOutputStream out = null;
        InputStream inputStream = null;
        try {
            inputStream = getActivity().getApplicationContext().getAssets().open("custom_map.txt");
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            String moduleName = getActivity().getApplicationContext().getFilesDir().getAbsolutePath();
            File f = new File(moduleName + "/" + "custom_map.txt");
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            out = new FileOutputStream(f);
            out.write(b);
            //  Log.i("ss","setCustomMapStylePath->  " + moduleName + "/map_style.txt");
            MapView.setCustomMapStylePath(moduleName + "/custom_map.txt");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    //重写建议搜索结果
    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        suggest = new ArrayList<String>();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key);
            }
        }
        sugAdapter = new ArrayAdapter<String>(getActivity(), R.layout.adapter_list_item,R.id.tv, suggest);
        keyWorldsView.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();
        mSearch.geocode(new GeoCodeOption().city("福州").address( keyWorldsView.getText().toString()));
    }


    //地理位置反编码
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
//                    .show();
            return;
        }
        endlatLng=new LatLng(result.getLocation().latitude,result.getLocation().longitude);


    }
//获取地理反编码后的选择地点的坐标
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {

            return;
        }
        endlatLng=new LatLng(result.getLocation().latitude,result.getLocation().longitude);

    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener{
        public void onItemClick(AdapterView<?>parent, View view,int poition,long id){
            if(endlatLng!=null){
                mBaiduMap.clear();
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLngZoom(endlatLng,17);
                mBaiduMap.animateMapStatus(status);// 动画的方式到中间
//                mBaiduMap.addOverlay(new MarkerOptions().position(endlatLng)
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_black_24dp)));

            }
            Toast.makeText(getActivity(),"抱歉未转到相关地点",Toast.LENGTH_LONG).show();
        }
    }

    //自定义的Adapter



}
