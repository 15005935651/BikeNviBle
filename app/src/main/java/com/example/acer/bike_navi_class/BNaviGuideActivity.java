/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.example.acer.bike_navi_class;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBRouteGuidanceListener;
import com.baidu.mapapi.bikenavi.adapter.IBTTSPlayer;
import com.baidu.mapapi.bikenavi.model.BikeRouteDetailInfo;
import com.baidu.mapapi.bikenavi.model.RouteGuideKind;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.hansion.h_ble.BleController;

public class BNaviGuideActivity extends Activity {
    private BleController mBleController;
    private BikeNavigateHelper mNaviHelper;

    BikeNaviLaunchParam param;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNaviHelper.quit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNaviHelper.resume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        mNaviHelper = BikeNavigateHelper.getInstance();
        View view = mNaviHelper.onCreate(BNaviGuideActivity.this);
        if (view != null) {
            setContentView(view);
        }

        mNaviHelper.startBikeNavi(BNaviGuideActivity.this);

        mNaviHelper.setTTsPlayer(new IBTTSPlayer() {
            @Override
            public int playTTSText(String s, boolean b) {
                Log.d("tts", s);
                return 0;
            }
        });

//        mBleController = BleController.getInstance().init(this);
//        String[] s = {"hello", "go start", "turn right", "turn left", "care for", "speed 3m/s"};
//        try {
//            for (int i = 0; i < s.length; i++) {
//                Thread.currentThread().sleep(2000);
//                mBleController.writeBuffer(s[i].getBytes(), new OnWriteCallback() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onFailed(int state) {
//
//                    }
//                });
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        //获取导航途中的诱导信息
        mNaviHelper.setRouteGuidanceListener(this, new IBRouteGuidanceListener() {
            @Override
            public void onRouteGuideIconUpdate(Drawable icon) {

            }

            @Override
            public void onRouteGuideKind(RouteGuideKind routeGuideKind) {

            }

            @Override
            public void onRoadGuideTextUpdate(CharSequence charSequence, CharSequence charSequence1) {

                Toast.makeText(BNaviGuideActivity.this, charSequence.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onRemainDistanceUpdate(CharSequence charSequence) {
                Toast.makeText(BNaviGuideActivity.this, charSequence, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRemainTimeUpdate(CharSequence charSequence) {
                Toast.makeText(BNaviGuideActivity.this, charSequence, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onGpsStatusChange(CharSequence charSequence, Drawable drawable) {

            }

            @Override
            public void onRouteFarAway(CharSequence charSequence, Drawable drawable) {

            }

            @Override
            public void onRoutePlanYawing(CharSequence charSequence, Drawable drawable) {

            }

            @Override
            public void onReRouteComplete() {

            }

            @Override
            public void onArriveDest() {

            }

            @Override
            public void onVibrate() {

            }

            @Override
            public void onGetRouteDetailInfo(BikeRouteDetailInfo bikeRouteDetailInfo) {

            }
        });
    }

}
