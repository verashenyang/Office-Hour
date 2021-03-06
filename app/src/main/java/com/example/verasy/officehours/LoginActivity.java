package com.example.verasy.officehours;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "iBCljUwBsGXfqfij8ez3SP2s2";
    private static final String TWITTER_SECRET = "65pCSOUzsQRDdYnTyCgSaRpbLgUu7stIrLWDlrLmp12filFi0m";


    private TwitterLoginButton loginButton;
    private Button guestButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_login);

        guestButton = (Button)findViewById(R.id.btnGuestLogin);

        guestButton.getBackground().setColorFilter(0xFF5baaf4, PorterDuff.Mode.MULTIPLY);
        // setup a dummy login for guests who do not use twitter.  It will send us to search instead
        // of the start page for classes
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SearchActivity.class);
                long dummyId = 0;
                i.putExtra("user", dummyId);
                i.putExtra("type", "classes");
                startActivity(i);
            }
        });

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;

                long userId = result.data.getUserId();

                // Get reference to database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                // Get reference to table dictionary
                DatabaseReference userRef = databaseReference.child("users").child(Long.toString(result.data.getUserId()));

                userRef.child("name").setValue(result.data.getUserName());

                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MyClassesActivity.class);
                // TODO: replace the term with a method to get the specific user's classes
                long user = result.data.getUserId();
                Log.e("test", "Login User is "+user);
                intent.putExtra("term", "");
                intent.putExtra("type", "classes");
                intent.putExtra("user", user);
                startActivity(intent);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
