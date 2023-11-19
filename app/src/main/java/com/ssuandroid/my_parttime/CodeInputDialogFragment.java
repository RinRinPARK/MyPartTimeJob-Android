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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CodeInputDialogFragment extends DialogFragment implements View.OnClickListener{

    private FirebaseFirestore db;

    public CodeInputDialogFragment() {}//생성자
    public static CodeInputDialogFragment getInstance() {
        CodeInputDialogFragment e = new CodeInputDialogFragment();
        return e;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.dialogfragment_codeinput, container);

        initializeCloudFirestore();

        Button closeButton = (Button) v.findViewById(R.id.codecloseButton);
        closeButton.setOnClickListener(this);

        Button inputButton = (Button) v.findViewById(R.id.codeinputButton);
        inputButton.setOnClickListener(this);

        return v;
    }

    private void initializeCloudFirestore(){
        db = FirebaseFirestore.getInstance();
        Log.d("ymj", "db 할당됨");
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
//                CollectionReference branchCode = db.collection("Alba"); //Alba collection에 넣을 거임

//                Log.d("ymj", "알맞게 Alba data instance 를 받아옴");

//                Alba alba = new Alba(, " ", " ", ); //Alba 객체를 얻음
//                Log.d("ymj", alba.getBranchName()+" "+alba.getParticipationCode()+" "+alba.getUserId()+" "+alba.getWage());

//                db.collection("Alba").document(alba.getParticipationCode()).set(alba); //일단 알바 코드를 제목으로 하여 올림

                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();

                HourlyRateDialogFragment hourlyRateDialogFragment= new HourlyRateDialogFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                hourlyRateDialogFragment.show(transaction, "HOME_TAG");
            }
        }
    }


}
