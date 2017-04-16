package jolan.example.com.navigationdrawer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class InfoWindow2 extends AppCompatActivity {

    private TextView name;
    private TextView desc;
    private TextView cat;
    private ImageView image;
    private Button report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_window2);

        name = (TextView) findViewById(R.id.name);
        desc = (TextView) findViewById(R.id.desc);
        cat = (TextView) findViewById(R.id.cat);
        image = (ImageView) findViewById(R.id.image);
        report = (Button) findViewById(R.id.report);

        String nameString = returnValueFromBundles (allLocationsMap.NAME);
        String descString = returnValueFromBundles (allLocationsMap.DESCRIPTION);
        String catString = returnValueFromBundles (allLocationsMap.CATEGORY);
        String URLString = returnValueFromBundles (allLocationsMap.IMAGE_URL);
        final String pushID = returnValueFromBundles(allLocationsMap.PUSH_ID);

        name.setText(nameString);
        desc.setText(descString);
        cat.setText(catString);


        Picasso.with(getApplicationContext())
                .load(URLString)
                .into(image);

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailTo","jolan.goburdhun@uom.umail.ac.mu",null));
                email.putExtra(Intent.EXTRA_SUBJECT, "Reporting a service");
                email.putExtra(Intent.EXTRA_TEXT, pushID);

                //email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an email client"));
            }
        });
    }

    private String returnValueFromBundles(String key) {
        Bundle inBundle = getIntent().getExtras();
        String returnedValue = inBundle.get(key).toString();
        return returnedValue;
    }

}
