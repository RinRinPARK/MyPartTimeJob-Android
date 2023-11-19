package com.ssuandroid.my_parttime;

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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener{

    //branch의 목록을 띄울 recyclerView
    public RecyclerView recyclerView;
    //recyclerView가 필요로 하는 adapter
    public AlbaBranchAdapter adapter_albaBranch;
    //Alba를 가져올 리스트
    ArrayList<Alba> albaArrayList = new ArrayList<>(); //Alba Object를 db로부터 받아와서 리턴함.
    FirebaseFirestore db;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("ymj", "onCreateView_HomeFragment");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        init(view); //dialogfragment와의 데이터 주고받기를 위해 추가

        initializeCloudFirestore(); //db에 firestore instance 얻어옴
        getAlbaObject(); //db로부터 데이터를 얻어와 albaArrayList에 세팅

        //recyclerView 사용을 위한 초기 작업: RecyclerView는 LayoutManager와 Adapter(ViewHolder을 포함하는)을 필요로 한다.
        recyclerView = (RecyclerView) view.findViewById(R.id.albaListRecyclerView);
        recyclerView.setHasFixedSize(true); //recyclerview의 크기는 고정되어 있음.

        RelativeLayout newAlbaBtn = (RelativeLayout) view.findViewById(R.id.PlusButton);
        newAlbaBtn.setOnClickListener(this);

        Log.d("ymj", "list에 몇개 들어있는지: "+albaArrayList.size());

        setHasOptionsMenu(true);
        return view;
    }


//    public void init(View v){
//        if (v!=null){
//            Button newAlbaBtn = (Button) v.findViewById(R.id.PlusButton);
//            newAlbaBtn.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    Log.d("ymj", "클릭 발생");
//                    CodeInputDialogFragment codeInputDialogFragment = CodeInputDialogFragment.getInstance();
//                    codeInputDialogFragment.setFragmentInterfacer(new CodeInputDialogFragment.MyFragmentInterfacer() {
//                        @Override
//                        public void onButtonClick(String input) {
//                            Toast.makeText(getContext(), "test."+input, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            });
//        }
//    }

    private void initializeCloudFirestore() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance(); //firestore의 인스턴스를 db로 얻음
        Log.d("ymj", "db instance 얻음");
    }

    private void getAlbaObject(){
        db.collection("Alba")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                albaArrayList.add(document.toObject(Alba.class));
                                Log.d("ymj", "list에 added");
                            }

                            //어댑터 초기화 및 설정을 이 블록 내부로 이동시켜서 데이터가 받아져야만 recyclerView가 구성되도록 만듦
                            adapter_albaBranch = new AlbaBranchAdapter(albaArrayList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(adapter_albaBranch);}
                            else {
                                Log.d("ymj", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.PlusButton){
            Log.d("ymj", "here");
            CodeInputDialogFragment codeInputDialogFragment = new CodeInputDialogFragment();
            codeInputDialogFragment.show(getActivity().getSupportFragmentManager(),"HOME_TAG");
        }
    }
}