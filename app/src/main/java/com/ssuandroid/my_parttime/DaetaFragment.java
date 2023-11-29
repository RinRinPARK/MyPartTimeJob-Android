package com.ssuandroid.my_parttime;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DaetaFragment extends Fragment implements View.OnClickListener, DaetaDescriptionDialogFragment.DaetaInterfacer1, DaetaApplicationDialogFragment.DaetaInterfacer2 {
    public RecyclerView recyclerViewDaetaList;
    public RecyclerView.Adapter adapter_daetaList;
    FirebaseFirestore db;
    ArrayList<Daeta> daetaArrayList = new ArrayList<>();
    ArrayList<Branch> branchArrayList = new ArrayList<>(); //daeta branchname 띄우기 위해서 필요함
    FirebaseUser user;
    ImageButton daetaListBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_daeta, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeCloudFirestore();

        getDaetaObject(); //db에서 대타 공고 가져옴

        recyclerViewDaetaList = (RecyclerView) view.findViewById(R.id.daetaListRecyclerView);
        recyclerViewDaetaList.setHasFixedSize(true);
        daetaListBtn = (ImageButton) view.findViewById(R.id.daetaList_btn);
        daetaListBtn.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
        }


    }

    private void initializeCloudFirestore() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance(); //firestore의 인스턴스를 db로 얻음
    }

    private void getDaetaObject(){
        db.collection("Branch")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        branchArrayList.add(document.toObject(Branch.class));
                                    }
                                    //branchArrayList를 채우고 난 뒤, 대타 데이터도 받아옴
                                    db.collection("Daeta")
                                            .whereEqualTo("externalTF", true)
                                            .whereEqualTo("covered", false)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()){
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            daetaArrayList.add(document.toObject(Daeta.class));
                                                        }

                                                        ///어댑터 셋팅 : 2개의 ArrayList를 전해준다
                                                        adapter_daetaList = new DaetaListAdapter(branchArrayList, daetaArrayList, buttonClickListener);
                                                        recyclerViewDaetaList.setLayoutManager(new LinearLayoutManager((getActivity())));
                                                        recyclerViewDaetaList.setAdapter(adapter_daetaList);
                                                    }
                                                    else {
                                                        Log.d("ymj", "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });
                                }
                            }
                        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.daetaList_btn){
            MyDaetaApplicationListFragment myDaetaApplicationListFragment = new MyDaetaApplicationListFragment();

            getParentFragmentManager()
                    .beginTransaction().replace(R.id.main_container, myDaetaApplicationListFragment).commit();
        }
    }

    DaetaListAdapter.OnButtonClickListener buttonClickListener = new DaetaListAdapter.OnButtonClickListener() {
        @Override
        public void onButtonClick(int position, String buttonPos) {
            if (buttonPos.equals("descriptionBtn")) {
                callDescriptionDialog(position);
            } else if (buttonPos.equals("applicationBtn"))
                callApplicationDialog(position);
        }
    };

    public void callDescriptionDialog(int position){
        Daeta daeta = daetaArrayList.get(position); //대타 객체를 통째로 dialogfragment에게 보낸다
        DaetaDescriptionDialogFragment daetaDescriptionDialogFragment = new DaetaDescriptionDialogFragment(daeta, position);
        daetaDescriptionDialogFragment.setDaetaInterfacer1(this); //인터페이스로 나를 건넨다....
        daetaDescriptionDialogFragment.show(getActivity().getSupportFragmentManager(), "DAETA_DESCRIPTION_TAG");
    }

    public void callApplicationDialog(int position){
        Daeta daeta = daetaArrayList.get(position); //대타 객체를 통째로 dialogfragment에게 보낸다
        DaetaApplicationDialogFragment daetaApplicationDialogFragment = new DaetaApplicationDialogFragment(daeta, position);
        daetaApplicationDialogFragment.setDaetaInterfacer2(this);
        daetaApplicationDialogFragment.show(getActivity().getSupportFragmentManager(), "DAETA_APPLICATION_TAG");
    }

        @Override
        public void onDescriptionApplication(Daeta daeta, int position) {
            daeta.setCovered();
            daeta.setApplicantId(user.getUid());
            db.collection("Daeta").document(daeta.getParticipationCode()+" "+daeta.getDate()+" "+daeta.getTime()).set(daeta);
            //covered field를 바꾼 객체를 넣어주어 목록에서 사라지게 한다.

            //신청이 완료되면 토스트를 띄운다
            Toast toast = Toast.makeText(getContext(), "외부 대타 신청이 완료되었어요!", Toast.LENGTH_SHORT);
            toast.show();

        }

        @Override
        public void onApplication(Daeta daeta, int position) {
            daeta.setCovered(); //covered를 true로 바꾼다
            daeta.setApplicantId(user.getUid());
            db.collection("Daeta").document(daeta.getParticipationCode()+" "+daeta.getDate()+" "+daeta.getTime()).set(daeta);

            //신청이 완료되면 토스트를 띄운다
            Toast toast = Toast.makeText(getContext(), "외부 대타 신청이 완료되었어요!", Toast.LENGTH_SHORT);
            toast.show();
    };
}


