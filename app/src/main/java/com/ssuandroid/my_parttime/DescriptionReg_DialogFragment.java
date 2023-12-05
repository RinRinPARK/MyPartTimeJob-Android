package com.ssuandroid.my_parttime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class DescriptionReg_DialogFragment extends DialogFragment implements View.OnClickListener {
    public DescriptionReg_DialogFragment() {
    }


    public static DescriptionReg_DialogFragment getInstance() {
        DescriptionReg_DialogFragment e = new DescriptionReg_DialogFragment();
        return e;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogfragment_descriptionreg, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        TextView name = (TextView) v.findViewById(R.id.description_name);
        TextView description = (TextView) v.findViewById(R.id.description_text);

        Button closeButton = (Button) v.findViewById(R.id.description_closeBtn);
        closeButton.setOnClickListener(this);
        Button inputButton = (Button) v.findViewById(R.id.description_regBtn);
        inputButton.setOnClickListener(this);

        setCancelable(false); //화면 터치 시 꺼짐을 막음

    }

    @Override
    public void onClick(View v) {
        ///////calendar에서 이 dialogfragment 띄울 때 tag 지정해서 해당 tag로 바꾸어줘야함!!
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("DAETA_CALENDAR_DESCRIPTION_TAG");
        if (v.getId()==R.id.description_closeBtn){
            if (fragment!=null){
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }
        }
        else if (v.getId()==(R.id.description_regBtn)){
            //알바 취소되는 로직 필요 (fragment와 연결)//

                if (fragment!=null){
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismiss();
                }
        }
    }
}