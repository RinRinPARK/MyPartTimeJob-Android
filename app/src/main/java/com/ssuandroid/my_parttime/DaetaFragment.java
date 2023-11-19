package com.ssuandroid.my_parttime;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DaetaFragment extends Fragment implements View.OnClickListener{

    //근무 목록을 띄울 recyclerView
    public RecyclerView recyclerViewWorkLog;
    //recyclerView가 필요로 하는 adapter
    public RecyclerView.Adapter adapter_workLog;
    //근무 목록을 가져올 리스트
    private ArrayList<String> workLogList;
    FirebaseFirestore db;
    Button workAddBtn;
    Button moveToDaetaCalendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alba_home, container, false);
        setHasOptionsMenu(true);

        //db 연결 전 테스트용
        ArrayList<String> workLog= new ArrayList<>();
        workLog.add("19,240");
        workLog.add("19,240");
        workLog.add("19,240");
        workLog.add("19,240");
        workLog.add("19,240");
        workLog.add("19,240");
        workLog.add("19,240");
        workLog.add("19,240");

        //recyclerView 사용을 위한 초기 작업
        recyclerViewWorkLog = (RecyclerView) view.findViewById(R.id.workLogRecyclerView);
        recyclerViewWorkLog.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter_workLog = new WorkLogAdapter(workLog); //임시로 만들어둔 데이터를 전옴
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewWorkLog.setLayoutManager(layoutManager);
        recyclerViewWorkLog.setAdapter(adapter_workLog);

        //버튼에 리스너 달아주기
        workAddBtn = (Button) view.findViewById(R.id.inputWorkedTimeBtn);
        moveToDaetaCalendar = (Button) view.findViewById(R.id.moveToDaetaCalendarBtn);
        workAddBtn.setOnClickListener(this);
        moveToDaetaCalendar.setOnClickListener(this);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.inputWorkedTimeBtn){
            WorkedTimeDialogFragment workedTimeDialogFragment = new WorkedTimeDialogFragment();
            workedTimeDialogFragment.show(getActivity().getSupportFragmentManager(), "tag"); //다시 돌아갈 부모 fragment 반드시 이름 맞추어 줘야 버튼 작동함!
        }
        else if (v.getId()==R.id.moveToDaetaCalendarBtn){
            //대타 캘린더로 이동하기
        }
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
//        return inflater.inflate(R.layout.fragment_alba_home, container, false);
//    }
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }


}