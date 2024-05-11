package hu.mobilalk.allasportal;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;

public class JobDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView longDescriptionTextView = findViewById(R.id.longDescriptionTextView);
        TextView locationTextView = findViewById(R.id.locationTextView);
        ImageView imageView = findViewById(R.id.imageView);
        RatingBar ratingBar = findViewById(R.id.ratingBar);


        titleTextView.setText(getIntent().getStringExtra("TITLE"));
        longDescriptionTextView.setText(getIntent().getStringExtra("LONG_DESCRIPTION"));
        locationTextView.setText(getIntent().getStringExtra("LOCATION"));
        ratingBar.setRating(getIntent().getFloatExtra("RATING", 0));

        int imageResId = getIntent().getIntExtra("IMAGE_RESOURCE", 0);
        if (imageResId != 0) {
            imageView.setImageResource(imageResId);
        } else {
            imageView.setImageResource(R.drawable.default_image);
        }

        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

    }
}
