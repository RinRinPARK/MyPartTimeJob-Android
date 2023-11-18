package com.ssuandroid.my_parttime;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkLogAdapter extends RecyclerView.Adapter<WorkLogAdapter.WorkLogViewHolder> {
    private ArrayList<String> workLogList;
    WorkLogAdapter(ArrayList<String> List){
        Log.d("adapter"," worklogadapter working..");
        this.workLogList= List;
    }


    @NonNull
    @Override
    public WorkLogAdapter.WorkLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.worklogitem, parent,false);
        return new WorkLogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkLogAdapter.WorkLogViewHolder holder, int position) {
        holder.earnedMoney.setText(workLogList.get(position));
    }

    @Override
    public int getItemCount() {
        return workLogList.size();
    }

    public class WorkLogViewHolder extends RecyclerView.ViewHolder {
        private TextView albaDate;
        private TextView earnedMoney;
        private TextView workedTime;

        /////DB에서 데이터 가져와서 holder에 저장

        public WorkLogViewHolder(@NonNull View itemView){
            super(itemView);
            albaDate = (TextView)itemView.findViewById(R.id.workedDate);
            earnedMoney = (TextView)itemView.findViewById(R.id.earnedMoney);
            workedTime= (TextView)itemView.findViewById(R.id.workedTime);

        }
    }
}
