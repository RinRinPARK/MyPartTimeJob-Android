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

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener{
    //branch의 목록을 띄울 recyclerView
    public RecyclerView recyclerView;
    //recyclerView가 필요로 하는 adapter
    public RecyclerView.Adapter adapter_albaBranch;
    //Alba를 가져올 리스트
    private ArrayList<String> branchList;
    FirebaseFirestore db;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("ymj", "onCreateView_HomeFragment");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //db 연결 전 테스트용
        ArrayList<String> branchNameList = new ArrayList<>();
        branchNameList.add("파리바게뜨");
        branchNameList.add("GS25");
        branchNameList.add("스타벅스");
        branchNameList.add("체조경기장");
        branchNameList.add("NCT DREAM");

        //recyclerView 사용을 위한 초기 작업: RecyclerView는 LayoutManager와 Adapter(ViewHolder을 포함하는)을 필요로 한다.
        recyclerView = (RecyclerView) view.findViewById(R.id.albaListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter_albaBranch = new AlbaBranchAdapter(branchNameList); //임시로 만들어둔 데이터 전달, 실제로는 db에서 꺼내옴
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter_albaBranch);

        Button newAlbaBtn = (Button) view.findViewById(R.id.PlusButton);
        newAlbaBtn.setOnClickListener(this);
        setHasOptionsMenu(true);
        return view;
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