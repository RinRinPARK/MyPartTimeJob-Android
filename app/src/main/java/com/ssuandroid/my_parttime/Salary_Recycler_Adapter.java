package com.ssuandroid.my_parttime;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Salary_Recycler_Adapter extends RecyclerView.Adapter<Salary_Recycler_Adapter.SalaryViewHolder> {
    Map<String, Integer> yearMonthWageMap = new HashMap<>();
    ArrayList<String> keySet= new ArrayList<>();

    Salary_Recycler_Adapter(ArrayList<String> keySet, Map<String,Integer> yearMonthWageMap){
        this.keySet= keySet;
        this.yearMonthWageMap=yearMonthWageMap;
        Log.d("ymj", "잘 받음");
    }

    @NonNull
    @Override
    public SalaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.salaryitem,parent,false);
        return new SalaryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SalaryViewHolder holder, int position) {
        holder.salary.setText(yearMonthWageMap.get(keySet.get(position)).toString());

        String yearMonth = keySet.get(position).toString();
        String year= yearMonth.substring(0,4);
        String month="0";
        if (yearMonth.length()==7){
            month = yearMonth.substring(5,7);
        }
        else if (yearMonth.length()==6){
            month = yearMonth.substring(5,6);
        }
        holder.monthYear.setText(year+"년 "+month+"월");
    }

    @Override
    public int getItemCount() {
        return yearMonthWageMap.size();
    }

    public class SalaryViewHolder extends RecyclerView.ViewHolder {
        private TextView monthYear;
        private TextView salary;

        public SalaryViewHolder(@NonNull View itemView){
            super(itemView);
            monthYear = (TextView) itemView.findViewById(R.id.monthYear);
            salary = (TextView) itemView.findViewById(R.id.salary);
        }
    }
}
