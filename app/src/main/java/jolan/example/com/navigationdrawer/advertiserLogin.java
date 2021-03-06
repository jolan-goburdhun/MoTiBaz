package jolan.example.com.navigationdrawer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class advertiserLogin extends AppCompatActivity implements View.OnClickListener{

    private Button registerBtn;
    private Button registeredBtn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertiser_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), advertiser.class));
        }

        registerBtn = (Button) findViewById(R.id.registerBtn);
        registeredBtn = (Button) findViewById(R.id.registeredBtn);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        registerBtn.setOnClickListener(this);
        registeredBtn.setOnClickListener(this);


    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering, please wait");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), advertiserDetails.class));
                        }else{
                            if(!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                } catch(FirebaseAuthWeakPasswordException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(advertiserLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(advertiserLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                } catch(FirebaseAuthUserCollisionException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(advertiserLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                } catch(Exception e) {
                                    Log.e("exception", e.getMessage());
                                }
                            }
                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {
        if (v == registerBtn){
            registerUser();
        }

        if (v == registeredBtn){
            startActivity(new Intent(this, advertiserLogin2.class));
        }

    }
}
