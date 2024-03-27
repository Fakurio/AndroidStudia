package com.example.lab.lab1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.GradesViewHolder> {

    private ArrayList<Grade> gradesList;

    public RecyclerAdapter(ArrayList<Grade> gradesList) {
        this.gradesList = gradesList;
    }

    public static class GradesViewHolder extends RecyclerView.ViewHolder {
        private TextView subjectName;
        private RadioGroup subjectGrade;
        public GradesViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            subjectGrade = itemView.findViewById(R.id.subjectGrade);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.GradesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.subjects_list_item, parent, false);
        return new GradesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.GradesViewHolder holder, int position) {
        String subjectName = this.gradesList.get(position).getSubjectName();
        int grade = this.gradesList.get(position).getGrade();

        holder.subjectName.setText(subjectName);
        switch (grade) {
            case 2:
                holder.subjectGrade.check(R.id.grade2);
                break;
            case 3:
                holder.subjectGrade.check(R.id.grade3);
                break;
            case 4:
                holder.subjectGrade.check(R.id.grade4);
                break;
            case 5:
                holder.subjectGrade.check(R.id.grade5);
                break;
        }

        holder.subjectGrade.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            this.gradesList.get(position).setGrade(Integer.parseInt(radioButton.getText().toString()));
        });
    }

    @Override
    public int getItemCount() {
        return this.gradesList.size();
    }
}
