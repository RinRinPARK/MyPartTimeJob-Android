package com.ssuandroid.my_parttime;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WorkLogAdapter extends RecyclerView.Adapter<WorkLogAdapter.WorkLogViewHolder> {
    private ArrayList<Work> workLogList;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
    WorkLogAdapter(ArrayList<Work> List){
        Log.d("adapter"," worklogadapter working..");
        this.workLogList= List;
    }


    @NonNull
    @Override
    public WorkLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.worklogitem, parent,false);
        return new WorkLogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkLogViewHolder holder, int position) {
        Date date = workLogList.get(position).getDate();
        String formattedDate = sdf.format(date);

        holder.albaDate.setText(formattedDate);
        holder.earnedMoney.setText( Integer.toString ((int)(workLogList.get(position).getWage() * workLogList.get(position).getWorkTime())) );
        holder.workedTime.setText( (Double.toString(workLogList.get(position).getWorkTime())) + "시간");
    }

    @Override
    public int getItemCount() {
        return workLogList.size();
    }

    public class WorkLogViewHolder extends RecyclerView.ViewHolder {
        private TextView albaDate;
        private TextView earnedMoney;
        private TextView workedTime;


        public WorkLogViewHolder(@NonNull View itemView){
            super(itemView);
            albaDate = (TextView)itemView.findViewById(R.id.workedDate);
            earnedMoney = (TextView)itemView.findViewById(R.id.earnedMoney);
            workedTime= (TextView)itemView.findViewById(R.id.workedTime);

            //item 누르면 수정 가능하도록?
//            itemView.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }
    }
}
