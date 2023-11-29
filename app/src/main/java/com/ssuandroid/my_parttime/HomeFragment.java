package com.ssuandroid.my_parttime;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener, CodeInputDialogFragment.MyFragmentInterfacer, HourlyRateDialogFragment.FragmentInterfacer{

    //branch의 목록을 띄울 recyclerView
    public RecyclerView recyclerView;
    //recyclerView가 필요로 하는 adapter
    public AlbaBranchAdapter adapter_albaBranch;
    //Alba를 가져올 리스트
    ArrayList<Alba> albaArrayList = new ArrayList<>(); //Alba Object를 db로부터 받아와서 리턴함.
    FirebaseFirestore db;

    String newAlbaCode;
    long newAlbaWage;
    String branchName;
    FirebaseUser user;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeCloudFirestore(); //db에 firestore instance 얻어옴
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            Log.d("ymj", uid+" id 출력");
        }
        getAlbaObject(); //db로부터 데이터를 얻어와 albaArrayList에 세팅

        //recyclerView 사용을 위한 초기 작업: RecyclerView는 LayoutManager와 Adapter(ViewHolder을 포함하는)을 필요로 한다.
        recyclerView = (RecyclerView) view.findViewById(R.id.albaListRecyclerView);
        recyclerView.setHasFixedSize(true); //recyclerview의 크기는 고정되어 있음.

        RelativeLayout newAlbaBtn = (RelativeLayout) view.findViewById(R.id.PlusButton);
        newAlbaBtn.setOnClickListener(this);

        setHasOptionsMenu(true);
    }


    private void initializeCloudFirestore() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance(); //firestore의 인스턴스를 db로 얻음
    }

    private void getAlbaObject(){
        db.collection("Alba")
                .whereEqualTo("userId", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                albaArrayList.add(document.toObject(Alba.class));
                                updateUIVisibility(); //새로운 알바 추가! 문구 갱신을 위함
                            }

                            //어댑터 초기화 및 설정을 이 블록 내부로 이동시켜서 데이터가 받아져야만 recyclerView가 구성되도록 만듦
                            adapter_albaBranch = new AlbaBranchAdapter(albaArrayList, (MainActivity) getActivity());
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(adapter_albaBranch);}
                            else {
                                Log.d("ymj", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.PlusButton) {
            CodeInputDialogFragment codeInputDialogFragment = CodeInputDialogFragment.getInstance();
            codeInputDialogFragment.setFragmentInterfacer(this); // HomeFragment에서 인터페이스 구현

            codeInputDialogFragment.show(getParentFragmentManager(), "CODE_TAG");
        }
    }

    // MyFragmentInterfacer 구현: 알바 코드 dialog를 띄우고 데이터를 받기 위한 인터페이스. 코드 데이터를 받으면
    // 시급 dialog 또한 homefragment에서 띄운다
    @Override
    public void onButtonClick(String input) {
        newAlbaCode= input;

        db.collection("Branch")
                .whereEqualTo("participationCode", newAlbaCode)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            QuerySnapshot doc = task.getResult();
                                if(doc.isEmpty()){
                                    Log.d("ymj", "코드가 존재하지 않음");
                                    //코드가 존재하지 않음 -> 다음 다이얼로그프래그먼트를 띄우지 않고 다이얼로그프래그먼트를 닫는다
                                    CodeInputDialogFragment dialogFragment = (CodeInputDialogFragment) getFragmentManager().findFragmentByTag("CODE_TAG");
                                    if (dialogFragment != null) {
                                        dialogFragment.dismiss();
                                    }
                                    Toast toast = Toast.makeText(getContext(), "해당 알바 브랜치가 존재하지 않습니다.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                else {
                                    Log.d("ymj", "올바른 코드 입력");
                                    HourlyRateDialogFragment hourlyRateDialogFragment = new HourlyRateDialogFragment();
                                    hourlyRateDialogFragment.setFragmentInterfacer(HomeFragment.this); // HomeFragment에서 인터페이스 구현

                                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                    hourlyRateDialogFragment.show(transaction, "WAGE_TAG");
                            }
                        }
                    }
                });
        // 두 번째 다이얼로그 띄우기
    }

    // FragmentInterfacer 구현: 시급 dialog로부터 데이터를 받기 위한 인터페이스
    @Override
    public void onWageButtonClick(long input) {
        newAlbaWage = input;
        // HomeFragment에서 두 번째 다이얼로그에서 받은 데이터 처리

        //두번째 다이얼로그로부터 데이터를 전부 잘 받았으면 새로운 Alba 객체를 생성
        newAlbaObject();
    }

    public void newAlbaObject(){

        //임시 userId
        String userId= user.getUid();

        //얻은 participationCode를 통해 branchName을 알아낸다
        db.collection("Branch")
                    .whereEqualTo("participationCode", newAlbaCode)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("ymj", document.getId() + " => " + document.getData());
                                    String fieldData = document.getString("branchName");
                                    //여기서 바로 변수에 할당하려고 하면 exception 남.

                                    branchName = fieldData;

                                    //Firebase의 데이터는 비동기적으로 처리된다.
                                    //즉, 데이터를 받아 오는 쿼리는 시간이 지연될 수 있다.
                                    //무조건 branchName 데이터를 다 받아오고 나서 새로운 Alba 객체를 생성해야
                                    //branchName에 null이 들어간 상태로 새로운 객체가 생성되는 일을 막을 수 있다.
                                    Alba alba= new Alba(userId, branchName, newAlbaCode, newAlbaWage);
                                    db.collection("Alba").document(alba.getBranchName()).set(alba);
                                }
                            } else {
                                Log.d("ymj", "Error getting documents: ", task.getException());
                            }
                        }
                    });
    }

    //알바가 하나도 없을 때를 위한 새로운 알바 추가 문구 설정
    private void updateUIVisibility() {
        View view = getView();
        TextView textView = (TextView) view.findViewById(R.id.noAlbaState);
        if (albaArrayList.size() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUIVisibility();
    }

}