package com.dldemo.firebaselogindemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.api.model.StringList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText txtEmail;
    EditText txtPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPass = (EditText)findViewById(R.id.txtPassword);

        Button btnNewAcc = (Button) findViewById(R.id.btn_new);
        btnNewAcc.setOnClickListener(handler);
        Button btnLogon = (Button)findViewById(R.id.btn_sign);
        btnLogon.setOnClickListener(handler);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("LOGGED IN", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("LOGGED OUT", "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v) {
            //Toast.makeText(MainActivity.this, ""+v.getId(), Toast.LENGTH_SHORT).show();
            switch (v.getId()){
                case R.id.btn_sign:
                    if (!txtEmail.getText().toString().trim().equals("") || !txtPass.getText().toString().trim().equals("")){

                        //SIGNING IN
                        mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtPass.getText().toString())
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Log.d("LOGGING IN", "signInWithEmail:onComplete:" + task.isSuccessful());


                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        if (!task.isSuccessful()) {
                                            Log.w("LOGGING IN FAILED", "signInWithEmail", task.getException());
                                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }else{
                                            //Toast.makeText(MainActivity.this, ""+txtEmail.getText().toString(), Toast.LENGTH_SHORT).show();
                                            Intent iSign = new Intent(getApplicationContext(), HomeActivity.class);
                                            iSign.putExtra("email", txtEmail.getText().toString());
                                            startActivity(iSign);
                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(MainActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                    }

                break;

                case R.id.btn_new:
                    Intent iNew = new Intent(MainActivity.this, NewUserActivity.class);
                    startActivity(iNew);
                    break;
            }

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
