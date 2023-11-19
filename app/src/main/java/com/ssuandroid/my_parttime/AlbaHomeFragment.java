package com.ssuandroid.my_parttime;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class AlbaHomeFragment extends Fragment implements View.OnClickListener{
    //근무 목록을 띄울 recyclerView
    public RecyclerView recyclerViewWorkLog;
    //recyclerView가 필요로 하는 adapter
    public RecyclerView.Adapter adapter_workLog;
    //근무 목록을 가져올 리스트
    ArrayList<Work> workArrayList = new ArrayList<>();
    FirebaseFirestore db;
    Button workAddBtn;
    Button moveToDaetaCalendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alba_home, container, false);
        setHasOptionsMenu(true);

        initializeCloudFirestore(); //db에 firestore instance 얻어옴
        getWorkObject(); //db로부터 데이터를 얻어와 albaArrayList에 세팅

        //recyclerView 사용을 위한 초기 작업
        recyclerViewWorkLog = (RecyclerView) view.findViewById(R.id.workLogRecyclerView);
        recyclerViewWorkLog.setHasFixedSize(true);

        //버튼에 리스너 달아주기
        workAddBtn = (Button) view.findViewById(R.id.inputWorkedTimeBtn);
        moveToDaetaCalendar = (Button) view.findViewById(R.id.moveToDaetaCalendarBtn);
        workAddBtn.setOnClickListener(this);
        moveToDaetaCalendar.setOnClickListener(this);

        return view;
    }

    private void initializeCloudFirestore(){
        db = FirebaseFirestore.getInstance(); //firestore의 인스턴스를 db로 얻음
        Log.d("ymj", "db instance 얻음");
    }

    private void getWorkObject(){
        db.collection("Work")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                workArrayList.add(document.toObject(Work.class));
                            }

                            adapter_workLog = new WorkLogAdapter(workArrayList);
                            recyclerViewWorkLog.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerViewWorkLog.setAdapter(adapter_workLog);
                        }
                        else {
                            Log.d("ymj", "Error getting documents: ", task.getException());
                        }
                    }
                });}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.inputWorkedTimeBtn){
            WorkedTimeDialogFragment workedTimeDialogFragment = new WorkedTimeDialogFragment();
            workedTimeDialogFragment.show(getActivity().getSupportFragmentManager(), "TAG");
        }
        else if (v.getId()==R.id.moveToDaetaCalendarBtn){
            //대타 캘린더로 이동하기
        }
    }
}