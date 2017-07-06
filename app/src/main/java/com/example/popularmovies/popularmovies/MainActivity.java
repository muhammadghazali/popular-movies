package com.example.popularmovies.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView helloTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloTextView = (TextView) findViewById(R.id.hello_world);
    }

    public void goToMovieDetailActivity(View view) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        startActivity(intent);
    }

}
