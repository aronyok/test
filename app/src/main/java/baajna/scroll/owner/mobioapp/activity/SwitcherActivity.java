package baajna.scroll.owner.mobioapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import baajna.scroll.owner.mobioapp.utils.CommonFunc;
import baajna.scroll.owner.mobioapp.utils.Globals;


/**
 * Created by Jewel on 10/29/2015.
 */
public class SwitcherActivity extends AppCompatActivity {
    private Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isRegistered()&& isStart()){
            intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        else if(isStart()){
            intent=new Intent(this,SplashActivity.class);
            startActivity(intent);
        }
        else
        {
            intent=new Intent(this,AccountActivity.class);
            startActivity(intent);
        }
        finish();
    }
    private boolean isRegistered(){
        String myToken= CommonFunc.getPref(this, Globals.USER_LOGIN);
        if(!myToken.equals("na")){
            return true;
        }

        return false;
    }
    private boolean isStart(){
        String token= CommonFunc.getPref(this, Globals.GET_START);
        if(!token.equals("na")){
            return true;
        }

        return false;
    }
}
