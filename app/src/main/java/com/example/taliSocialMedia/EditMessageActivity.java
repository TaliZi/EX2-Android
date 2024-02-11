package com.example.taliSocialMedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditMessageActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1234;
    String indicate="";
    Uri postPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);

        Bundle extras = getIntent().getExtras();

        Button backButton,submitPostBTN,changePicBTN;
        EditText userMessage;
        ImageView userImage;
        TextView userFirstName,titleTV;
        String theMessage,theTitle;


        /*

        We come here from PostActivity to either edit a comment or a post.
        we use the "indicate" variable to know what we edit
        So if its a post we want, for example, show the "Change Picture" button.

         */


        backButton = findViewById(R.id.goBackBTN);
        userMessage = findViewById(R.id.usermessageET);
        userImage = findViewById(R.id.userImage);
        userFirstName = findViewById(R.id.username_TV);
        submitPostBTN = findViewById(R.id.finishBTN);
        titleTV = findViewById(R.id.Title_TV);
        changePicBTN = findViewById(R.id.changePicBTN);


        if(extras != null) {
            userImage.setImageURI(getIntent().getData());
            userFirstName.setText(extras.getString("firstname"));
            theMessage = extras.getString("message");
            theTitle = extras.getString("title");
            userMessage.setText(theMessage);
            titleTV.setText(theTitle);
            indicate = extras.getString("indicate");
            postPhoto = Uri.parse(extras.getString("postImage"));
            if(indicate.equals("post")) {
                changePicBTN.setVisibility(View.VISIBLE);
            }

        }




        submitPostBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(EditMessageActivity.this, PostActivity.class);

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                String temp = (indicate.equals("post"))?"afterEditPost":"afterEditComment";

                myIntent.setData(getIntent().getData());
                myIntent.putExtra("firstname", extras.getString("firstname"));
                myIntent.putExtra("indicator", temp);
                myIntent.putExtra("email", extras.getString("email"));
                myIntent.putExtra("message", userMessage.getText().toString() );
                myIntent.putExtra("date", formattedDate );
                myIntent.putExtra("position",extras.getInt("position",0));
                myIntent.putExtra("postImage",postPhoto.toString());
                startActivity(myIntent);

            }
        });

        changePicBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), GALLERY_REQUEST);
            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // After coming back from camera/gallery , we check where we came back from, and act accordingly
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            // Get the url of the image from data
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                postPhoto=selectedImageUri;
            }
        }
    }
}
