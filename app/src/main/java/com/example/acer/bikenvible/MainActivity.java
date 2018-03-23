package com.example.acer.bikenvible;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.acer.control_class.DialogUtil;
import com.example.acer.control_class.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {
    int mWidth;
    SearchView searchView;
    private String[] items = {"福州大学旗山校区图书馆", "开始骑行", "预计花费24分钟"};

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
        initData();
        showDialog();
    }


    private void initData() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;
    }


    private void showDialog() {

        DialogUtil.showItemSelectDialog(MainActivity.this, mWidth
                , onIllegalListener
                , "福州大学旗山校区图书馆"
                , "开始骑行"
                , "预计花费24分钟"
                );//可填添加任意多个Item呦
    }

    private OnItemSelectedListener onIllegalListener = new OnItemSelectedListener() {
        @Override
        public void getSelectedItem(String content) {
            Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
        }
    };



}
