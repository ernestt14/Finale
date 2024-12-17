package com.example.finale;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EnrollActivity extends AppCompatActivity {

    public static final int MAX_CREDITS = 24;
    private RecyclerView recyclerView;
    private TextView totalCreditsText;
    private FirebaseAuth auth;
    private DatabaseReference enrollmentsRef;
    private List<Subject> subjectList = new ArrayList<>();
    private int totalCredits = 0;
    private SubjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);

        recyclerView = findViewById(R.id.recyclerView);
        totalCreditsText = findViewById(R.id.totalCreditsText);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userID = currentUser.getUid();
            enrollmentsRef = FirebaseDatabase.getInstance().getReference("Enrollments").child(userID);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadSubjects();
        fetchTotalCredits();
    }

    private void loadSubjects() {
        String[][] subjects = {
                {"Math 101", "3", "Room A", "Mon 9:00 AM"},
                {"Physics 201", "4", "Room B", "Wed 11:00 AM"},
                {"Chemistry 301", "3", "Room C", "Fri 2:00 PM"},
                {"English 401", "2", "Room D", "Tue 11:00 AM"},
                {"Biology 102", "3", "Room E", "Mon 1:00 PM"},
                {"History 203", "3", "Room F", "Thu 3:00 PM"},
                {"Computer Science 202", "4", "Room G", "Tue 9:00 AM"},
                {"Psychology 301", "3", "Room H", "Fri 10:00 AM"},
                {"Sociology 202", "3", "Room I", "Wed 2:00 PM"},
                {"Economics 101", "3", "Room J", "Mon 3:00 PM"},
                {"Art 105", "2", "Room K", "Thu 1:00 PM"},
                {"Philosophy 110", "3", "Room L", "Fri 4:00 PM"},
                {"Music 203", "2", "Room M", "Tue 2:00 PM"},
                {"Political Science 201", "4", "Room N", "Wed 9:00 AM"}
        };

        for (String[] subject : subjects) {
            String subjectName = subject[0];
            int subjectCredits = Integer.parseInt(subject[1]);
            String className = subject[2];
            String schedule = subject[3];

            Subject subjectObj = new Subject(subjectName, subjectCredits, className, schedule);
            subjectList.add(subjectObj);
        }

        adapter = new SubjectAdapter(subjectList, this);
        recyclerView.setAdapter(adapter);

        // Check if each subject is already enrolled
        checkIfSubjectsAreEnrolled();
    }

    private void checkIfSubjectsAreEnrolled() {
        // Check if the user is already enrolled in any subject
        for (Subject subject : subjectList) {
            enrollmentsRef.child(subject.getName()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // If the subject is found in the enrollments, mark it as enrolled
                    if (task.getResult().exists()) {
                        subject.setEnrolled(true);
                    }
                    adapter.notifyDataSetChanged(); // Refresh the RecyclerView
                } else {
                    Toast.makeText(EnrollActivity.this, "Failed to check enrollment status.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void fetchTotalCredits() {
        enrollmentsRef.child("totalCredits").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Integer credits = task.getResult().getValue(Integer.class);
                totalCredits = (credits != null) ? credits : 0;
                updateTotalCredits();
            } else {
                Toast.makeText(EnrollActivity.this, "Failed to fetch total credits", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalCredits() {
        totalCreditsText.setText("Total Credits: " + totalCredits);
    }

    public void onEnrollButtonClick(Subject subject) {
        if (!subject.isEnrolled() && (totalCredits + subject.getCredits() <= MAX_CREDITS)) {
            subject.setEnrolled(true);
            totalCredits += subject.getCredits();

            // Save the enrolled subject to the "Enrollments" node
            enrollmentsRef.child(subject.getName()).setValue(subject);
            // Save the total credits under the "Enrollments" node
            enrollmentsRef.child("totalCredits").setValue(totalCredits);

            Toast.makeText(this, subject.getName() + " enrolled!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cannot enroll in " + subject.getName() + ". Credit limit exceeded or already enrolled.", Toast.LENGTH_SHORT).show();
        }

        updateTotalCredits();
        adapter.notifyDataSetChanged(); // Refresh the RecyclerView
    }
}
