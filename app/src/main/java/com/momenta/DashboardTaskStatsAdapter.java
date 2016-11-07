package com.momenta;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import me.grantland.widget.AutofitTextView;


public class DashboardTaskStatsAdapter extends RecyclerView.Adapter<DashboardTaskStatsAdapter.ViewHolder>{
    private List<Task> tasks;

    public DashboardTaskStatsAdapter(Context context, List<Task> tasks){
        this.tasks = tasks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the item layout
        View activity = inflater.inflate(R.layout.dashboard_task_stats_item, parent, false);


        // Return a new holder instance
        return new ViewHolder(activity);
    }

    @Override
    public void onBindViewHolder(DashboardTaskStatsAdapter.ViewHolder holder, int position) {
        //Get the tasks at index, position from the tasks list
        Task task = tasks.get(position);

        //Set the fields of the item_activity layout from the task object
        holder.taskName.setText(task.getName());

        Calendar taskLastMod = task.getLastModifiedValue();
        Calendar tempDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        String taskLastModString;

        //Display 'Today' string if task modified today
        if(Objects.equals(sdf.format(taskLastMod.getTime()), sdf.format(tempDate.getTime()))){
            taskLastModString = "Today";
        }
        else{
            tempDate.add(Calendar.DAY_OF_YEAR, -1);
            if(Objects.equals(sdf.format(taskLastMod.getTime()), sdf.format(tempDate.getTime()))){
                taskLastModString = "Yesterday";
            }
            else{
                sdf.applyPattern("MMM d, ''yy");
                taskLastModString = sdf.format(taskLastMod.getTime());
            }
        }
        holder.taskLastModified.setText(taskLastModString);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final AutofitTextView taskName;
        public final TextView taskLastModified;

        public ViewHolder(View itemView) {
            super(itemView);
            taskName = (AutofitTextView) itemView.findViewById(R.id.dash_task_stat_item_task_name);
            taskLastModified = (TextView) itemView.findViewById(R.id.dash_task_stat_item_task_value);
        }
    }
}