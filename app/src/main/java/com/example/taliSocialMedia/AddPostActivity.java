package com.example.taliSocialMedia;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddPostActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 1234;

    Uri photoForPost=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        Button backButton,submitPostBTN,addImageBtn;
        EditText userMessage;
        ImageView userImage;
        TextView userFirstName;

        /*

        We come here from PostActivity to create a new post.

         */


        backButton = findViewById(R.id.goBackBTN);
        userMessage = findViewById(R.id.usermessageET);
        userMessage.setText("");
        userImage = findViewById(R.id.userImage);
        userFirstName = findViewById(R.id.username_TV);
        submitPostBTN = findViewById(R.id.finishBTN);
        addImageBtn = findViewById(R.id.postAddPicture);

        Bundle extras = getIntent().getExtras();

        userImage.setImageURI(getIntent().getData());
        userFirstName.setText(extras.getString("firstname"));









        submitPostBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(photoForPost==null){
                    Toast.makeText(AddPostActivity.this, "Must choose image", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent myIntent = new Intent(AddPostActivity.this, PostActivity.class);
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);

                    myIntent.setData(getIntent().getData());
                    myIntent.putExtra("firstname", extras.getString("firstname"));
                    myIntent.putExtra("indicator", "afterPost");
                    myIntent.putExtra("email", extras.getString("email"));
                    myIntent.putExtra("message", userMessage.getText().toString());
                    myIntent.putExtra("date", formattedDate);
                    myIntent.putExtra("postImage", photoForPost.toString());
                    startActivity(myIntent);
                }
            }
        });

        addImageBtn.setOnClickListener(new View.OnClickListener() {
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
                photoForPost=selectedImageUri;
            }
        }
    }

}