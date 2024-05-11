package hu.mobilalk.allasportal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ExperimentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment);
    }

    /**
     *  Megnyit egy adott weboldalt
     * @param view
     */
    public void openWebsite(View view){
        EditText website = findViewById(R.id.passwordEditText);
        String url = website.getText().toString();

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * Megnyitja a keresett lokációt
     * @param view
     */
    public void openLocation(View view){
        EditText location = findViewById(R.id.passwordEditText);
        String url = location.getText().toString();

        Uri uri = Uri.parse("geo:0,0?q="+url);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }

    public void shareText(View view){
        EditText sharableText = findViewById(R.id.passwordEditText);
        String textToShare = sharableText.getText().toString();

        ShareCompat.IntentBuilder
                .from(this)
                .setType("text/plain")
                .setChooserTitle("Mobil alkfejl megosztas")
                .setText(textToShare)
                .startChooser();
    }

}