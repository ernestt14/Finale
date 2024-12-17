package com.example.finale;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private List<Subject> subjectList;
    private EnrollActivity activity;

    public SubjectAdapter(List<Subject> subjectList, EnrollActivity activity) {
        this.subjectList = subjectList;
        this.activity = activity;
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

        // Set subject details
        holder.subjectName.setText(subject.getName());
        holder.subjectDetails.setText("Class: " + subject.getClassName() +
                "\nSchedule: " + subject.getSchedule() +
                "\nCredits: " + subject.getCredits());

        // Update button visibility and behavior
        if (subject.isEnrolled()) {
            holder.enrollButton.setVisibility(View.GONE); // Hide button if already enrolled
        } else {
            holder.enrollButton.setVisibility(View.VISIBLE); // Show button if not enrolled
            holder.enrollButton.setText("Enroll");

            holder.enrollButton.setOnClickListener(v -> {
                activity.onEnrollButtonClick(subject); // Enroll subject
                notifyDataSetChanged(); // Refresh list to hide button after enrollment
            });
        }
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName, subjectDetails;
        Button enrollButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            subjectDetails = itemView.findViewById(R.id.subjectDetails);
            enrollButton = itemView.findViewById(R.id.enrollButton);
        }
    }
}
