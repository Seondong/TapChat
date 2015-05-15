package com.parse.starter;

/**
 * Created by user on 2015-04-29.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class IntroActivity extends Activity implements View.OnTouchListener{
    /** Called when the activity is first created. */
    ViewFlipper flipper;
    float xAtDown;  //when you push
    float xAtUp;  //when you push off
    int count = 0; // to notify first, last screen

    Button introButton;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        flipper = (ViewFlipper)findViewById(R.id.flipper);
        flipper.setOnTouchListener(IntroActivity.this);


        Button introButton = (Button) findViewById(R.id.introbutton);

        introButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ParseStarterProjectActivity.class);
                startActivity(intent);
            }
        });

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                Intent intent = new Intent(IntroActivity.this, ParseStarterProjectActivity.class);
//                startActivity(intent);
//                // 뒤로가기 했을경우 안나오도록 없애주기 >> finish!!
//                finish();
//            }
//        }, 7000);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event){
        if(v != flipper){
            return false;
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN){  // Starting point
            xAtDown = event.getX();   // Save pushed point.
        }else if(event.getAction() == MotionEvent.ACTION_UP){ // End point
            xAtUp = event.getX();
            if(xAtDown > xAtUp){  //Left to right
                flipper.setInAnimation(AnimationUtils.loadAnimation(IntroActivity.this, R.animator.push_left_in));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(IntroActivity.this, R.animator.push_left_out));
                count++;
                if(count < 4) {
                    flipper.showNext(); //Turn into next page
                }else{
                    Toast.makeText(this, "Welcome to TapChat.", Toast.LENGTH_SHORT).show();
                    count--;
                }
            }else if(xAtDown < xAtUp){  //Right to left
                flipper.setInAnimation(AnimationUtils.loadAnimation(IntroActivity.this, R.animator.push_left_in));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(IntroActivity.this, R.animator.push_left_out));
                count--;
                if(count >- 4) {
                    flipper.showNext(); //Turn into previous page
                }else{
                    Toast.makeText(this, "Click the start button.", Toast.LENGTH_SHORT).show();
                    count++;
                }
            }
        }
        return true;
    }
}
