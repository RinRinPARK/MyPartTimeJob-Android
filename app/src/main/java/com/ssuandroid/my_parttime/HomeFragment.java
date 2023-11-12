package com.ssuandroid.my_parttime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

    // alertDialog 변수를 클래스 레벨로 선언
    private AlertDialog alertDialog;
    private AlertDialog hourlyRateDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button plusButton = view.findViewById(R.id.PlusButton); //homefragment에서 알바 추가 버튼

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // LayoutInflater를 사용하여 커스텀 다이얼로그 레이아웃을 가져옵니다.
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialogfragment_codeinput, null);
                View hourlyRateDialogView = inflater.inflate(R.layout.dialogfragment_hourlyrate, null);

                // AlertDialog를 생성하고 설정합니다.
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireActivity());
                alertDialogBuilder.setView(dialogView);

                //hourlyRateDialog를 생성하고 설정합니다.
                AlertDialog.Builder hourlyRateDialogBuilder = new AlertDialog.Builder(requireActivity());
                hourlyRateDialogBuilder.setView(dialogView);

                // 다이얼로그를 생성합니다.
                alertDialog = alertDialogBuilder.create();
                hourlyRateDialog = hourlyRateDialogBuilder.create();

                // 다이얼로그 안의 버튼을 찾습니다.
                Button btnConfirm = dialogView.findViewById(R.id.codeinputButton);
                Button btnCancel = dialogView.findViewById(R.id.codecloseButton);

                // '입력' 버튼 클릭 시의 동작
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hourlyRateDialog.dismiss();
                        hourlyRateDialog.show();
                    }
                });

                // '취소' 버튼 클릭 시의 동작
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 취소 버튼을 눌렀을 때의 동작
                        alertDialog.dismiss(); // 다이얼로그 닫기
                        alertDialog = null; // 다이얼로그 변수를 null로 설정
                    }
                });

                // 다이얼로그를 보여줍니다.
                alertDialog.show();
            }
        });
    }
}