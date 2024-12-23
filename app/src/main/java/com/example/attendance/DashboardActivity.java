package com.example.attendance;



import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class DashboardActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // User is logged in, check role
            checkUserRole();
        } else {
            // User is not logged in, go to LoginActivity
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void checkUserRole() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Retrieve user role from Firebase
        FirebaseDatabase.getInstance().getReference("Users").child(uid).child("role")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String role = task.getResult().getValue(String.class);
                        if ("Teacher".equals(role)) {
                            startActivity(new Intent(DashboardActivity.this, TeacherMainActivity.class));
                        } else if ("Student".equals(role)) {
                            startActivity(new Intent(DashboardActivity.this, StudentMainActivity.class));
                        }
                    }
                });
    }
}