package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2015-04-28.
 */

public class ChatroomActivity extends Activity {
    private static final String TAG = ChatroomActivity.class.getName();
    private static String sUserId;
    public static final String USER_ID_KEY = "userId";
    private TextView chatroomTextView;
    private EditText etMessage;
    private Button btSend;

    private ListView lvChat;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 5000;
    private Handler handler = new Handler();

    private String chatName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register my parse model
        ParseObject.registerSubclass(Message.class);
        setContentView(R.layout.activity_chatroom);
        // User login

        if(ChatMainActivity.str==""){
            chatName = "For Anyone";
        }else{
            chatName = ChatMainActivity.str;
        }
        Log.d("CHATNUM", chatName+", "+ChatMainActivity.str);

        chatroomTextView = (TextView) findViewById(R.id.chatroomtextView);
        chatroomTextView.setText("ChatName: "+ chatName);
        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            login();
        }
        // Run the runnable object defined every 100ms
        handler.postDelayed(runnable, 1000);
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run(){
            refreshMessages();
            handler.postDelayed(this, 60000);
        }
    };

    private void refreshMessages(){
        receiveMessage();
    }

    protected void onResume(){
        //Query messages from Parse
        super.onResume();

    }


    // Get the userId from the cached currentUser object
    private void startWithCurrentUser() {
        sUserId = ParseUser.getCurrentUser().getObjectId();
        setupMessagePosting();
    }

    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    private void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Anonymous login failed: " + e.toString());
                } else {
                    startWithCurrentUser();
                }
            }
        });
    }

    // Setup button event handler which posts the entered message to Parse
    private void setupMessagePosting(){
        // Find the text field and button
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);
        mMessages = new ArrayList<Message>();
        mAdapter = new ChatListAdapter(ChatroomActivity.this, sUserId, mMessages);
        lvChat.setAdapter(mAdapter);
        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String body = etMessage.getText().toString();
                // Use Message model to create new messages now

                Message message = new Message();
                message.setUserId(sUserId);
                message.setBody(body);
                message.setChatName(chatName);

//                ParseObject message = new ParseObject("Message");
//                message.put(USER_ID_KEY, sUserId);
//                message.put("body", body);
//                message.put("ChatName", chatName);


                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        receiveMessage();
                        Toast.makeText(ChatroomActivity.this, "Successfully created message on Parse", Toast.LENGTH_SHORT).show();
                    }
                });
                etMessage.setText("");
            }
        });
    }

    private void receiveMessage(){
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery("Message");
        query = query.whereMatches("ChatName", chatName);
        // Configure limit and sort order

        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Message>(){
            public void done(List<Message> messages, ParseException e){
                if (e == null){
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    lvChat.invalidate(); // redraw listview
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });


    }


}