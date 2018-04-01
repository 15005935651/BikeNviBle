package com.example.acer.control_class;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.acer.bikenvible.R;

/**
 * Created by Administrator on 2018/3/23.
 */

public class DialogUtil {

    /**
     * 此 dialog 效果从底部弹出，可以添加任意多个Item。
     *
     * @param context
     * @param mWidth   需要设置的dialog的宽度
     * @param listener 选择Item会调用此监听
     * @param contents 任意多个Item选项内容（从最顶端到最低端）
     * @return
     */
    public static Dialog showItemSelectDialog(Context context
            , int mWidth
            , final OnItemSelectedListener listener
            , final String... contents) {

        final Dialog mDialog = new Dialog(context, R.style.Dialog_Transparent_Theme);
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_choice, null);
        LinearLayout contentsView = (LinearLayout) rootView.findViewById(R.id.dialogContent);
        for (int i = 0; i < contents.length; i++) {
            if (i == 0) {
                View topView = LayoutInflater.from(context).inflate(R.layout.dialog_top_item, null);
                TextView topText = (TextView) topView.findViewById(R.id.dialog_top);
                topText.setText(contents[0]);
                topText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mDialog.dismiss();
                        if (listener != null)
                            listener.getSelectedItem(contents[0]);
                    }
                });
                contentsView.addView(topView);
            } else if (i == contents.length - 1) {
                View bottomView = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_item, null);
                TextView boottomTv = (TextView) bottomView.findViewById(R.id.dialog_bottom);
                boottomTv.setText(contents[contents.length - 1]);
                boottomTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mDialog.dismiss();
                        if (listener != null)
                            listener.getSelectedItem(contents[contents.length - 1]);
                    }
                });
                contentsView.addView(bottomView);
            } else {
                View centerView = LayoutInflater.from(context).inflate(R.layout.dialog_center_item, null);
                TextView centTv = (TextView) centerView.findViewById(R.id.dialog_center_item);
                final int finalI = i;
                centTv.setText(contents[finalI]);
                centTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mDialog.dismiss();
                        if (listener != null)
                            listener.getSelectedItem(contents[finalI]);
                    }
                });
                contentsView.addView(centerView);
            }
        }
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setContentView(rootView);
        mDialog.setCanceledOnTouchOutside(true);


        Window window = mDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = mWidth * 9 / 10;
        window.setWindowAnimations(R.style.popupAnimation);
        window.setAttributes(params);

        mDialog.show();

        return mDialog;
    }

}