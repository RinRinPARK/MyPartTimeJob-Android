package com.ssuandroid.my_parttime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastCustom extends Toast {
    Context mContext;
    public ToastCustom(Context context){
        super(context);
        mContext= context;

    }

    public void showToast(String message){
        LayoutInflater inflater;
        View v;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v= inflater.inflate(R.layout.toast_custom, null);

        TextView text = (TextView) v.findViewById(R.id.text_toast);
        text.setText(message);

        show(this, v, Toast.LENGTH_LONG);
    }

    private void show(Toast toast, View v, int duration){
        toast.setDuration(duration);
        toast.setView(v);
        toast.show();
    }
}
