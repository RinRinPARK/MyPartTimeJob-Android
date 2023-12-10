package com.ssuandroid.my_parttime;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyDaetaApplicationListFragment extends Fragment implements DaetaCancellationDialogFragment.CancelInterfacer {
    FirebaseFirestore db;
    Context mContext;
    FirebaseUser user;
    String uid;
    ArrayList<Daeta> daetaArrayList = new ArrayList<>();
    public RecyclerView recyclerViewMyDaetaList;
    public RecyclerView.Adapter adapter_myDaetaList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public MyDaetaApplicationListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_daeta_application_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeCloudFirestore();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

        getDaetaObject(); //db에서 대타 공고 가져옴

        recyclerViewMyDaetaList = view.findViewById(R.id.myDaetaList);

        ImageButton btn = view.findViewById(R.id.back_to_daeta_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ymj", "clicked");
                DaetaFragment daetaFragment = new DaetaFragment();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.main_container, daetaFragment)
                        .commit();
            }
        });
    }

    private void initializeCloudFirestore() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance(); //firestore의 인스턴스를 db로 얻음
    }

    public void getDaetaObject(){
        db.collection("Daeta")
                .whereEqualTo("applicantId", uid)
                .whereEqualTo("externalTF", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                daetaArrayList.add(document.toObject(Daeta.class));
                                Log.d("ymj", document.getId());
                            }
                        }

                        ///어댑터 셋팅 : 2개의 ArrayList를 전해준다
                        adapter_myDaetaList = new MyDaetaListAdapter(daetaArrayList, onCancelButtonListener);
                        recyclerViewMyDaetaList.setLayoutManager(new LinearLayoutManager((getActivity())));
                        recyclerViewMyDaetaList.setAdapter(adapter_myDaetaList);

                    }
                });
    }

    MyDaetaListAdapter.OnCancelButtonListener onCancelButtonListener = new MyDaetaListAdapter.OnCancelButtonListener(){
        @Override
        public void onCancelClick(int position) {
            Log.d("ymj", "cancel clicked"+position+"번째 눌림");
            cancelDaeta(position);
        }
    };

    public void cancelDaeta(int position){
        Daeta daeta = daetaArrayList.get(position);
        DaetaCancellationDialogFragment daetaCancellationDialogFragment = new DaetaCancellationDialogFragment(daeta, position);
        daetaCancellationDialogFragment.setCancelInterfacer(this); //인터페이스로 나를 건넨다
        daetaCancellationDialogFragment.show(getActivity().getSupportFragmentManager(), "DAETA_CANCELLATION_TAG");
    }

    @Override
    public void onCancellation(Daeta daeta, int position) {
//        신청 취소시 다시 work에서 삭제
        db.collection("Work").document(daeta.getApplicantId()+" "+daeta.getParticipationCode()+" "+daeta.getDate()).delete();
        Log.d("ymj", daeta.getApplicantId()+" "+daeta.getParticipationCode()+" "+daeta.getDate());

        //daeta 객체도 수정
        daeta.cancelSetApplicantId();
        daeta.cancelSetCovered();
        db.collection("Daeta").document(daeta.getParticipationCode()+" "+daeta.getDate()+" "+daeta.getTime()).set(daeta);

        ToastCustom toastCustom = new ToastCustom(mContext);
        toastCustom.showToast("외부 대타 취소가 완료되었어요!");
    }
}