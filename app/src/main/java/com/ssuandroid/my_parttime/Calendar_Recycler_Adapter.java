package com.ssuandroid.my_parttime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Calendar_Recycler_Adapter extends RecyclerView.Adapter<Calendar_Recycler_Adapter.viewHolder> {

    Context context;
    String[] data;

    public Calendar_Recycler_Adapter(Context context, String[] data) {
        super();
        this.context = context;
        this.data = data;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView;

        public viewHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.day_recycler_calendar);
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_calendar, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.dayTextView.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}