package baajna.scroll.owner.mobioapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import baajna.scroll.owner.mobioapp.connection.InternetConnectivity;
import baajna.scroll.owner.mobioapp.utils.SparkleApp;
import baajna.scroll.owner.mobioapp.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import com.mobioapp.baajna.R;

public class SignupActivity extends Activity implements View.OnClickListener {

    private AutoCompleteTextView autoCompleteTextView_email;
    private EditText editText_password;
    private EditText editText_confirm_password;

    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lay_account_registration);

        initViews();
        setListener();
        loadData();
    }

    private void initViews() {

        autoCompleteTextView_email = (AutoCompleteTextView) findViewById(R.id.auto_complete_textview_signupEmail);
        editText_password = (EditText) findViewById(R.id.edittext_password);
        editText_confirm_password = (EditText) findViewById(R.id.edittext_confirm_password);

        btnSignup = (Button) findViewById(R.id.btn_signup);
    }

    private void setListener() {
        btnSignup.setOnClickListener(this);
        ((Button) findViewById(R.id.btn_existLogin)).setOnClickListener(this);
    }

    private void loadData() {

    }

    public void goToMain(View view) {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        setResult(AccountActivity.EXIT);
        finish();
    }

    public void loginAccount(View view) {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
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
            case R.id.btn_signup:
                if (!editText_password.getText().toString().trim().equalsIgnoreCase("")
                        && !autoCompleteTextView_email.getText().toString().trim().equalsIgnoreCase("")
                        && !editText_confirm_password.getText().toString().trim().equalsIgnoreCase("")) {
                    if (editText_password.getText().toString().length() >= 6) {
                        if (editText_password.getText().toString().equals(editText_confirm_password.getText().toString())) {
                            if (SparkleApp.getInstance().isEmailValid(autoCompleteTextView_email.getText().toString().trim())) {
                                signupData(autoCompleteTextView_email.getText().toString().trim(),
                                        editText_password.getText().toString().trim());
                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid e-mail address", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Confirm Password and Password do not match", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "E-mail, Password, Confirm Password fields are required", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btn_existLogin:
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
                break;

            default:
                break;
        }
    }

    private void signupData(String email, String password) {

        if (InternetConnectivity.isConnectedToInternet(SignupActivity.this)) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("user_email", email);
            params.put("password", password);
            params.put("device_id", "1");
            client.post(Urls.BASE_URL +"index.php/api/create_user", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    Log.d("Sajal", response.toString());
                    try {
                        parseSignUpData(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /*if (statusCode == 200) {
                        //startActivity(new Intent(SignupActivity.this, MainActivity.class));
                        Toast.makeText(getApplicationContext(), "Your are successfully registered. Welcome to Sparkle", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_SHORT).show();
                    }

                    Log.d("SIGNUPACTIVITY_STATUS", String.valueOf(statusCode));*/

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.e("Sajal", responseString);
                }
            });
        }

        else {
            SparkleApp.getInstance().openAlert("Test", SignupActivity.this);
        }
    }

    private void parseSignUpData(JSONObject jData) throws JSONException {

        int singUpdata = 0;
        JSONObject jDataObj = new JSONObject(jData.toString());
        singUpdata = jDataObj.getInt("message");

        if (singUpdata == 1) {
            Toast.makeText(getApplicationContext(), "Your are successfully registered. Welcome to Sync", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));

        } else {
            //SparkleApp.getInstance().setMoUser(null);
            Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_SHORT).show();
        }


    }
}

