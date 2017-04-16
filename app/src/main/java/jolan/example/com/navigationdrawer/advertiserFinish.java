package jolan.example.com.navigationdrawer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static jolan.example.com.navigationdrawer.advertiseService1.Category;
import static jolan.example.com.navigationdrawer.advertiseService1.Description;
import static jolan.example.com.navigationdrawer.advertiseService1.Name;
import static jolan.example.com.navigationdrawer.advertiseService2.downloadUrl;
import static jolan.example.com.navigationdrawer.advertiseService2.downloads;
import static jolan.example.com.navigationdrawer.advertiserMap.lat;
import static jolan.example.com.navigationdrawer.advertiserMap.lng;

public class advertiserFinish extends AppCompatActivity implements View.OnClickListener{


    private Button advertise;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    //private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertiser_finish);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        advertise = (Button) findViewById(R.id.advertise);
        advertise.setOnClickListener(this);

    }

    private void saveServiceDetails(){
        adService1 adService1 = new adService1(Name, Description, Category, downloadUrl, lat, lng,
                firebaseAuth.getCurrentUser().getUid().toString());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving information..., please wait");
        progressDialog.show();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String key = databaseReference.child(user.getUid()).push().getKey();
        databaseReference.child("Advertiser").child(key).setValue(adService1)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Information saved...",
                                    Toast.LENGTH_LONG).show();
                            //startActivity(new Intent(getApplicationContext(), SecondFragmentAd.class));
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == advertise) {
            saveServiceDetails();
        }
    }
}
