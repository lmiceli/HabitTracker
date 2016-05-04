package com.lmiceli.habittracker.controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lmiceli.habittracker.R;

/**
 * Created by lmiceli on 19/04/2016.
 */
public class HabitViewHolder extends RecyclerView.ViewHolder{

    private TextView desc;
    private TextView done;

    public HabitViewHolder(View view) {
        super(view);

        this.desc = (TextView) view.findViewById(R.id.habit_desc);
        this.done = (TextView) view.findViewById(R.id.habit_done);

    }

    public TextView getDesc() {
        return desc;
    }

    public void setDesc(TextView desc) {
        this.desc = desc;
    }

    public TextView getDone() {
        return done;
    }

    public void setDone(TextView done) {
        this.done = done;
    }
}
