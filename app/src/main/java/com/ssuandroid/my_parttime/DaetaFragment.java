package com.ssuandroid.my_parttime;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DaetaFragment extends Fragment implements DaetaDescriptionDialogFragment.DaetaInterfacer1, DaetaApplicationDialogFragment.DaetaInterfacer2 {
    public RecyclerView recyclerViewDaetaList;
    public RecyclerView.Adapter adapter_daetaList;
    FirebaseFirestore db;
    ArrayList<Daeta> daetaArrayList = new ArrayList<>();
    ArrayList<Branch> branchArrayList = new ArrayList<>(); //daeta branchname 띄우기 위해서 필요함

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
                                                        Log.d("ymj", "대타 db에서 잘 받아옴");

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
    DaetaListAdapter.OnButtonClickListener buttonClickListener = new DaetaListAdapter.OnButtonClickListener() {
        @Override
        public void onButtonClick(int position, String buttonPos) {
            if (buttonPos.equals("descriptionBtn")) {
                Log.d("ymj", "버튼 눌림 in fragment");
                callDescriptionDialog(position);
            } else if (buttonPos.equals("applicationBtn"))
                callApplicationDialog(position);
        }
    };

    public void callDescriptionDialog(int position){
        Daeta daeta = daetaArrayList.get(position); //대타 객체를 통째로 dialogfragment에게 보낸다
        Log.d("ymj", "here");
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
            db.collection("Daeta").document(Integer.toString(position)).set(daeta); //위에선 map을 올렸는데, 직접 만든 객체 형식으로도 넣을 수 있음을 보임.
        }

        @Override
        public void onApplication(Daeta daeta, int position) {
//                    db.collection("Daeta").document(daetaArrayList.get(position)).set(daeta)); //위에선 map을 올렸는데, 직접 만든 객체 형식으로도 넣을 수 있음을 보임.
    };
}


