package com.example.acer.bikenvible;

import android.app.Fragment;
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

        } else if (id == R.id.share) {

        } else if (id == R.id.about_us) {

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




}