package com.example.taliSocialMedia;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);


        Button backButton,addCommentBTN;
        EditText userMessage;
        ImageView userImage;
        TextView userFirstName;

        /*
        We come here from PostActivity to create a new comment.
         */


        backButton = findViewById(R.id.goBackBTN);
        userMessage = findViewById(R.id.usermessageET);
        userMessage.setText("");
        userImage = findViewById(R.id.userImage);
        userFirstName = findViewById(R.id.username_TV);
        addCommentBTN = findViewById(R.id.finishBTN);

        Bundle extras = getIntent().getExtras();

        userImage.setImageURI(getIntent().getData());
        userFirstName.setText(extras.getString("firstname"));









        addCommentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(AddCommentActivity.this, PostActivity.class);

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                myIntent.setData(getIntent().getData());
                myIntent.putExtra("firstname", extras.getString("firstname"));
                myIntent.putExtra("indicator", "afterComment");
                myIntent.putExtra("email", extras.getString("email"));
                myIntent.putExtra("message", userMessage.getText().toString() );
                myIntent.putExtra("date", formattedDate );
                startActivity(myIntent);

            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}