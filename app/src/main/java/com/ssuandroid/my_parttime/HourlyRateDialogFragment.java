package com.ssuandroid.my_parttime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class HourlyRateDialogFragment extends DialogFragment {
//    implements View.OnClickListener // - onClick() 함수 주석 제거 후 사용

    public HourlyRateDialogFragment() {}//생성자
    public static HourlyRateDialogFragment getInstance() {
            HourlyRateDialogFragment e = new HourlyRateDialogFragment();
            return e;
        }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.dialogfragment_hourlyrate, container);
//        Button closeButton = (Button) v.findViewById(R.id.closeButton);
//        closeButton.setOnClickListener(this); //마찬가지로 onClcik() 함수 주석 제거 후 사용
        return v;
    }

    //"tag"를 통해 부모 fragment로 돌아갈 예정
//    @Override
//    public void onClick(View v) {
//        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");
//        if (v.getId()== R.id.closeButton){
//            if (fragment!=null){
//                DialogFragment dialogFragment = (DialogFragment) fragment;
//                dialogFragment.dismiss();
//            }
//        }
//    }
}