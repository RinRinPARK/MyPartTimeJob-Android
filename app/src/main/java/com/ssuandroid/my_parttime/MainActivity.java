package com.ssuandroid.my_parttime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ssuandroid.my_parttime.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity{
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment;
    CalendarFragment calendarFragment;
    DaetaFragment daetaFragment;
    ActivityMainBinding binding;
    int albaPos;
    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        //초기 셋팅 함수
        init();

        //NavigationView의 특정 아이템이 선택되었을 때 fragment가 변한다
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.fragment_home){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, new HomeFragment())
                            .commit();
                    return true;
                }
                else if (item.getItemId()==R.id.fragment_calendar){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, new CalendarFragment())
                            .commit();
                    return true;
                }
                else if (item.getItemId()==R.id.fragment_daeta){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, new DaetaFragment())
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }

    //초기 셋팅
    private void init(){
        db = FirebaseFirestore.getInstance();

        //fragment 객체 생성하여 할당
        homeFragment= new HomeFragment();
        calendarFragment= new CalendarFragment();
        daetaFragment = new DaetaFragment();

        //main.xml상의 bottomNavigationView 연결
        bottomNavigationView= binding.bottomNavigationView;

        //가장 처음에 띄울 뷰 셋팅
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.main_container, homeFragment).commitAllowingStateLoss();
    }


    //recyclerview 터치시 fragment 변경
    public void fragmentChange(Alba selectedAlba){
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedAlba",selectedAlba); //선택된 Alba item을 통째로 건넨다

        AlbaHomeFragment albaHomeFragment = new AlbaHomeFragment();
        albaHomeFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, albaHomeFragment)
                .addToBackStack( null)
                .commit();

    }

}

