package com.example.ahmed.googlelogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final int RC_SING_IN = 9001;
    GoogleApiClient mGoogleApiClient;
    SignInButton signInButton;
    Button btn_Sing_out;
    private TextView textView;
    private ImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure Google Sign In
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

        textView = (TextView) findViewById(R.id.textView1);
        imgAvatar = (ImageView) findViewById(R.id.img_avatar);
        signInButton = (SignInButton) findViewById(R.id.btn_sign_in);
        btn_Sing_out = (Button) findViewById(R.id.btn_Sing_out);
        signInButton.setOnClickListener(this);
        btn_Sing_out.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                singIn();
                break;
            case R.id.btn_Sing_out:
                singOut();
                break;
        }
    }

    private void singIn() {
        Intent singInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(singInIntent, RC_SING_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SING_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        // Log.d(TAG, "handleSignInResult : " + result.isSuccess());
        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();
            textView.setText("Name : \n" + acct.getDisplayName() + "\n" + "\n" + "Email : \n" + acct.getEmail());

            try {
                String PhotoUrl = acct.getPhotoUrl().toString();
                Picasso.with(MainActivity.this).load(PhotoUrl).into(imgAvatar);
            } catch (Exception e) {

            }
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    private void singOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Toast.makeText(MainActivity.this, "singOut", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
