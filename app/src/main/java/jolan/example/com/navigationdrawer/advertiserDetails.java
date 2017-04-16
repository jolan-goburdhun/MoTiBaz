package jolan.example.com.navigationdrawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class advertiserDetails extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private EditText fullName;
    private EditText companyName;
    private Button save;

    public static final String advertiserData = "advertiser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertiser_details);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        fullName = (EditText) findViewById(R.id.fullName);
        companyName = (EditText) findViewById(R.id.companyName);
        save = (Button) findViewById(R.id.saveDetails);

        save.setOnClickListener(this);

    }



    private void saveAdvertiserDetails(){

        String nameString = fullName.getText().toString().trim();
        String companyString = companyName.getText().toString().trim();

        if (TextUtils.isEmpty(nameString)){
            Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences.Editor editor = getSharedPreferences(advertiserData, 0).edit();
            editor.putString("name", nameString);
            editor.putString("company", companyString);
            editor.apply();
            Toast.makeText(this, "Information saved...", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), advertiser.class));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == save){
            saveAdvertiserDetails();
        }
    }
}
