package baajna.scroll.owner.mobioapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.connection.InternetConnectivity;
import baajna.scroll.owner.mobioapp.utils.SparkleApp;
import baajna.scroll.owner.mobioapp.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText editText_password;
    private EditText editText_email;

    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lay_account_login);

        initViews();
        setListener();
        loadData();
    }

    private void initViews() {

        editText_email = (EditText) findViewById(R.id.edittext_loginEmail);
        editText_password = (EditText) findViewById(R.id.edittext_loginPassword);

        btnLogin = (Button) findViewById(R.id.btn_loginBtn);
        btnSignUp = (Button) findViewById(R.id.btn_signupRequest);
    }

    private void setListener() {
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    private void loadData() {

    }

    public void goToMain(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        setResult(AccountActivity.EXIT);
        finish();
    }

    public void loginAccount(View view) {
        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
        startActivity(intent);
        setResult(AccountActivity.STAY);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(AccountActivity.STAY);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_loginBtn:
                if (!editText_email.getText().toString().trim().equalsIgnoreCase("")
                        && !editText_password.getText().toString().trim().equalsIgnoreCase("")) {
                    if (SparkleApp.getInstance().isEmailValid(editText_email.getText().toString().trim())) {
                        loginData(editText_email.getText().toString().trim(),
                                editText_password.getText().toString().trim());
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid e-mail address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "E-mail and Password fields are required", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_signupRequest:
                Intent singupIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(singupIntent);

            default:
                break;
        }
    }

    private void loginData(final String email, final String password) {
        if (InternetConnectivity.isConnectedToInternet(LoginActivity.this)) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("user_email", email);
            params.put("password", password);
            params.put("device_id", "1");
            client.post(Urls.BASE_URL + "index.php/api/logins", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.d("Sajal", response.toString());
                    try {
                        parseLoginData(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.e("Jewel", responseString);
                }
            });
        } else {
            SparkleApp.getInstance().openAlert("Test", LoginActivity.this);
        }
    }

    private void parseLoginData(JSONObject jData) throws JSONException {
        int logindata = 0;
        JSONObject jDataObj = new JSONObject(jData.toString());
        logindata = jDataObj.getInt("message");

        if (logindata == 1 ) {
            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
            Toast.makeText(getApplicationContext(), "You are successfully logged in. Welcome to Sync", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, SplashActivity.class));
            //SparkleApp.getInstance().setMoUser(info);
        } else {

            Toast.makeText(getApplicationContext(), "Wrong username/Password", Toast.LENGTH_SHORT).show();
            //SparkleApp.getInstance().setMoUser(null);
        }

    }

}

