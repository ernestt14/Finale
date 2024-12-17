package com.example.finale;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    private Button btnEnroll, btnSummary;

    private FirebaseAuth auth;
    private DatabaseReference databaseRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI components
        welcomeTextView = findViewById(R.id.welcomeTextView);
        btnEnroll = findViewById(R.id.btnEnroll);
        btnSummary = findViewById(R.id.btnSummary);

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            // If no user is logged in, redirect to Login page
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Get reference to the database
        databaseRef = FirebaseDatabase.getInstance().getReference("Students").child(currentUser.getUid());

        // Fetch and display the student's name
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    welcomeTextView.setText("Hello " + name);
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Button to enroll for next semester
        btnEnroll.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, EnrollActivity.class));
        });

        // Button to view enrollment summary
        btnSummary.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, SummaryActivity.class));
        });
    }
}
