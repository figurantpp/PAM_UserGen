package com.example.usergen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;

    Button search;

    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries, android.R. layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        search = findViewById(R.id.search_on_api_button);

        title = findViewById(R.id.random_text);


        Animation updown = AnimationUtils.loadAnimation(this, R.anim.up_down);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.fade);

        search.setAnimation(updown);

        title.setAnimation(animation1);

        search.setOnClickListener(this::show);

    }

    public void show(View view)
    {
       Intent intent = new Intent(this, ShowResultsActivity.class);
       startActivity(intent);
    }
}