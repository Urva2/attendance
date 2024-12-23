package com.example.attendance; // Replace with your package name

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth and Database Reference
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // Check if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // If logged in, retrieve user role and navigate accordingly
            String userId = currentUser.getUid();
            userRef.child(userId).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String role = snapshot.getValue(String.class);
                        if ("teacher".equals(role)) {
                            startActivity(new Intent(MainActivity.this, TeacherMainActivity.class));
                        } else if ("student".equals(role)) {
                            startActivity(new Intent(MainActivity.this, StudentMainActivity.class));
                        }
                        finish(); // Close MainActivity
                    } else {
                        Toast.makeText(MainActivity.this, "Role not found, please register again.", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // If not logged in, navigate to LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }
}