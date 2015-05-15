package com.parse.starter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseStarterProjectActivity extends Activity {
    private BackPressCloseHandler backPressCloseHandler;
    /** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        backPressCloseHandler = new BackPressCloseHandler(this);
		ParseAnalytics.trackAppOpenedInBackground(getIntent());
//        ParseUser currentUser = ParseUser.getCurrentUser();

        Button btn = (Button) findViewById(R.id.mainsignupbutton);
        Button btn2 = (Button) findViewById(R.id.mainloginbutton);
        final EditText et1 = (EditText) findViewById(R.id.mainideditText);
        final EditText et2 = (EditText) findViewById(R.id.mainpweditText);
//        if(currentUser != null){
//            Intent intent = new Intent(getApplicationContext(), ChatMainActivity.class);
//            startActivity(intent); // do stuff with the user
//            Toast.makeText(ParseStarterProjectActivity.this, "Hello, "+currentUser.getUsername()+"!", Toast.LENGTH_SHORT).show(); // Welcome Toast
//        }else{//show login screen

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(et1.getText());
                String password = String.valueOf(et2.getText());;
                ParseUser.logInInBackground(id, password, new LogInCallback(){
                    public void done(ParseUser user, ParseException e){
                        if (user != null){
                            Intent intent = new Intent(getApplicationContext(), ChatMainActivity.class);
                            startActivity(intent);
                        }else{
                            new AlertDialog.Builder(ParseStarterProjectActivity.this)
                                    .setTitle("Error")
                                    .setMessage("Please check your ID/Password")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                            //Signup failed
                        }
                    }
                });
            }
        });
    }
//    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}
