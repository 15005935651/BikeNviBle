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


public class MainActivity extends AppCompatActivity{
    //底部地图导航框的宽度
    int mWidth;
    FragmentManager fm = getFragmentManager();
    FragmentTransaction tran = fm.beginTransaction();
    HomeFragment f1=new HomeFragment();
    MoreFragment f2=new MoreFragment();
    //底部地图导航框里的数据
    private String[] items = {"福州大学旗山校区图书馆", "开始骑行", "预计花费24分钟"};


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


    //用来初始化底部导航框里的数据
    private void initNviDialogData() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;
    }

    //用来显示底部导航框的数据
    private void showNviDialog() {
        DialogUtil.showItemSelectDialog(MainActivity.this, mWidth
                , onIllegalListener
                , "福州大学旗山校区图书馆"
                , "开始骑行"
                , "预计花费24分钟"
        );//可填添加任意多个Item呦
    }


    //底部导航框选择后的监听事件
    private OnItemSelectedListener onIllegalListener = new OnItemSelectedListener() {
        @Override
        public void getSelectedItem(String content) {
            Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
        }
    };


    //初始化各种控件的数据
    private void initControl() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    //初始化打开界面主页的fragment布局
    private void initHomeFragment() {
//        initNviDialogData();
//        showNviDialog();
        tran.add(R.id.message, f1, "index").show(f1).add(R.id.message, f2, "bill").hide(f2);
        tran.commit();
    }


}
