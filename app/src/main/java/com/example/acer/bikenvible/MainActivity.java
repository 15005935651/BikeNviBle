package com.example.acer.bikenvible;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity{

    FragmentManager fm = getFragmentManager();
    FragmentTransaction tran = fm.beginTransaction();
    HomeFragment f1=new HomeFragment();
    MoreFragment f2=new MoreFragment();



    //底部导航栏里的监听
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentManager fm_1 = getFragmentManager();
                    FragmentTransaction tran_0 = fm_1.beginTransaction();
                    tran_0.show(f1).hide(f2).commit();
                    return true;
                case R.id.navigation_dashboard:
                  //  searchView.setVisibility(searchView.GONE);
                    FragmentManager fm_2 = getFragmentManager();
                    FragmentTransaction tran_1 = fm_2.beginTransaction();
                    tran_1.show(f2).hide(f1).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化程序所有数据
        initAll();
    }

    //用来初始化程序的所有数据
    private void initAll() {
        //初始化界面里的控件
        initControl();
        //初始化主界面fragment里的地图控件
        initHomeFragment();
    }

    //初始化各种控件的数据
    private void initControl() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    //初始化打开界面主页的fragment布局
    private void initHomeFragment() {

        tran.add(R.id.message, f1, "index").show(f1).add(R.id.message, f2, "bill").hide(f2);
        tran.commit();
    }


}
