package com.example.finale;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SummarySubjectAdapter subjectAdapter;
    private List<Subject> subjectList = new ArrayList<>();
    private FirebaseAuth auth;
    private DatabaseReference databaseRef;
    private DatabaseReference totalCreditsRef;
    private FirebaseUser currentUser;
    private TextView totalCreditsText; // Declare TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        totalCreditsText = findViewById(R.id.totalCreditsText); // Initialize TextView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            // If no user is logged in, redirect to login page
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Reference to the enrolled subjects and total credits for the current user
        databaseRef = FirebaseDatabase.getInstance().getReference("Enrollments").child(currentUser.getUid());
        totalCreditsRef = FirebaseDatabase.getInstance().getReference("Enrollments").child(currentUser.getUid()).child("totalCredits");

        // Fetch the enrolled subjects and total credits
        fetchEnrolledSubjects();
    }

    private void fetchEnrolledSubjects() {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                subjectList.clear();
                if (snapshot.exists()) {
                    // Loop through the subjects in the database
                    for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                        if (!subjectSnapshot.getKey().equals("totalCredits")) {
                            String name = subjectSnapshot.child("name").getValue(String.class);
                            int credits = subjectSnapshot.child("credits").getValue(Integer.class);
                            String className = subjectSnapshot.child("className").getValue(String.class);
                            String schedule = subjectSnapshot.child("schedule").getValue(String.class);

                            // Create Subject object and add to the list
                            Subject subject = new Subject(name, credits, className, schedule);
                            subjectList.add(subject);
                        }
                    }

                    // Set up the new adapter with the list of enrolled subjects
                    subjectAdapter = new SummarySubjectAdapter(subjectList);
                    recyclerView.setAdapter(subjectAdapter);

                    // Fetch and display the total credits
                    fetchTotalCredits();
                } else {
                    Toast.makeText(SummaryActivity.this, "No enrolled subjects found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SummaryActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTotalCredits() {
        totalCreditsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Integer credits = task.getResult().getValue(Integer.class);
                if (credits != null) {
                    // Display total credits in the UI
                    if (totalCreditsText != null) {
                        totalCreditsText.setText("Total Credits: " + credits);
                    }
                }
            } else {
                Toast.makeText(SummaryActivity.this, "Error fetching total credits.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
