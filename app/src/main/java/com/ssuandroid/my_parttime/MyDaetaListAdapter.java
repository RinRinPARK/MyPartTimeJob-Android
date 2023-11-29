package com.ssuandroid.my_parttime;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class MyDaetaListAdapter extends RecyclerView.Adapter<MyDaetaListAdapter.MyDaetaListViewHolder> {
    private ArrayList<Daeta> daetaList;
    private OnCancelButtonListener onCancelButtonListener;
    MyDaetaListAdapter(ArrayList<Daeta> daetaList, OnCancelButtonListener onCancelButtonListener){
        this.daetaList = daetaList;
        this.onCancelButtonListener = onCancelButtonListener;
    }

    public interface OnCancelButtonListener {
        void onCancelClick(int position);
    }

    @NonNull
    @Override
    public MyDaetaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.mydaetaitem, parent, false);
        return new MyDaetaListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDaetaListViewHolder holder, int position) {
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
        Date date = daetaList.get(position).getDate();
        String dayWage= Integer.toString((int) workTotalTime);
        String workTime= daetaList.get(position).getTime();
        String hourlyRate= Long.toString(daetaList.get(position).getWage());
        String description = daetaList.get(position).getDescription();

        holder.branchName.setText(daetaList.get(position).getBranchName());
        holder.daetaDate.setText(date.toString());
        holder.dayWage.setText(dayWage);
        holder.daetaWorkTime.setText(workTime);
        holder.hourlyrate.setText(hourlyRate);
        holder.description.setText(description);

        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (onCancelButtonListener!=null){
                        onCancelButtonListener.onCancelClick(holder.getLayoutPosition());
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return daetaList.size();
    }



    public class MyDaetaListViewHolder extends RecyclerView.ViewHolder {
        private TextView branchName;
        private TextView daetaDate;
        private TextView dayWage;
        private TextView daetaWorkTime;
        private TextView hourlyrate;
        private TextView description;
        private Button cancelBtn;
        public MyDaetaListViewHolder(@NonNull View itemView){
            super(itemView);
            branchName= (TextView) itemView.findViewById(R.id.branchName);
            daetaDate = (TextView) itemView.findViewById(R.id.daetaDate);
            dayWage = (TextView) itemView.findViewById(R.id.dayWage);
            daetaWorkTime = (TextView) itemView.findViewById(R.id.daetaWorkTime);
            hourlyrate = (TextView) itemView.findViewById(R.id.daeta_wage);
            description =(TextView) itemView.findViewById(R.id.daeta_description);
            cancelBtn = (Button) itemView.findViewById(R.id.daeta_cancel_btn);
        }
    }
}