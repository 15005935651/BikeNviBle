package com.example.acer.bikenvible;

/**
 * Created by Administrator on 2018/4/4.
 */

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.acer.bikenvible.adapter.DeviceListAdapter;
import com.hansion.h_ble.BleController;
import com.hansion.h_ble.callback.ConnectCallback;
import com.hansion.h_ble.callback.ScanCallback;

import java.util.ArrayList;
import java.util.List;

public class BluetoohActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ProgressDialog progressDialog;
    private BleController mBleController;
    private static String LOGTAG = "AppCompatActivity";
    //搜索结果列表
    private List<BluetoothDevice> bluetoothDevices = new ArrayList<BluetoothDevice>();
    private ListView mDeviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooh_activity);

        mDeviceList = (ListView) findViewById(R.id.mDeviceList);

        // TODO  第一步：初始化
        mBleController = BleController.getInstance().init(this);

        // TODO  第二步：搜索设备，获取列表后进行展示
        scanDevices();


    }

    private void scanDevices() {

        showProgressDialog("请稍后", "正在搜索设备");

        mBleController.scanBle(0, new ScanCallback() {
            @Override
            public void onSuccess() {
                hideProgressDialog();
                if (bluetoothDevices.size() > 0) {
                    mDeviceList.setAdapter(new DeviceListAdapter(BluetoohActivity.this, bluetoothDevices));
                    mDeviceList.setOnItemClickListener(BluetoohActivity.this);
                } else {
                    Toast.makeText(BluetoohActivity.this, "未搜索到Ble设备", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScanning(BluetoothDevice device, int rssi, byte[] scanRecord) {
                if (!bluetoothDevices.contains(device)) {
                    bluetoothDevices.add(device);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    public void showProgressDialog(String title, String message) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, title, message, true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
        }
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        showProgressDialog("请稍后", "正在连接设备");

        // TODO 第三步：点击条目后,获取地址，根据地址连接设备
        String address = bluetoothDevices.get(i).getAddress();
        mBleController.connect(0, address, new ConnectCallback() {
            @Override
            public void onConnSuccess() {
                hideProgressDialog();
//                startActivity(BluetoohActivity.class,BluetoohActivity.class));
//                String [] s={"hello","go start","turn right","turn left","care for","speed 3m/s"};
//                try{
//                    for(int i=0;i<s.length;i++){
//                        Thread.currentThread().sleep(2000);
//                        mBleController.writeBuffer(s[i].getBytes(), new OnWriteCallback() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onFailed(int state) {
//
//                            }
//                        });
//                    }
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
                //  Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                //  startActivity(new Intent(MainActivity.this,SendAndReciveActivity.class));
            }

            @Override
            public void onConnFailed() {
                hideProgressDialog();
                Toast.makeText(BluetoohActivity.this, "连接超时，请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
