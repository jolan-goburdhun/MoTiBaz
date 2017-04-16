package jolan.example.com.navigationdrawer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class InfoWindow extends AppCompatActivity {

    private TextView name;
    private TextView desc;
    private TextView cat;
    private ImageView image;
    private Button report;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_window);

        name = (TextView) findViewById(R.id.name);
        desc = (TextView) findViewById(R.id.desc);
        cat = (TextView) findViewById(R.id.cat);
        image = (ImageView) findViewById(R.id.image);
        report = (Button) findViewById(R.id.report);

        String nameString = returnValueFromBundles (MainActivity.NAME);
        String descString = returnValueFromBundles (MainActivity.DESCRIPTION);
        String catString = returnValueFromBundles (MainActivity.CATEGORY);
        String URLString = returnValueFromBundles (MainActivity.IMAGE_URL);
        final String pushID = returnValueFromBundles(MainActivity.PUSH_ID);

        name.setText(nameString);
        desc.setText(descString);
        cat.setText(catString);


        Picasso.with(getApplicationContext())
                .load(URLString)
                .into(image);

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("message/rfc822");
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"jolan.goburdhun@uom.umail.ac.mu"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Reporting a service");
                email.putExtra(Intent.EXTRA_TEXT, pushID);
                try {
                    startActivity(Intent.createChooser(email, "Choose an email client"));
                } catch (android.content.ActivityNotFoundException e){
                    Toast.makeText(InfoWindow.this, "There are no email clients installed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String returnValueFromBundles(String key) {
        Bundle inBundle = getIntent().getExtras();
        String returnedValue = inBundle.get(key).toString();
        return returnedValue;
    }
}
