package com.ssuandroid.my_parttime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class WorkedTimeDialogFragment extends DialogFragment implements View.OnClickListener{


    public WorkedTimeDialogFragment() {}//생성자
    public static WorkedTimeDialogFragment getInstance() {
        WorkedTimeDialogFragment e = new WorkedTimeDialogFragment();
        return e;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.dialogfragment_worked_time, container);
        Button closeButton = (Button) v.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");
        if (v.getId()==R.id.closeButton){
            if (fragment!=null){
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }
        }
        else if (v.getId()==R.id.inputButton){
            if (fragment!=null){

                //input 전달하는 코드 추가해야 함

                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();

            }
        }
    }


}