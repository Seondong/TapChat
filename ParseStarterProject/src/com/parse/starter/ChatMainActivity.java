package com.parse.starter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class ChatMainActivity extends Activity {

    final static String TAG = "ChatMainActivity";
    private BackPressCloseHandler backPressCloseHandler;
    NfcAdapter nfcAdapter;
    static String str=""; //Plain-Text written in NFC tag(UTF-8)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatmain);
        backPressCloseHandler = new BackPressCloseHandler(this);
        Log.d(TAG, "onCreate()");


        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter!=null && nfcAdapter.isEnabled()){
//            Toast.makeText(this, "NFC available!", Toast.LENGTH_LONG).show();
        }else{
//            Toast.makeText(this, "NFC not available :( ", Toast.LENGTH_LONG).show();
        }



        ParseObject userchatlink = new ParseObject("UserChatLink");
        userchatlink.put("userID", ParseUser.getCurrentUser().getObjectId());
        userchatlink.put("chatName", str);
        userchatlink.saveInBackground();
        Toast.makeText(this, ParseUser.getCurrentUser().getObjectId() + ", " + str, Toast.LENGTH_SHORT).show();



        Button btn = (Button) findViewById(R.id.chatmainbutton);
        Button btn2 = (Button) findViewById(R.id.chatmainbutton2);
        Button btn3 = (Button) findViewById(R.id.chatmainbutton3);

        final Button chatbtn = (Button) findViewById(R.id.button);
        final Button chatbtn2 = (Button) findViewById(R.id.button2);
        final Button chatbtn3 = (Button) findViewById(R.id.button3);
        final TextView chattv = (TextView) findViewById(R.id.textView6);
        final TextView chattv2 = (TextView) findViewById(R.id.textView8);
        final TextView chattv3 = (TextView) findViewById(R.id.textView10);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("UserChatLink");
                query = query.whereMatches("userID", ParseUser.getCurrentUser().getObjectId());
                query.orderByDescending("chatName");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        String cn1 = "";
                        String cn2 = "";
                        String cn3 = "";
                        List<String> chatnameList = new ArrayList<String>();
                        chatnameList.add(0, "For Anyone");   //Initial setup

                        for(int i=1; i<parseObjects.size()-1; i++){
                            Log.d("HOLA", i+", "+parseObjects.get(i).getString("chatName"));
                            if(!parseObjects.get(i).getString("chatName").equals(parseObjects.get(i+1).getString("chatName"))){
                                chatnameList.add(parseObjects.get(i).getString("chatName"));

                            }
                        }
                        cn1 = chatnameList.get(0);
                        cn2 = chatnameList.get(1);
                        cn3 = chatnameList.get(2);
//                        cn1 = parseObjects.get(1).getString("chatName");
//                        cn2 = parseObjects.get(2).getString("chatName");
//                        cn3 = parseObjects.get(3).getString("chatName");
                        Log.d("HOLA", cn1+", "+cn2+", "+cn3);
                        chattv.setText(cn1);
                        chattv2.setText(cn2);
                        chattv3.setText(cn3);
                        if(chattv.getText().length()>1) chatbtn.setText("Join");
                        if(chattv2.getText().length()>1) chatbtn2.setText("Join");
                        if(chattv3.getText().length()>1) chatbtn3.setText("Join");
                    }
                });
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatroomActivity.class);
                startActivity(intent);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(getApplicationContext(), ParseStarterProjectActivity.class);
                startActivity(intent);
            }
        });

        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chatbtn.getText()=="Join") {
                    str = chattv.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), ChatroomActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ChatMainActivity.this, "This chatroom is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        chatbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chatbtn.getText()=="Join") {
                    str = chattv2.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), ChatroomActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ChatMainActivity.this, "This chatroom is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        chatbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chatbtn.getText()=="Join") {
                    str = chattv3.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), ChatroomActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ChatMainActivity.this, "This chatroom is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onNewIntent(Intent intent){
        Log.d(TAG, "onNewIntent()");
        super.onNewIntent(intent);
//        Toast.makeText(this, "NFC intent received!", Toast.LENGTH_LONG).show();
    }

    protected void onResume(){
        Log.d(TAG, "onResume()");
        super.onResume();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

            Object o = tag.getId();
            //Log.d("test", o.toString());

            Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    Log.d(TAG, msgs[i].toString());

                    NdefRecord[] ndefRecords = msgs[i].getRecords();
                    byte[] payload = ndefRecords[0].getPayload();

                    try {
                        str = new String(payload, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    str = str.substring(3);
                    Log.d(TAG, str);


//                    Toast.makeText(this, "onResume() with NFC tagging : " + str, Toast.LENGTH_LONG).show();
                }
            }

        } else {
//            Toast.makeText(this, "onResume() without NFC tagging", Toast.LENGTH_LONG).show();

        }


//        Intent intent = new Intent(this, ChatMainActivity.class);
//        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        IntentFilter[] intentFilter = new IntentFilter[]{};
//
//        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);


    }

    protected void onPause(){
        Log.d(TAG, "onPause()");
        super.onPause();

//        nfcAdapter.disableForegroundDispatch(this);

    }


    protected void onDestroy(){
        Log.d(TAG, "onDestroy()");
        super.onDestroy();

//        nfcAdapter.disableForegroundDispatch(this);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_before_nfc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
