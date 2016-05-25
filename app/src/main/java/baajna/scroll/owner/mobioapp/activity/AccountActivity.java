package baajna.scroll.owner.mobioapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.adapter.FragmentImageAdapterGetStarted;
import baajna.scroll.owner.mobioapp.utils.CommonFunc;
import baajna.scroll.owner.mobioapp.utils.Globals;
import baajna.viewpagerindicator.CirclePageIndicator;
import baajna.viewpagerindicator.PageIndicator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


//First Activity of Sparkle For Login or SignUp

public class AccountActivity extends AppCompatActivity {

    public static int EXIT = 0;
    public static int STAY = 1;
    private CallbackManager callbackManager;
    private TextView info;
    private LoginButton loginButton;
    FragmentImageAdapterGetStarted mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=this;
        CommonFunc.savePref(context, Globals.GET_START,"get_start");

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.lay_account);
        mAdapter = new FragmentImageAdapterGetStarted(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator = indicator;
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
        // indicator.setBackgroundColor(0xFF669966);
        indicator.setRadius(7 * density);
        indicator.setPageColor(0xFFCCCCCC);
        // indicator.setFillColor(0x88CC0066);
        indicator.setFillColor(0xFF669966);
        indicator.setStrokeColor(0xFF003300);
        indicator.setStrokeWidth(2 * density);

        info = (TextView) findViewById(R.id.info);
        loginButton = (LoginButton) findViewById(R.id.facebookLogin);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                info.setText("User ID:  " +loginResult.getAccessToken().getUserId());

                startActivity(new Intent(AccountActivity.this, SplashActivity.class));
            }

            @Override
            public void onCancel() {
                info.setVisibility(View.VISIBLE);
                info.setText("Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setVisibility(View.VISIBLE);
                info.setText("Login attempt failed.");
            }
        });

    }

    public void startAccount(View view) {
        Intent intent = new Intent(AccountActivity.this, SplashActivity.class);
        startActivityForResult(intent, STAY);
    }

    public void loginAccount(View view) {
        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
        startActivityForResult(intent, STAY);
    }

    public void signupAccount(View view) {
        Intent intent = new Intent(AccountActivity.this, SignupActivity.class);
        startActivityForResult(intent, STAY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == EXIT) {
            finish();
        }

    }
}
