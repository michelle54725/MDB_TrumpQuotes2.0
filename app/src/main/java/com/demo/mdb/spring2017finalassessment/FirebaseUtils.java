package com.demo.mdb.spring2017finalassessment;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUtils {
    private static final String TAG = "EmailPassword";
    private static FirebaseAuth mAuth;
    private static FirebaseUser user;


    static FirebaseUser createAccount(Context context, String email, String password) {
        mAuth = FirebaseAuth.getInstance();
        user = null;
        Log.d(TAG, "createAccount:" + email);

        if (email.equals("") || password.equals("") || password.length()<6) {
            Log.d(TAG, "invalid input: " + email + "; " + password);
            return user;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
        return user;
    }

    static FirebaseUser signIn(Context context, String email, String password) {
        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "signIn:" + email);

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            user = mAuth.getCurrentUser();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                        }
                    }
                });
        return user;
    }

}

