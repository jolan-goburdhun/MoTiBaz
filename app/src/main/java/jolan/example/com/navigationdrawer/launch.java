package jolan.example.com.navigationdrawer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class launch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        Button next = (Button) findViewById(R.id.button);
        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(launch.this, MainActivity.class);
                startActivity(intent);
            }
        });

        TextView ad = (TextView) findViewById(R.id.text);
        ad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(launch.this, advertiser.class);
                startActivity(intent);
            }
        });

    }
}
