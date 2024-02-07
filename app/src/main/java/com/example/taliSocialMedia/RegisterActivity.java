package com.example.taliSocialMedia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST = 1234;
    Uri userimage=null;

    // used for email validation using regex
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView errorTV,gobackTV;
        EditText useremailET,passwordET,confirmpassET,firstnameET;
        Button registerBTN,imageFromGalleryBTN,imageFromCameraBTN;



        errorTV = findViewById(R.id.errorTV);
        gobackTV = findViewById(R.id.gobackTV);
        useremailET = findViewById(R.id.useremailET);
        passwordET = findViewById(R.id.passwordET);
        confirmpassET = findViewById(R.id.confirm_passwordET);
        firstnameET = findViewById(R.id.userfirstnameET);
        registerBTN = findViewById(R.id.registerBTN);
        imageFromGalleryBTN = findViewById(R.id.imageFromGalleryBTN);
        imageFromCameraBTN = findViewById(R.id.imageFromCameraBTN);


        errorTV.setVisibility(View.GONE);

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password,confirmpass,firstname;

                firstname = firstnameET.getText().toString();
                email = useremailET.getText().toString();
                password = passwordET.getText().toString();
                confirmpass = confirmpassET.getText().toString();


                if (firstname.length() < 2){
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText("* Username length must be 2 or more");
                }
                else if (firstname.matches(".*[0-9].*")){ // Check if first name has numbers
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText("* Name must not contain numbers");
                }

                else if(!validate(email))
                {
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText("Please enter a valid email");
                }
                else if(!password.equals(confirmpass))
                {
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText("Passwords do not match");
                }
                else if(password.length()<8)
                {
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText("Password length must be 8 or more");
                }
                else if(userimage==null)
                {
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText("Please upload an image");
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Registration complete", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    myIntent.setData(userimage);
                    myIntent.putExtra("email",email);
                    myIntent.putExtra("firstname",firstname);
                    myIntent.putExtra("password",password);
                    startActivity(myIntent);
                }


            }
        });


        gobackTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageFromGalleryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), GALLERY_REQUEST);
            }
        });

        imageFromCameraBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // After coming back from camera/gallery , we check where we came back from, and act accordingly
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            // Main goal here is to convert bitmap to URI

            // 1) Get bitmap of captured image
            // 2) Compress it and store it
            // 3) *Uri*.parse the path to the image
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), photo, "My photo", null);

            // Now we have a URI (what we wanted) and we can set userImage.
            userimage=Uri.parse(path);;
        }

        else if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    userimage=selectedImageUri;
            }
        }
    }


    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }
}