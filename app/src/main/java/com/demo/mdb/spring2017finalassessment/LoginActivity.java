package com.demo.mdb.spring2017finalassessment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /* TODO Part 1
         * Implement login. When the login button is pressed, attempt to login the user, and if
         * that is successful, go to the TabbedActivity. If the register button is pressed, go to
         * the RegisterActivity. Check the layout files for the IDs of the views used in this part
         */
        //Fields
        mEmailField = findViewById(R.id.email);
        mPasswordField = findViewById(R.id.password);

        //Buttons
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        FirebaseUser user = null;
        switch (b.getId()) {
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            case R.id.login:
                user = FirebaseUtils.signIn(LoginActivity.this, mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
        if (user == null) {
            Toast.makeText(LoginActivity.this, "Invalid input. Please try again.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            startActivity(new Intent(LoginActivity.this, TabbedActivity.class));
        }
    }

}
}
