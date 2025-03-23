package com.example.movieapp.view;

import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieapp.Interfaces.MovieClickListener;
import com.example.movieapp.R;
import com.example.movieapp.databinding.ActivityMainBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.utils.ApiClient;
import com.example.movieapp.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieClickListener {

    // Attributes
    ActivityMainBinding binding;
    List<Movie> movieList;
    MovieAdapter movieAdapter;
    MovieViewModel movieViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        movieList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(getApplicationContext(), movieList);
        binding.recyclerView.setAdapter(movieAdapter);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Data Observes
        movieViewModel.getMovieData().observe(this, movieData->{
            movieList.clear();
            movieList.addAll(movieData);
            movieAdapter.notifyDataSetChanged();
        });

        // Error updates
        movieViewModel.getError().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });


        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Search Now", Toast.LENGTH_SHORT).show();

                String searchTxt = binding.editTextSearch.getText().toString();

                Toast.makeText(MainActivity.this, searchTxt, Toast.LENGTH_SHORT).show();

                movieViewModel.searchMovies(searchTxt);

            }
        });

        movieAdapter.setClickListener(this);

    }


    // Click on Items
    @Override
    public void onClick(View v, int pos) {
        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        intent.putExtra("MOVIE", movieList.get(pos));
        startActivity(intent);
    }
}