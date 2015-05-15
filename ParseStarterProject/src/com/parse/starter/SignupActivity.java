package com.parse.starter;

/**
 * Created by Sundong Kim on 2015-04-27.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignupActivity extends Activity {
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        Button btn = (Button) findViewById(R.id.signuphomebutton);
        Button btn2 = (Button) findViewById(R.id.signupsignupbutton);
        final EditText et1 = (EditText) findViewById(R.id.signupideditText);
        final EditText et2 = (EditText) findViewById(R.id.signuppweditText);
        final EditText et3 = (EditText) findViewById(R.id.signuppweditText2);
        final EditText et4 = (EditText) findViewById(R.id.signupnameeditText);
        final EditText et5 = (EditText) findViewById(R.id.signupbirthdayeditText);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ParseStarterProjectActivity.class);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(et1.getText().equals("")) {
                    Toast.makeText(SignupActivity.this, "Please type valid ID", Toast.LENGTH_SHORT).show();
                }else{
                    ParseUser user = new ParseUser();
//                  user.setUsername("Sundong");
//                  user.setPassword("sundong");
//                  user.put("hobby", "swim");
                    user.setUsername(String.valueOf(et1.getText()));
                    user.setPassword(String.valueOf(et2.getText()));
                    user.put("name", String.valueOf(et4.getText()));
                    user.put("birthday", String.valueOf(et5.getText()));
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Toast.makeText(SignupActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show(); // Working
                                Intent intent = new Intent(getApplicationContext(), ParseStarterProjectActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(SignupActivity.this, "Try again", Toast.LENGTH_SHORT).show(); //Sign up didn't succeed.
                            }
                        }
                    });

                }

            }
        });

    }


}
