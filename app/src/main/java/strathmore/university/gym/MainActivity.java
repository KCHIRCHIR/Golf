package strathmore.university.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText mEmail, mPassword;
    TextView mRegister;
    TextView mReset;
    private Button mLogin;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference Users_94112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRegister = findViewById(R.id.register);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mReset = findViewById(R.id.forgotPassword);
        mLogin = findViewById(R.id.btnLogin);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        Users_94112 = db.getReference("Users_94112");

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResetPassword.class);
                startActivity(intent);
                finish();
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check Email Validation
                if (TextUtils.isEmpty(mEmail.getText())) {
                    Toast.makeText(MainActivity.this, "Please Enter Your Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check Password Validation
                if (mPassword.getText().toString().length() < 6) {
                    Toast.makeText(MainActivity.this, "Password Is Too Short !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Sign In
                auth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(MainActivity.this, GymLocations.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Please enter your Email or Password Correctly", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }
}
