package strathmore.university.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;
import strathmore.university.gym.Helper.LocaleHelper;

public class MainActivity extends AppCompatActivity {

    EditText mEmail, mPassword;
    TextView mRegister;
    TextView mReset;
    TextView mEnter;
    private Button mLogin;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference Users_94112;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase,"en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEnter = (TextView)findViewById(R.id.login);
        mRegister = (TextView) findViewById(R.id.register);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mReset = (TextView) findViewById(R.id.forgotPassword);
        mLogin = (Button) findViewById(R.id.btnLogin);

        // Init paper first
        Paper.init(this);

        // Default language is English
        String language = Paper.book().read("language");
        if (language == null){
            Paper.book().write("language", "en");
            updateView((String)Paper.book().read("language"));
        }

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
                        Toast.makeText(MainActivity.this, "Please Enter Your Email or Password Correctly", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }

    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this,language);
        Resources resources = context.getResources();

        mEnter.setText(resources.getString(R.string.login));
        mLogin.setText(resources.getString(R.string.login));
//        mEmail.setText(resources.getString(R.string.email));
 //       mPassword.setText(resources.getString(R.string.password));
        mRegister.setText(resources.getString(R.string.register));
        mReset.setText(resources.getString(R.string.reset));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.change_language, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.english) {
            Paper.book().write("language", "en");
            updateView((String)Paper.book().read("language"));
        }
        else if (item.getItemId() == R.id.swahili){
            Paper.book().write("language", "sw");
            updateView((String)Paper.book().read("language"));
        }
        return true;
    }
}
