package com.ssuandroid.my_parttime;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class CodeInputDialogFragment extends DialogFragment implements View.OnClickListener{


    public CodeInputDialogFragment() {}//생성자
    public static CodeInputDialogFragment getInstance() {
        CodeInputDialogFragment e = new CodeInputDialogFragment();
        return e;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.dialogfragment_codeinput, container);

        Button closeButton = (Button) v.findViewById(R.id.codecloseButton);
        closeButton.setOnClickListener(this);

        Button inputButton = (Button) v.findViewById(R.id.codeinputButton);
        inputButton.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("HOME_TAG");
        if (v.getId()==R.id.codecloseButton){
            Log.d("ymj","here");
            if (fragment!=null){
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }
        }
        else if (v.getId()==R.id.codeinputButton){
            if (fragment!=null){
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();

                HourlyRateDialogFragment hourlyRateDialogFragment= new HourlyRateDialogFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                hourlyRateDialogFragment.show(transaction, "HOME_TAG");
            }
        }
    }


}
