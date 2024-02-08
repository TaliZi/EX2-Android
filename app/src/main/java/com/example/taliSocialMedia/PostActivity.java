package com.example.taliSocialMedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,RecyclerViewInterface {

    // Global variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SearchView searchView;

    List<Item> items = new ArrayList<>();
    List<Item> itemsForComments = new ArrayList<>();
    List<Item> itemsClickedComment = new ArrayList<>();
    RecyclerView recyclerView,recyclerViewForComments;
    MyAdapter myAdapter;
    MyAdapterForComments myAdapterForComments;

    RadioButton nightModeBtn;
    Item testCaseAddComment=null;

    Writer writer;
    JSONObject jObject;
    JSONArray jArray;

    String userEmail;

    boolean viewingComments=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        ImageView userImage;
        TextView userName;
        Button backBTN,addPostBTN;


        // View attachments
        userImage = findViewById(R.id.userImage);
        userName = findViewById(R.id.username_TV);
        backBTN = findViewById(R.id.signOutBTN);
        addPostBTN = findViewById(R.id.addPostBTN);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewForComments = findViewById(R.id.recyclerViewForComments);
        drawerLayout= findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        searchView=findViewById(R.id.searchView);
        searchView.clearFocus();
        nightModeBtn = findViewById(R.id.nightModeBTN);

        // Setup two adapters, one for posts and one for comments
        myAdapter = new MyAdapter(getApplicationContext(),items,this);
        myAdapterForComments = new MyAdapterForComments(getApplicationContext(),itemsForComments,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);


        // Simple search logic (when text change, filter list by input-text and refresh list)
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        // Night mode button MODE_NIGHT_YES / MODE_NIGHT_NO
        nightModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO || AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
                    nightModeBtn.setChecked(true);
                    nightModeBtn.setSelected(true);

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                } else {
                    nightModeBtn.setChecked(false);
                    nightModeBtn.setSelected(false);

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        // Hamburger menu
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        // Get info from the activity that launched us
        // Four Scenarios
        // 1) We got called from MainActivity
        // 2) We got called from AddPostActivity
        // 3) We got called from AddCommentActivity
        // 4) We got called from EditMessageActivity (edit post/comment activity)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Get name, email, image from the activity that launched us
            // indicator = to know where we came from and act accordingly (after post? after comment? after edit?)
            String indicator,firstname,email;
            Uri imageUri = getIntent().getData();
            indicator = extras.getString("indicator");
            firstname = extras.getString("firstname");
            userEmail = extras.getString("email");

            userImage.setImageURI(imageUri);
            userName.setText(firstname);


            // Here we take care of the different scenarios.
            // For example, if we came after creating new post, Then what we want to do is add the new "post" to the items array and notify the adapter so we will see the new post on our screen
            // Or , if we came after 'editing comment' we change the comment.
            switch (indicator) {
                case "afterPost": {
                    Uri postPhoto = Uri.parse(extras.getString("postImage"));
                    Item newItem = new Item(extras.getString("firstname"), extras.getString("message"), imageUri, extras.getString("date"), R.id.like, false, userEmail, true, false,postPhoto);
                    items.add(newItem);
                    myAdapter.notifyDataSetChanged();
                    break;
                }
                case "afterComment":
                case "afterEditComment": {
                    testCaseAddComment = new Item(extras.getString("firstname"), extras.getString("message"), imageUri, extras.getString("date"), R.id.like, false, userEmail, true, false);
                    break;
                }
                case "afterEditPost": {
                    Uri postPhoto = Uri.parse(extras.getString("postImage"));
                    Item newItem = new Item(extras.getString("firstname"), extras.getString("message"), imageUri, extras.getString("date"), R.id.like, false, userEmail, true, false,postPhoto);
                    int pos = extras.getInt("position");
                    items.add(newItem);
                    break;
                }
            }
        }


        // For JSON reading
        InputStream is = getResources().openRawResource(R.raw.users);
        writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Here we traverse through the JSON array (The array is called "Users" in the JSON file)
        // - getting the relevant information from each object in the JSON array
        // We put all the info together to create an "Item" object, and add it to the "items" array.
        // When we finish, the 'items' array should be full with items that represent different posts.
        String jsonString = writer.toString();
        try {
            jObject = new JSONObject(jsonString);
            jArray = jObject.getJSONArray("Users");

            for (int i=0; i < jArray.length(); i++)
            {
                try {
                    JSONObject Object = jArray.getJSONObject(i);
                    // Pulling items from the array
                    String name = Object.getString("name");
                    String message = Object.getString("message");
                    String date = Object.getString("date");
                    String email = Object.getString("email");
                    Uri image = Uri.parse(Object.getString("image"));


                    if(i%4==0) {
                        items.add(new Item(name, message, R.drawable.firstpic, date,R.id.like,false,email,userEmail.equals(email),false));
                    }
                    else if(i%4==1) {
                        items.add(new Item(name, message, R.drawable.secondpic, date,R.id.like,false,email,userEmail.equals(email),false));
                    }
                    else if(i%4==2) {
                        items.add(new Item(name, message, R.drawable.thirdpic, date,R.id.like,false,email,userEmail.equals(email),false));
                    }
                    else if(i%4==3) {
                        items.add(new Item(name, message, R.drawable.fourthpic, date,R.id.like,false,email,userEmail.equals(email),false));
                    }
                } catch (JSONException e) {
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }






        // A back btn that simply calls "onBackPressed" when clicked
        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // When we want to add post, we call the AddPostActivity.
        // That activity shows the userImage + his name (and uses his email behind the scenes)
        // So we put the data into the intent for AddPostActivity to use
        addPostBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (extras != null) {

                    Intent myIntent = new Intent(PostActivity.this, AddPostActivity.class);
                    // Get Uri from Intent
                    Uri userImage=getIntent().getData();
                    myIntent.setData(userImage);
                    myIntent.putExtra("firstname", extras.getString("firstname"));
                    myIntent.putExtra("email", extras.getString("email"));
                    startActivity(myIntent);

                }
            }
        });

    }

    // Used for the search bar, whenever we type something into the search bar, this function will run
    // This function gets the current string that's in the search bar
    // And compares it to the names of all the post-creators (for Item item:items)
    // If the name contains the string in the search bar, it will be added to the filteredList
    // Finally we feed the adapter the filteredList, and we only see posts that match our search.
    private void filterList(String text) {
        List<Item> filteredList = new ArrayList<>();
        for (Item item : items){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }


            myAdapter.setFilteredList(filteredList);

    }

    // When the back button is pressed , we check if the hamburger menu is open , if it is , we close it
    // If its not, only then we check if were viewing comments of a post , if we are, change the appearance to normal (as if were not viewing comments anymore)
    // And finally if the hamburger menu is not open AND were not viewing comments, we go back to the main menu.
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(viewingComments)
        {
            recyclerView.setAdapter(myAdapter);
            recyclerViewForComments.setVisibility(View.GONE);
            viewingComments = false;
            myAdapter.notifyDataSetChanged();
        }
        else {
            Intent myIntent = new Intent(PostActivity.this, MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(myIntent);
        }
    }

    // Not implemented yet - hamburger menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }




    @Override
    public void onItemClick(int position) {/*
    OnItemClick - whenever we click on an item, if were not viewing comments, that means we want to
    load the comments of the post we just clicked.

    The screen will now show 2 recyclerViews (instead of 1 for all the posts)
    # one for the post we clicked (with the picture and all that)
    # and one for the comments (below the post, those without pictures)

    itemsClickedComment - is the itemArray for the post we clicked (an array with 1 item only! - the clicked post..)
    itemsForComments - is the itemArray for all the comments

    We make the "recyclerViewForComments" visible (because we want to see comments now, we set it to not be visible in the XML file
    set the recyclerview layout to linearlayout and set its adapter to "myAdapterForComments"
    the adapter was already set (not here) to use the "recyclerViewForComments" array.

    we then fill the "recyclerViewForComments" array with elements we load from the JSON Array called "Comments" (in the JSON file)
    and then we're done.
     */
        if(viewingComments==false)
        {
            itemsClickedComment.clear();

            if(items.get(position).getUriImage() == null)
                itemsClickedComment.add(new Item(items.get(position).getName(),items.get(position).getMessage(),items.get(position).getImage(),items.get(position).getDate(),items.get(position).getLike(),items.get(position).isitliked,items.get(position).getEmail(),items.get(position).isPostByCurrentUser,true,items.get(position).getPostPhoto()));
            else
                itemsClickedComment.add(new Item(items.get(position).getName(),items.get(position).getMessage(),items.get(position).getUriImage(),items.get(position).getDate(),items.get(position).getLike(),items.get(position).isitliked,items.get(position).getEmail(),items.get(position).isPostByCurrentUser,true,items.get(position).getPostPhoto()));



            MyAdapter mytempAdapter = new MyAdapter(getApplicationContext(), itemsClickedComment, this);
            recyclerView.setAdapter(mytempAdapter);
            mytempAdapter.notifyDataSetChanged();
            recyclerViewForComments.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewForComments.setAdapter(myAdapterForComments);
            recyclerViewForComments.setVisibility(View.VISIBLE);
            viewingComments = true;
            itemsForComments.clear();

            if(testCaseAddComment!=null){
                itemsForComments.add(testCaseAddComment);
            }


        for (int i=0; i < jArray.length(); i++)
        {
            // Example of a JSON object in the "comments" array
//            "commentFor": "danielcohen@gmail.com",
//                "name" : "Nehorai Rani",
//                "message": "This is my message",
//                "image": "",
//                "date": "31/01/2024"
            try {
                jArray = jObject.getJSONArray("Comments");
                JSONObject Object = jArray.getJSONObject(i);

                // Pulling items from the array
                String commentFor = Object.getString("commentFor");
                String name = Object.getString("name");
                String message = Object.getString("message");
                String date = Object.getString("date");
                String email = Object.getString("email");
                Uri image = Uri.parse(Object.getString("image"));


                // Only if the comment belongs to the clicked user , load the comment
                if (items.get(position).getEmail().equals(commentFor)) {
                    if (i % 4 == 0) {
                        itemsForComments.add(new Item(name, message, R.drawable.firstpic, date, R.id.like, false, email,userEmail.equals(email),false));
                    } else if (i % 4 == 1) {
                        itemsForComments.add(new Item(name, message, R.drawable.secondpic, date, R.id.like, false, email,userEmail.equals(email),false));
                    } else if (i % 4 == 2) {
                        itemsForComments.add(new Item(name, message, R.drawable.thirdpic, date, R.id.like, false, email,userEmail.equals(email),false));
                    } else if (i % 4 == 3) {
                        itemsForComments.add(new Item(name, message, R.drawable.fourthpic, date, R.id.like, false, email,userEmail.equals(email),false));
                    }
                }



            } catch (JSONException e) {
            }
        }
        }

    }

    // When we click like, check if the post is already liked. if it is liked -> do unlike , if its not liked -> do like.
    @Override
    public boolean onLikeClick(int position) {
        if(items.get(position).isitliked){
            items.get(position).setIsitliked(false);
            return true;
        }
        items.get(position).setIsitliked(true);
        return false;
    }


    // We use this method to know if the post is made by the current user
    // If yes, we show the "Edit" and "Delete" button
    @Override
    public boolean checkIfPostIsMadeByCurrentUser(int position) {
        if(viewingComments) {
            if (position == 0){
                // Don't show delete icon if viewing comments of your own post, you can only delete post from outside.
                if (userEmail.equals(itemsClickedComment.get(0).getEmail())) {
                return false;
                }
            }
            else{
                if (userEmail.equals(itemsForComments.get(position-1).getEmail())) {
                    return true;
                }
            }
        }
        else{
            if (userEmail.equals(items.get(position).getEmail())) {
                return true;
            }
        }
        return false;
    }



    // Delete post OR comment, and because we have fake comment (can't write to JSON) we deal with that also.
    @Override
    public void onDeleteClick(int position) {
        if(viewingComments){
            if (position==0)
                return;
            else{
                if(testCaseAddComment!= null && itemsForComments.get(position-1).getEmail()== testCaseAddComment.getEmail())
                    testCaseAddComment=null;
                itemsForComments.remove(position-1);
                myAdapterForComments.notifyItemRemoved(position-1);
            }
        }
        else {
            items.remove(position);
            myAdapter.notifyItemRemoved(position);
        }
    }

    // When we click the "add comment" button, very similar to "addPostBTN.setOnClickListener"
    @Override
    public void onAddCommentClick(int position) {
        Bundle extras = getIntent().getExtras();
        Intent myIntent = new Intent(PostActivity.this, AddCommentActivity.class);
        // Get Uri from Intent
        Uri userImage=getIntent().getData();
        myIntent.setData(userImage);
        myIntent.putExtra("firstname", extras.getString("firstname"));
        myIntent.putExtra("email", extras.getString("email"));
        startActivity(myIntent);
    }

    // When we click the "edit" button (the pencil) , also very similar to "addPostBTN.setOnClickListener"
    @Override
    public void onEditPostClick(int position) {
        if(viewingComments)
        {
            Bundle extras = getIntent().getExtras();
            Intent myIntent = new Intent(PostActivity.this, EditMessageActivity.class);
            // Get Uri from Intent
            Uri userImage = getIntent().getData();
            myIntent.setData(userImage);
            myIntent.putExtra("firstname", extras.getString("firstname"));
            myIntent.putExtra("email", extras.getString("email"));
            myIntent.putExtra("message", itemsForComments.get(position-1).getMessage());
            myIntent.putExtra("position", position-1);
            myIntent.putExtra("title","Edit comment");
            myIntent.putExtra("indicate","comment");
            startActivity(myIntent);
        }
        else {
            Bundle extras = getIntent().getExtras();
            Intent myIntent = new Intent(PostActivity.this, EditMessageActivity.class);
            // Get Uri from Intent
            Uri userImage = getIntent().getData();
            myIntent.setData(userImage);
            myIntent.putExtra("firstname", extras.getString("firstname"));
            myIntent.putExtra("email", extras.getString("email"));
            myIntent.putExtra("message", items.get(position).getMessage());
            myIntent.putExtra("position", position);
            myIntent.putExtra("title","Edit post");
            myIntent.putExtra("indicate","post");
            myIntent.putExtra("postImage", items.get(position).getPostPhoto().toString());
            startActivity(myIntent);

        }
    }

    // When click share - open sharing menu , after user chooses where to share, we also send a message
    // If for example user will decide to share via Whatsapp, it will automatically write "Share from tali social media app"
    // To the whatsapp user he chose to share with. this can later be changed to something more meaningful like the message in the post that the user clicked share on.
    @Override
    public void onShareClick(int position) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Share from tali social media app");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


}