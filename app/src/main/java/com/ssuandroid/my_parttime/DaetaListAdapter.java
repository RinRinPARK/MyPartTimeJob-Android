package com.ssuandroid.my_parttime;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DaetaListAdapter extends RecyclerView.Adapter<DaetaListAdapter.DaetaListViewHolder> {
    private ArrayList<Daeta> daetaList;
    DaetaListAdapter(ArrayList<Daeta> List){
        Log.d("ymj", "리스트 잘 받음요");
        this.daetaList = List;
    }

    @NonNull
    @Override
    public DaetaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.alldaetaitem, parent, false);
        return new DaetaListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DaetaListViewHolder holder, int position) {
        String branchName= daetaList.get(position).getParticipationCode(); //branchName participationcode로 검색해서 얻어와야함
        String dayWage; //일급 계산해서 넣어줘야 함
        String workTime;
        String hourlyRate= Long.toString(daetaList.get(position).getWage());

        holder.branchName.setText(branchName);

        ////수정 필요
        holder.dayWage.setText("40,000원");
        holder.daetaWorkTime.setText("08:00~15:00");
        ////수정필요

        holder.hourlyrate.setText(hourlyRate);

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
        public DaetaListViewHolder(@NonNull View itemView){
            super(itemView);
            branchName= (TextView) itemView.findViewById(R.id.branchName);
            dayWage = (TextView) itemView.findViewById(R.id.dayWage);
            daetaWorkTime = (TextView) itemView.findViewById(R.id.daetaWorkTime);
            hourlyrate = (TextView) itemView.findViewById(R.id.daeta_wage);

        }
    }
}
