package com.example.movieapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.databinding.ActivityMovieDetailsBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.viewmodel.MovieViewModel;

import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {
    private ActivityMovieDetailsBinding binding;
    private MovieViewModel movieViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Binding
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        Movie movie = (Movie) getIntent().getSerializableExtra("MOVIE");
        if (movie == null) {
            Toast.makeText(this, "Error loading movie", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch details using IMDb ID
        movieViewModel.fetchMovieDetail(movie.getImdbID());


        movieViewModel.getMovieDetailsLiveData().observe(this, movieDetails -> {
            if (movieDetails != null) {
                binding.textMovieTitle.setText(movieDetails.getTitle());
                binding.textMovieDetails.setText(movieDetails.getYear() + " | " + movieDetails.getRuntime() + " | " + movieDetails.getGenre() + " | " + "★ " + movieDetails.getImdbRating() + " Ratings");
                binding.textMovieDirector.setText("Director - "+movieDetails.getDirector());
                binding.textMovieDescription.setText(movieDetails.getDescription());
                Glide.with(this).load(movieDetails.getPoster()).into(binding.imageMoviePoster);

            }
        });

        // Observe errors
        movieViewModel.getError().observe(this, error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show());


        // Set up Back button listener
        binding.btnBack.setOnClickListener(view -> finish());
    }
}
