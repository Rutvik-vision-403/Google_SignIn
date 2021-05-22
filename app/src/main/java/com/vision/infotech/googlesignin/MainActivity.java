package com.vision.infotech.googlesignin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private GoogleSignInClient signInClient;
    private final int SIGN_IN_REQ_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // if user already sign in using google account return not null otherwise return null
        if (getLastSignInAccount(this) != null){
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent); // start user activity if user already login with gmail
        }

        SignInButton googleSignIn = findViewById(R.id.google_sign_in);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                signInClient = GoogleSignIn.getClient(MainActivity.this, signInOptions);

                Intent signInIntent = signInClient.getSignInIntent();
                startActivityForResult(signInIntent,SIGN_IN_REQ_CODE);
            }
        });

    }

    // return null if user is not sign in previously or return last account
    private GoogleSignInAccount getLastSignInAccount(Context context) {
        return GoogleSignIn.getLastSignedInAccount(context);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_REQ_CODE){
            if (data != null){

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    String name = account.getDisplayName();
                    String email = account.getEmail();

                    // image can be null if user not set in their account
                    String imageUri = "";
                    if (account.getPhotoUrl() != null){
                        imageUri = account.getPhotoUrl().toString();
                    }
                    String userID = account.getId();

                    Log.d(TAG, "onActivityResult: user name"+name);
                    Log.d(TAG, "onActivityResult: user email"+email);
                    Log.d(TAG, "onActivityResult: user image uri"+imageUri);
                    Log.d(TAG, "onActivityResult: user id"+userID);

                    Intent intent = new Intent(MainActivity.this,UserActivity.class);
                    startActivity(intent);

                } catch (ApiException e) {
                    Log.d("TAG", "handleSignInResult: "+e.getMessage());
                }
            }
        }
    }

}