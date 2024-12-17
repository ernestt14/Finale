package com.example.finale;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EnrollActivity extends AppCompatActivity {

    private LinearLayout subjectsLayout;
    private TextView totalCreditsText;
    private Button enrollButton;
    private FirebaseAuth auth;
    private DatabaseReference enrollmentsRef;
    private int MAX_CREDITS = 24;
    private List<Subject> selectedSubjects = new ArrayList<>();
    private int totalCredits = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);

        subjectsLayout = findViewById(R.id.subjectsLayout);
        totalCreditsText = findViewById(R.id.totalCreditsText);
        enrollButton = findViewById(R.id.enrollButton);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        // Database reference
        String userID = currentUser.getUid();
        enrollmentsRef = FirebaseDatabase.getInstance().getReference("Enrollments").child(userID);

        // Load Subjects
        loadSubjects();

        // Handle Enroll Button Click
        enrollButton.setOnClickListener(v -> enrollSubjects());
    }

    private void loadSubjects() {
        // Example subject data - can come from Firebase "Subjects" node
        String[][] subjects = {
                {"Math 101", "3", "Room A", "Mon 9:00 AM"},
                {"Physics 201", "4", "Room B", "Wed 11:00 AM"},
                {"Chemistry 301", "3", "Room C", "Fri 2:00 PM"},
                {"English 401", "2", "Room D", "Tue 11:00 AM"},
                {"History 501", "3", "Room E", "Thu 3:00 PM"},
                {"Biology 601", "4", "Room F", "Mon 1:00 PM"},
                {"Computer Science", "3", "Room G", "Wed 7:00 AM"},
                {"Art 101", "2", "Room H", "Mon 10:00 AM"},
                {"Philosophy 201", "3", "Room I", "Tue 1:00 PM"},
                {"Psychology 301", "3", "Room J", "Thu 9:00 AM"},
                {"Economics 401", "4", "Room K", "Fri 11:00 AM"},
                {"Sociology 501", "2", "Room L", "Wed 3:00 PM"},
                {"Statistics 601", "4", "Room M", "Tue 4:00 PM"},
                {"Mechanical Engineering 701", "5", "Room N", "Mon 2:00 PM"},
                {"Electrical Engineering 801", "5", "Room O", "Thu 10:00 AM"},
                {"Literature 901", "3", "Room P", "Fri 9:00 AM"},
                {"Business Administration 1001", "4", "Room Q", "Wed 12:00 PM"},
                {"Environmental Science 1101", "3", "Room R", "Mon 5:00 PM"}
        };

        for (String[] subject : subjects) {
            String subjectName = subject[0];
            int subjectCredits = Integer.parseInt(subject[1]);
            String className = subject[2];
            String schedule = subject[3];

            // Inflate subject view
            View subjectView = getLayoutInflater().inflate(R.layout.item_subject, null);
            TextView subjectDetails = subjectView.findViewById(R.id.subjectDetails);
            CheckBox subjectCheckBox = subjectView.findViewById(R.id.subjectCheckBox);

            subjectDetails.setText(subjectName + " - " + subjectCredits + " Credits\nClass: " + className + ", Schedule: " + schedule);

            Subject subjectObj = new Subject(subjectName, subjectCredits, className, schedule);

            subjectCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedSubjects.add(subjectObj);
                    totalCredits += subjectCredits;
                } else {
                    selectedSubjects.remove(subjectObj);
                    totalCredits -= subjectCredits;
                }

                updateTotalCredits();
            });

            subjectsLayout.addView(subjectView);
        }
    }

    private void updateTotalCredits() {
        totalCreditsText.setText("Total Credits: " + totalCredits);
        if (totalCredits >= MAX_CREDITS) {
            enrollButton.setEnabled(false); // Disable the button if credit limit is exceeded
        } else {
            enrollButton.setEnabled(true); // Enable the button if the credit limit is not exceeded
        }
    }

    private void enrollSubjects() {
        if (selectedSubjects.isEmpty()) {
            Toast.makeText(EnrollActivity.this, "Please select subjects to enroll!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Subject subject : selectedSubjects) {
            enrollmentsRef.child(subject.getName()).setValue(subject);
        }

        Toast.makeText(EnrollActivity.this, "Subjects enrolled successfully!", Toast.LENGTH_SHORT).show();
    }
}
