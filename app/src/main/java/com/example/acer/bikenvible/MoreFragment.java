package com.example.acer.bikenvible;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by acer on 2018/3/22.
 */

public class MoreFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //引用创建好的xml布局
        View view = inflater.inflate(R.layout.more_fragment,container,false);
        NavigationView navigationView = (NavigationView)view.findViewById(R.id.more_view);
        navigationView.setNavigationItemSelectedListener(this);
        return view;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.ofo) {
            //打开ofo
           Open_App("so.ofo.labofo");
        } else if (id == R.id.mobai) {
            //打开摩拜单车
            Open_App("com.mobike.mobikeapp");
        } else if (id == R.id.bluetooth) {
            show_my_bluetooh_dialog();
        } else if (id == R.id.share) {
            share();
        } else if (id == R.id.about_us) {
            show_about_us_dialog();
        }
        return true;
    }
//
    private void Open_App(String PackageName){
        PackageInfo packageInfo;//PackageInfo所在包为android.content.pm
        try {
            packageInfo = getActivity().getPackageManager().getPackageInfo(PackageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo ==null){
            Toast.makeText(getActivity(), "您的手机没有安装该应用!", Toast.LENGTH_LONG).show();
        }else{
            PackageManager packageManager =  getActivity().getPackageManager();
            Intent it= packageManager.getLaunchIntentForPackage(PackageName);
            getActivity().startActivity(it);
        }
    }

    private void show_about_us_dialog(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle("邮箱");
        builder.setMessage("\n804052545@qq.com");
        builder.setPositiveButton("确定", null);
        builder.show();
    }
    private void show_my_bluetooh_dialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Mac地址");
        builder.setMessage("\n"+getBtAddressByReflection());
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    public static String getBtAddressByReflection() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Field field = null;
        try {
            field = BluetoothAdapter.class.getDeclaredField("mService");
            field.setAccessible(true);
            Object bluetoothManagerService = field.get(bluetoothAdapter);
            if (bluetoothManagerService == null) {
                return null;
            }
            Method method = bluetoothManagerService.getClass().getMethod("getAddress");
            if(method != null) {
                Object obj = method.invoke(bluetoothManagerService);
                if(obj != null) {
                    return obj.toString();
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void share(){
        Intent intent1=new Intent(Intent.ACTION_SEND);
        intent1.putExtra(Intent.EXTRA_TEXT,"我在使用G-Watch骑行，加入我们！");
        intent1.setType("text/plain");
        startActivity(Intent.createChooser(intent1,"share"));
    }
}