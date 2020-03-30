package strathmore.university.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import strathmore.university.gym.Model.Users_94112;

public class RegisterActivity extends AppCompatActivity {

    private EditText mFirstName, mLastName, mEmail, mPassword, mConfirmPassword;
    private Button mRegister;
    private ProgressDialog mProgressDialog;
    private Intent intent;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    FirebaseDatabase db;
    DatabaseReference Users_94112;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        Users_94112 = db.getReference("Users_94112");

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent = new Intent(RegisterActivity.this, GymLocations.class);
                startActivity(intent);
                finish();
                return;
            }
        };

        mRegister = findViewById(R.id.btnRegister);
        mFirstName = (EditText) findViewById(R.id.firstName);
        mLastName = (EditText) findViewById(R.id.lastName);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confPassword);

        mProgressDialog = new ProgressDialog(this);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String firstName = mFirstName.getText().toString();
                final String lastName = mLastName.getText().toString();
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String confirmPassword = mConfirmPassword.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(TextUtils.isEmpty(firstName)){
                    mFirstName.setError("First Name required");
                    mFirstName.requestFocus();
                    Toast.makeText(RegisterActivity.this, "First Name required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(lastName)){
                    mLastName.setError("Last Name required");
                    mLastName.requestFocus();
                    Toast.makeText(RegisterActivity.this, "Last Name required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email Address Required");
                    mEmail.requestFocus();
                    Toast.makeText(RegisterActivity.this, "Email required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email.matches(emailPattern)){
                    mEmail.setError("Enter a Valid Email Address");
                    mEmail.requestFocus();
                    Toast.makeText(RegisterActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required");
                    mPassword.requestFocus();
                    Toast.makeText(RegisterActivity.this, "Password required", Toast.LENGTH_SHORT).show();

                    return;
                }
                if (password.length() < 6){
                    mPassword.setError("Password Required should be a minimum of 6 characters");
                    mPassword.requestFocus();
                    Toast.makeText(RegisterActivity.this, "Password Require minimum of 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length() != confirmPassword.length()){
                    mConfirmPassword.setError("Password Does Not Match");
                    mConfirmPassword.requestFocus();
                    Toast.makeText(RegisterActivity.this, "Password Not Match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Register New User
                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(),mPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Save Users to Database
                        Users_94112 user = new Users_94112();
                        user.setFirstName(mFirstName.getText().toString());
                        user.setLastName(mLastName.getText().toString());
                        user.setEmail(mEmail.getText().toString());

                        // Use Email to key
                        Users_94112.child(FirebaseAuth.getInstance().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(RegisterActivity.this, GymLocations.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
