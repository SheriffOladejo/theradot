package com.theradot.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theradot.Activities.CreateReminderActivity;
import com.theradot.Adapters.AlarmAdapter;
import com.theradot.Models.Alarm;
import com.theradot.R;
import com.theradot.Utilities.DbHelper;
import com.theradot.Utilities.UtilityMethods;

import java.util.ArrayList;

public class ReminderFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    FloatingActionButton create_reminder;
    LinearLayout no_reminder;
    AlarmAdapter adapter;
    ArrayList<Alarm> list;
    DbHelper helper;
    Context context;
    RelativeLayout multi_select_layout;
    ImageView close;
    CheckBox select_all;
    TextView number_selected;
    ImageView delete_reminder;

    ArrayList<Alarm> to_delete = new ArrayList<>();


    public ReminderFragment(Context context){
        this.context = context;
        helper = new DbHelper(this.context);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(helper.getAlarms().size() >= 1){
            no_reminder.setVisibility(View.GONE);
        }
        else{
            no_reminder.setVisibility(View.VISIBLE);
        }
        adapter = new AlarmAdapter(getContext(), helper.getAlarms(), false, false, multi_select_layout, recyclerView, number_selected, to_delete, select_all, delete_reminder, create_reminder);
        recyclerView.setAdapter(adapter);
    }

    public ReminderFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_reminder, container, false);
        helper = new DbHelper(this.context);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        create_reminder = view.findViewById(R.id.create_new_reminder);
        delete_reminder = view.findViewById(R.id.delete_reminder);
        no_reminder = view.findViewById(R.id.no_reminder_ll);
        multi_select_layout = view.findViewById(R.id.multi_select_layout);
        close = view.findViewById(R.id.close_multi_select);
        select_all = view.findViewById(R.id.select_all);
        number_selected = view.findViewById(R.id.number_selected);

        delete_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(to_delete.size() != 0){
                    for(int i=0; i<to_delete.size(); i++){
                        helper.deleteAlarm(to_delete.get(i).getId());
                    }
                    adapter = new AlarmAdapter(getContext(), helper.getAlarms(), false, false, multi_select_layout, recyclerView, number_selected, to_delete, select_all, delete_reminder, create_reminder);
                    recyclerView.setAdapter(adapter);
                    delete_reminder.setVisibility(View.GONE);
                    create_reminder.setVisibility(View.VISIBLE);
                    multi_select_layout.setVisibility(View.GONE);
                    to_delete.clear();
                    new UtilityMethods(getContext()).toast("Reminder deleted");
                    if(helper.getAlarms().size() >= 1){

                        no_reminder.setVisibility(View.GONE);
                    }
                    else{
                        no_reminder.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        select_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    to_delete = helper.getAlarms();
                    number_selected.setText(to_delete.size() + " items selected");
                    adapter = new AlarmAdapter(getContext(), list, true, true, multi_select_layout,recyclerView, number_selected, to_delete, select_all, delete_reminder, create_reminder);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    to_delete = helper.getAlarms();
                    number_selected.setText("0 items selected");
                    adapter = new AlarmAdapter(getContext(), list, false, true, multi_select_layout, recyclerView, number_selected, to_delete, select_all, delete_reminder, create_reminder);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multi_select_layout.setVisibility(View.GONE);
                delete_reminder.setVisibility(View.GONE);
                create_reminder.setVisibility(View.VISIBLE);
                adapter = new AlarmAdapter(getContext(), list, false, false, multi_select_layout, recyclerView, number_selected, to_delete, select_all, delete_reminder, create_reminder);
                recyclerView.setAdapter(adapter);
            }
        });

        create_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intet = new Intent(getContext(), CreateReminderActivity.class);
                startActivity(intet);
            }
        });

        list = helper.getAlarms();
        if(list.size() == 0){
            no_reminder.setVisibility(View.VISIBLE);
        }
        else{
            no_reminder.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new AlarmAdapter(getContext(), list, false, false, multi_select_layout, recyclerView, number_selected, to_delete, select_all, delete_reminder, create_reminder);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }
}
