package com.ssuandroid.my_parttime;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DaetaListAdapter extends RecyclerView.Adapter<DaetaListAdapter.DaetaListViewHolder> {
    private ArrayList<Daeta> daetaList;
    private ArrayList<Branch> branchList;
    private OnButtonClickListener buttonClickListener;
    DaetaListAdapter(ArrayList<Branch> branchList, ArrayList<Daeta> daetaList, OnButtonClickListener buttonClickListener){
        this.branchList = branchList;
        this.daetaList = daetaList;
        this.buttonClickListener = buttonClickListener;
    }

    //Daetafragment와의 통신을 위한 인터페이스 정의
    public interface OnButtonClickListener {
        void onButtonClick(int position, String buttonPos);
    }

    @NonNull
    @Override
    public DaetaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.alldaetaitem, parent, false);
        return new DaetaListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DaetaListViewHolder holder, int position) {
        //branch이름 검색해서 알맞게 띄우기
        String branchName;
        for (Branch branch : branchList) {
            if (branch.getParticipationCode().equals(daetaList.get(position).getParticipationCode())) {
                branchName = branch.getbranchName();
                daetaList.get(position).setBranchName(branchName);
                holder.branchName.setText(branchName);
                break;  // 원하는 객체를 찾았으면 반복문 종료
            }
        }

        //workTime (실제 일하는 시간, 예를 들어 3시간, 2.5시간 등.. ->이걸로 "일당"을 계산
        String timeRange = daetaList.get(position).getTime();
        String[] parts = timeRange.split(" - ");

        // 첫 번째 시간대 13:30
        String startHour = parts[0].split(":")[0]; //13
        String startMin = parts[0].split(":")[1]; //30

        // 두 번째 시간대에서 15:00
        String endHour = parts[1].split(":")[0]; //15
        String endMin = parts[1].split(":")[1]; //00

        double workHour = Double.parseDouble(endHour)-Double.parseDouble(startHour); //2
        double workMin = Double.parseDouble(endMin)-Double.parseDouble(startMin); //-30 또는 0 또는 30

        double workTotalTime = (workHour + workMin/60)*daetaList.get(position).getWage();
        if (workTotalTime<0) workTotalTime=-workTotalTime; //음수 가능성 존재
        String dayWage= Integer.toString((int) workTotalTime);
        String workTime= daetaList.get(position).getTime();
        String hourlyRate= Long.toString(daetaList.get(position).getWage());

        holder.dayWage.setText(dayWage);
        holder.daetaWorkTime.setText(workTime);
        holder.hourlyrate.setText(hourlyRate);

        holder.descriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener!=null){
                    buttonClickListener.onButtonClick(holder.getLayoutPosition(), "descriptionBtn");
                    //인터페이스의 함수 호출
                }
            }
        });

        holder.applicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener!=null){
                    buttonClickListener.onButtonClick(holder.getLayoutPosition(), "applicationBtn");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return daetaList.size();
    }



    public class DaetaListViewHolder extends RecyclerView.ViewHolder {
        private TextView branchName;
        private TextView dayWage;
        private TextView daetaWorkTime;
        private TextView hourlyrate;
        private Button descriptionButton;
        private Button applicationButton;
        public DaetaListViewHolder(@NonNull View itemView){
            super(itemView);
            branchName= (TextView) itemView.findViewById(R.id.branchName);
            dayWage = (TextView) itemView.findViewById(R.id.dayWage);
            daetaWorkTime = (TextView) itemView.findViewById(R.id.daetaWorkTime);
            hourlyrate = (TextView) itemView.findViewById(R.id.daeta_wage);
            descriptionButton = (Button) itemView.findViewById(R.id.daeta_description_btn);
            applicationButton = (Button) itemView.findViewById(R.id.daeta_application_btn);
        }
    }
}
