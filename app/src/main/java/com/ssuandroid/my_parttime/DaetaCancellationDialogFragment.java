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

public class DaetaCancellationDialogFragment extends DialogFragment implements View.OnClickListener {
    private Daeta daeta;
    private int position;
    public DaetaCancellationDialogFragment() {
    }

    public DaetaCancellationDialogFragment(Daeta daeta, int position){
        this.daeta= daeta;
        this.position=position;
    }

    public static DaetaCancellationDialogFragment getInstance() {
        DaetaCancellationDialogFragment e = new DaetaCancellationDialogFragment();
        return e;
    }

    //신청 취소 누르면 데이터 넘기기 위한 인터페이스
    public interface CancelInterfacer {
        void onCancellation(Daeta daeta, int position);
    }
    private CancelInterfacer cancelInterfacer;
    public void setCancelInterfacer(CancelInterfacer cancelInterfacer){
        this.cancelInterfacer= cancelInterfacer;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogfragment_daetacancelation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        TextView textView = (TextView) v.findViewById(R.id.daetaBranchName);
        Button closeButton = (Button) v.findViewById(R.id.application_closeBtn);
        closeButton.setOnClickListener(this);
        Button cancelButton = (Button) v.findViewById(R.id.application_cancellationBtn);
        cancelButton.setOnClickListener(this);

        textView.setText(daeta.getBranchName());

        setCancelable(false); //화면 터치 시 꺼짐을 막음

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.application_closeBtn){
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("DAETA_CANCELLATION_TAG");
            if (fragment!=null){
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }
        }
        else if (v.getId()==(R.id.application_cancellationBtn)){
            if (daeta!=null){
                cancelInterfacer.onCancellation(daeta, position);
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("DAETA_CANCELLATION_TAG");
                if (fragment!=null){
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismiss();
                }
            }
        }
    }
}