package com.example.finale;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SummarySubjectAdapter extends RecyclerView.Adapter<SummarySubjectAdapter.SubjectViewHolder> {

    private List<Subject> subjectList;

    public SummarySubjectAdapter(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Check if the context is SummaryActivity, and inflate the corresponding layout
        View view;
        if (parent.getContext() instanceof SummaryActivity) {
            // Inflate the layout without CheckBox for SummaryActivity
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject_summary, parent, false);
        } else {
            // Inflate the original layout with CheckBox for other activities
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        }
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.subjectName.setText(subject.getName());
        holder.subjectDetails.setText("Class: " + subject.getClassName() + "\nSchedule: " + subject.getSchedule() + "\nCredits: " + subject.getCredits());
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        TextView subjectDetails;

        public SubjectViewHolder(View itemView) {
            super(itemView);
            // Bind views
            subjectName = itemView.findViewById(R.id.subjectName);
            subjectDetails = itemView.findViewById(R.id.subjectDetails);
        }
    }
}
