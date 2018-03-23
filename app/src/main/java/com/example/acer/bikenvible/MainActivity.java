package com.example.acer.bikenvible;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    SearchView searchView;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    searchView.setVisibility(searchView.VISIBLE);
                    //获取到FragmentManager，在V4包中通过getSupportFragmentManager，
                    //在系统中原生的Fragment是通过getFragmentManager获得的。
                    FragmentManager FM = getFragmentManager();
                    //2.开启一个事务，通过调用beginTransaction方法开启。
                    FragmentTransaction MfragmentTransaction =FM.beginTransaction();
                    //把自己创建好的fragment创建一个对象
                    HomeFragment  f1 = new HomeFragment();
                    //向容器内加入Fragment，一般使用add或者replace方法实现，需要传入容器的id和Fragment的实例。
                    MfragmentTransaction.add(R.id.message,f1);
                    //提交事务，调用commit方法提交。
                    MfragmentTransaction.commit();
                    return true;
                case R.id.navigation_dashboard:

                    searchView.setVisibility(searchView.GONE);
                    //获取到FragmentManager，在V4包中通过getSupportFragmentManager，
                    //在系统中原生的Fragment是通过getFragmentManager获得的。
                    FragmentManager FMs = getFragmentManager();
                    //2.开启一个事务，通过调用beginTransaction方法开启。
                    FragmentTransaction MfragmentTransactions = FMs.beginTransaction();
                    //把自己创建好的fragment创建一个对象
                    MoreFragment f2 = new MoreFragment();
                    //向容器内加入Fragment，一般使用add或者replace方法实现，需要传入容器的id和Fragment的实例。
                    MfragmentTransactions.replace(R.id.message,f2);
                    //提交事务，调用commit方法提交。
                    MfragmentTransactions.commit();

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        searchView=(SearchView)findViewById(R.id.search);




        FragmentManager FM = getFragmentManager();
        FragmentTransaction MfragmentTransaction =FM.beginTransaction();
        HomeFragment  f1 = new HomeFragment();
        MfragmentTransaction.replace(R.id.message,f1);
        MfragmentTransaction.commit();


    }



}
