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

public class DaetaApplicationDialogFragment extends DialogFragment implements View.OnClickListener {
    private Daeta daeta;
    private int position;
    public DaetaApplicationDialogFragment() {
    }

    public DaetaApplicationDialogFragment(Daeta daeta, int position){
        this.daeta= daeta;
        this.position=position;
    }

    public static DaetaApplicationDialogFragment getInstance() {
        DaetaApplicationDialogFragment e = new DaetaApplicationDialogFragment();
        return e;
    }

    //daetafragment에 데이터 넘겨주기 위한 인터페이스
    public interface DaetaInterfacer2 {
        void onApplication(Daeta daeta, int position); //추상 메소드
    }

    private DaetaInterfacer2 daetaInterfacer2;
    public void setDaetaInterfacer2(DaetaInterfacer2 daetaInterfacer2){
        this.daetaInterfacer2= daetaInterfacer2;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogfragment_daetaapplication, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        TextView textView = (TextView) v.findViewById(R.id.daetaBranchName);
        Button closeButton = (Button) v.findViewById(R.id.application_closeBtn);
        closeButton.setOnClickListener(this);
        Button inputButton = (Button) v.findViewById(R.id.application_applicationBtn);
        inputButton.setOnClickListener(this);

        textView.setText(daeta.getBranchName());

        setCancelable(false); //화면 터치 시 꺼짐을 막음

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.application_closeBtn){
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("DAETA_APPLICATION_TAG");
            if (fragment!=null){
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }
        }
        else if (v.getId()==(R.id.application_applicationBtn)){
            if (daeta!=null){
                daeta.setCovered();
                daetaInterfacer2.onApplication(daeta, position);
                //신청 버튼
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("DAETA_APPLICATION_TAG");
                if (fragment!=null){
                    daeta.setCovered();
//                daeta.setApplicantId(); 신청자 아이디 받아와서 세팅하여 나중에 띄우는 작업 필요할 것으로 보임
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismiss();
                }
            }
        }
    }
}