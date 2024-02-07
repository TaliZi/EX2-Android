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

        TextView Title,gotoRegister;
        EditText emailET,passwordET;
        Button enterBTN;

        Title = findViewById(R.id.title_TV);
        enterBTN = findViewById(R.id.enterBTN);
        emailET = findViewById(R.id.useremailET);
        passwordET = findViewById(R.id.passwordET);
        gotoRegister = findViewById(R.id.registerTV);

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        enterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String storedpassword,storedemail,firstname,userInputEmail,userInputPassword;
                    storedemail=extras.getString("email");
                    storedpassword=extras.getString("password");
                    firstname=extras.getString("firstname");

                    userInputEmail = emailET.getText().toString();
                    userInputPassword = passwordET.getText().toString();

                    if(storedemail.equals(userInputEmail) && storedpassword.equals(userInputPassword)) {
                        Intent myIntent = new Intent(MainActivity.this, PostActivity.class);

                        // Get Uri from Intent
                        Uri userImage=getIntent().getData();
                        myIntent.setData(userImage);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK &(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));
                        myIntent.putExtra("firstname", firstname);
                        myIntent.putExtra("email", emailET.getText().toString());
                        myIntent.putExtra("indicator", "main");
                      //myIntent.putExtra("password", userInputPassword);
                        startActivity(myIntent);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Password or email doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Intent myIntent = new Intent(MainActivity.this, PostActivity.class);

                    // Get Uri from Intent
                    Uri userImage= Uri.parse("android.resource://com.example.taliSocialMedia/drawable/headshot");
                    myIntent.setData(userImage);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    myIntent.putExtra("firstname", "Aviel");
                    myIntent.putExtra("email", "aviel@gmail.com");
                    myIntent.putExtra("indicator", "main");
                    //myIntent.putExtra("password", userInputPassword);
                    startActivity(myIntent);

                  //  Toast.makeText(MainActivity.this, "Password or username doesn't match", Toast.LENGTH_SHORT).show();
                }

            }
        });





    }
}