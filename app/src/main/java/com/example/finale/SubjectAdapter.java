package com.example.finale;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private List<Subject> subjectList;
    private OnSubjectCheckedListener listener;

    public interface OnSubjectCheckedListener {
        void onSubjectCheckedChange(Subject subject, boolean isChecked);
    }

    public SubjectAdapter(List<Subject> subjectList, OnSubjectCheckedListener listener) {
        this.subjectList = subjectList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject subject = subjectList.get(position);

        holder.subjectName.setText(subject.getName());
        holder.subjectDetails.setText("Class: " + subject.getClassName() +
                "\nSchedule: " + subject.getSchedule() +
                "\nCredits: " + subject.getCredits());
        holder.checkBox.setChecked(subject.isSelected());

        // Listen for checkbox changes
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            subject.setSelected(isChecked);
            listener.onSubjectCheckedChange(subject, isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName, subjectDetails;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            subjectDetails = itemView.findViewById(R.id.subjectDetails);
            checkBox = itemView.findViewById(R.id.subjectCheckBox);
        }
    }
}
