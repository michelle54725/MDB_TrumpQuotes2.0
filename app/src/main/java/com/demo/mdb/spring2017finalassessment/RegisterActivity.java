package com.demo.mdb.spring2017finalassessment;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mName;
    private Uri img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /* TODO Part 2
        * Implement registration. If the imageView is clicked, set it to an image from the gallery
        * and store the image as a Uri instance variable (also change the imageView's image to this
        * Uri. If the create new user button is pressed, call createUser using the email and password
        * from the edittexts. Remember that it's email2 and password2 now!
        */

        //Fields
        mEmailField = findViewById(R.id.email2);
        mPasswordField = findViewById(R.id.password2);
        mName = findViewById(R.id.name);


        //Buttons
        findViewById(R.id.imageView).setOnClickListener(this);
        findViewById(R.id.createnewuser).setOnClickListener(this);

    }

    private void createUser(final String email, final String password) {
        /* TODO Part 2.1
         * This part's long, so listen up!
         * Create a user, and if it fails, display a Toast.
         *
         * If it works, we're going to add their image to the database. To do this, we will need a
         * unique user id to identify the user (push isn't the best answer here. Do some Googling!)
         *
         * Now, if THAT works (storing the image), set the name and photo uri of the user (hint: you
         * want to update a firebase user's profile.)
         *
         * Finally, if updating the user profile works, go to the TabbedActivity
         */
        FirebaseUser user = null;
        user = FirebaseUtils.createAccount(RegisterActivity.this, mEmailField.getText().toString(), mPasswordField.getText().toString());
        if (user == null) {
        Toast.makeText(RegisterActivity.this, "Invalid input. Please try again.",
                Toast.LENGTH_SHORT).show();
        } else {
            // put into database
            final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("/users");
            String mKey = mAuth.getUid(); // get unique user id
            // make User object to store in myRef
            User userObject = new User(
                    mKey,
                    mName.getText().toString(),
                    mEmailField.getText().toString(),
                    img);
            //store in Database
            myRef.child(mKey).setValue(userObject);
            finish();

            startActivity(new Intent(RegisterActivity.this, TabbedActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        switch (b.getId()) {
            case R.id.imageView:
                // Set to image from gallery, store image as Uri instance var, chagne imageView's image
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);

            case R.id.createnewuser:
                createUser(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data != null) {
                final Uri file = data.getData();
                img = file;
                RequestOptions myOptions = new RequestOptions()
                        .override(100, 100);
                Glide.with(RegisterActivity.this)
                        .load(img)
                        .apply(myOptions).into(findViewById(R.id.imageView));
            }
        }
    }

    public class User {
        String key;
        String name;
        String email;
        Uri pic;
        ArrayList<Game> games;

        public User() {}

        public User(String key, String name, String email, Uri pic) {
            this.name = name;
            this.name = name;
            this.email = email;
            this.pic = pic;
            games = new ArrayList<>();
        }
    }
}
