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

public class DaetaFragment extends Fragment{
    public RecyclerView recyclerViewDaetaList;
    public RecyclerView.Adapter adapter_daetaList;
    FirebaseFirestore db;
    ArrayList<Daeta> daetaArrayList = new ArrayList<>();

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
        db.collection("Daeta")
                .whereEqualTo("externalTF", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                daetaArrayList.add(document.toObject(Daeta.class));
                            }
                            Log.d("ymj", "대타 db에서 잘 받아옴");

                            ///어댑터 셋팅
                            adapter_daetaList = new DaetaListAdapter(daetaArrayList);
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


