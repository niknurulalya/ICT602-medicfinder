package com.example.medicfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class Home extends AppCompatActivity {

    Button userLocation, viewMaps, comment;
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        textview = findViewById(R.id.textView);

        // Get the full name passed from the LoginActivity
        String fullName = getIntent().getStringExtra("FULL_NAME");

        // Reference to the TextView where you want to display the full name
        TextView greetUser = findViewById(R.id.textView2);

        // Display the full name or a default message if it's null
        if (fullName != null) {
            greetUser.setText("Welcome " + fullName);
        }
        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userLocation = findViewById(R.id.button1);
        viewMaps = findViewById(R.id.button3);
        comment = findViewById(R.id.button4);


        userLocation.setOnClickListener(view -> {
            // Start MapsActivity
            Intent intent = new Intent(getApplicationContext(), CurrentLocationActivity.class);
            startActivity(intent);
        });
        viewMaps.setOnClickListener(view -> {
            // Start MapsActivity
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        });
        comment.setOnClickListener(view -> {
            // Start MapsActivity
            Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
            startActivity(intent);
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId() == R.id.Share) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Please use my application - https://github.com ");
            startActivity(Intent.createChooser(shareIntent, null));

            return true;
        }
        else if(item.getItemId() == R.id.About) {

            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
        }
        else if(item.getItemId() == R.id.Logout) {

            Intent instructionIntent = new Intent(this, LoginActivity.class);
            startActivity(instructionIntent);
        }
        return false;
    }
}