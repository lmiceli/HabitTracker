package com.lmiceli.habittracker.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lmiceli.habittracker.R;
import com.lmiceli.habittracker.controller.HabitViewHolder;
import com.lmiceli.habittracker.model.DatabaseHelper;
import com.lmiceli.habittracker.model.Habit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lmiceli on 19/04/2016.
 */
public class HabitViewAdapter extends RecyclerView.Adapter<HabitViewHolder>{

    private static final String TAG = "HabitViewAdapter";
    private final List<Habit> habits;
    private final Context context;

    public HabitViewAdapter(Context context) {
        Log.d(TAG, "HabitViewAdapter: creating adapter");
        this.habits = new ArrayList<>();
//        createFakeHabit();
//        createFakeHabit();
//        createFakeHabit();
//        createFakeHabit();
        // TODO add tests?
        this.context = context;

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);


        this.habits.addAll(dbHelper.getAllHabits());




    }

    private void createFakeHabit() {
        Habit habit = new Habit();
        habit.setDesc("Mega Fake");
        habits.add(habit);
    }

    @Override
    public HabitViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.habit, null);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HabitViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: binding data to holder");

        Habit habit = habits.get(position);
        holder.getDesc().setText(habit.getDesc());
        holder.getDone().setText(habit.isDone() ? "DONE" : "NOT DONE");
    }

    @Override
    public int getItemCount() {
        return (null != habits ? habits.size() : 0);
    }
}
