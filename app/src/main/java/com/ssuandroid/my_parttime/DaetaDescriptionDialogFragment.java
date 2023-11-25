package com.ssuandroid.my_parttime;

import android.os.Bundle;
import android.util.Log;
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

import java.text.SimpleDateFormat;

public class DaetaDescriptionDialogFragment extends DialogFragment implements View.OnClickListener {
    private Daeta daeta;
    int position;
    public DaetaDescriptionDialogFragment() {
    }

    public DaetaDescriptionDialogFragment(Daeta daeta, int position){
        this.daeta= daeta;
        this.position= position;
    }

    public static DaetaDescriptionDialogFragment getInstance() {
        DaetaDescriptionDialogFragment e = new DaetaDescriptionDialogFragment();
        return e;
    }

    //daetafragment에 데이터 넘겨주기 위한 인터페이스
    public interface DaetaInterfacer1 {
        void onDescriptionApplication(Daeta daeta, int position); //추상 메소드
    }

    private DaetaInterfacer1 daetaInterfacer1;
    public void setDaetaInterfacer1(DaetaInterfacer1 daetaInterfacer1){
        this.daetaInterfacer1= daetaInterfacer1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogfragment_daetadescription, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        TextView branchName = (TextView) v.findViewById(R.id.description_branchName);
        TextView date = (TextView) v.findViewById(R.id.description_date);
        TextView time = (TextView) v.findViewById(R.id.description_time);
        TextView dayWage = (TextView) v.findViewById(R.id.description_dayWage);
        TextView wage = (TextView) v.findViewById(R.id.description_wage);
        TextView description = (TextView) v.findViewById(R.id.description_description);

        Button closeButton = (Button) v.findViewById(R.id.description_closeBtn);
        closeButton.setOnClickListener(this);
        Button inputButton = (Button) v.findViewById(R.id.description_applicationBtn);
        inputButton.setOnClickListener(this);

        branchName.setText(daeta.getBranchName());
        Log.d("ymj", daeta.getDate()+"");

        time.setText(daeta.getTime());
        dayWage.setText(Long.toString(daeta.getWage()));
        wage.setText(Long.toString(daeta.getWage()));
        description.setText(daeta.getDescription());

        // Date를 알맞은 형식으로 변환
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy년 M월 d일");
        String formattedDate = newFormat.format(daeta.getDate());
        date.setText(formattedDate);

        setCancelable(false); //화면 터치 시 꺼짐을 막음
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.description_closeBtn){
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("DAETA_DESCRIPTION_TAG");
            if (fragment!=null){
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }
        }
        else if (v.getId()==(R.id.description_applicationBtn)){
            if (daeta!=null){
                daeta.setCovered();
                daetaInterfacer1.onDescriptionApplication(daeta, position);
                //신청 버튼
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("DAETA_DESCRIPTION_TAG");
                if (fragment!=null){
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismiss();
                }
            }
        }
    }
}
