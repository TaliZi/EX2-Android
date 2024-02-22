package com.example.taliSocialMedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        TextView Title, gotoRegister;
        EditText emailET, passwordET;
        Button enterBTN;

        // Find UI elements by their IDs
        Title = findViewById(R.id.title_TV);
        enterBTN = findViewById(R.id.enterBTN);
        emailET = findViewById(R.id.useremailET);
        passwordET = findViewById(R.id.passwordET);
        gotoRegister = findViewById(R.id.registerTV);

        // Click listener for "Register" TextView
        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        // Click listener for "Enter" Button
        enterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get extras from the intent
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    // Retrieve stored email, password, and firstname
                    String storedpassword, storedemail, firstname, userInputEmail, userInputPassword;
                    storedemail = extras.getString("email");
                    storedpassword = extras.getString("password");
                    firstname = extras.getString("firstname");

                    // Get user input email and password
                    userInputEmail = emailET.getText().toString();
                    userInputPassword = passwordET.getText().toString();

                    // Check if the entered email and password match the stored ones
                    if (storedemail.equals(userInputEmail) && storedpassword.equals(userInputPassword)) {
                        // If matched, create intent to start PostActivity
                        Intent myIntent = new Intent(MainActivity.this, PostActivity.class);

                        // Get Uri from Intent
                        Uri userImage = getIntent().getData();
                        myIntent.setData(userImage);

                        // Add flags to the intent
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));

                        // Put extras to the intent
                        myIntent.putExtra("firstname", firstname);
                        myIntent.putExtra("email", emailET.getText().toString());
                        myIntent.putExtra("indicator", "main");

                        // Start the activity
                        startActivity(myIntent);
                    } else {
                        // If not matched, show a toast message
                        Toast.makeText(MainActivity.this, "Password or email doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
