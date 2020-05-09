package com.krutarth07.sos2;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bcgdv.asia.lib.fanmenu.FanMenuButtons;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.nispok.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import im.delight.android.location.SimpleLocation;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class MainActivity extends Activity {

    private static MainActivity inst;
    FanMenuButtons sub;

    Button send;
    Button cam, mic, vid;
    TextView loc;
    ListView lv;

    ImageView pic;
    VideoView vidv;

    ImageButton whatsapp, fb, twitter;

    SmsManager sms;
    SimpleLocation location;
    Geocoder geocoder;

    int tutorial;

    List<Address> addresses;
    double latitude, longitude;
    Thread thr;
    String strPhone;
    String strMessage;
    String link;
    String[] descriptionData = {"Tutorial", "Set Contacts", "Panic", "Sent"};

    String[] displayList;
    ArrayAdapter<String> adapter;
    List<String> list;
    SharedPreferences pref;

    StateProgressBar stateProgressBar;

    String address;
    String city;
    String state;
    String country;
    String postalCode;
    String a;
    int j,request=0;
    String requestno;


    public static MainActivity instance() {
        return inst;
    }
    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().setIcon(R.drawable.app);
sub= (FanMenuButtons) findViewById(R.id.myFABSubmenu);

        sms = SmsManager.getDefault();

        cam = (Button) findViewById(R.id.camera);
        mic = (Button) findViewById(R.id.mic);
        vid = (Button) findViewById(R.id.video);

        pic = (ImageView) findViewById(R.id.pic);
        vidv = (VideoView) findViewById(R.id.videoView);

        whatsapp = (ImageButton) findViewById(R.id.whatsapp);
        fb = (ImageButton) findViewById(R.id.fb);
        twitter = (ImageButton) findViewById(R.id.twi);

        send = (Button) findViewById(R.id.send);
        loc = (TextView) findViewById(R.id.loc);
        lv = (ListView) findViewById(R.id.lv);
        geocoder = new Geocoder(this, Locale.getDefault());

        pref = getSharedPreferences("details", MODE_PRIVATE);


        send.setBackgroundResource(R.drawable.norm);

        stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);



///////////////////////////tutorial///////////////////////////////////////

        tutorial = pref.getInt("tutorial", 0);

        if(tutorial!=1) {

            ///////////////////new view////////////////////////
            new MaterialTapTargetPrompt.Builder(MainActivity.this)
                    .setTarget(findViewById(R.id.settings))
                    .setPrimaryText("Settings")
                    .setPrimaryTextTypeface(Typeface.DEFAULT_BOLD)
                    .setSecondaryText("Tap the button to add/delete the emergency contacts.")
                    .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                        @Override
                        public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                            /////////////new view////////////////////////////////////////////////\
                            new MaterialTapTargetPrompt.Builder(MainActivity.this)
                                    .setTarget(findViewById(R.id.send))
                                    .setPrimaryText("PANIC BUTTON")
                                    .setPrimaryTextTypeface(Typeface.DEFAULT_BOLD)
                                    .setSecondaryText("Tap the button to send emergency message to selected contacts.")
                                    .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                        @Override
                                        public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                            ///////////////////new view////////////////////////
                                            new MaterialTapTargetPrompt.Builder(MainActivity.this)
                                                    .setTarget(findViewById(R.id.fb))
                                                    .setPrimaryText("SOCIAL BUTTON")
                                                    .setPrimaryTextTypeface(Typeface.DEFAULT_BOLD)
                                                    .setSecondaryText("Tap these buttons to send messages or update status.")
                                                    .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                                        @Override
                                                        public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                                            ///////////////////new view////////////////////////
                                                            new MaterialTapTargetPrompt.Builder(MainActivity.this)
                                                                    .setTarget(findViewById(R.id.camera))
                                                                    .setPrimaryText("Media Button")
                                                                    .setPrimaryTextTypeface(Typeface.DEFAULT_BOLD)
                                                                    .setSecondaryText("Tap these buttons to send image or videos as a message to selected contacts.")
                                                                    .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                                                        @Override
                                                                        public void onHidePrompt(MotionEvent event, boolean tappedTarget) {

                                                                            tutorial = 1;
                                                                            SharedPreferences.Editor edit = pref.edit();
                                                                            edit.putInt("tutorial", tutorial);
                                                                            edit.commit();
                                                                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);

                                                                        }

                                                                        @Override
                                                                        public void onHidePromptComplete() {

                                                                        }
                                                                    })
                                                                    .show();
                                                        }

                                                        @Override
                                                        public void onHidePromptComplete() {

                                                        }
                                                    })
                                                    .show();
                                        }

                                        @Override
                                        public void onHidePromptComplete() {

                                        }
                                    })
                                    .show();

                        }

                        @Override
                        public void onHidePromptComplete() {

                        }
                    })
                    .show();
        }

/////////////////////////////////////location object////////////////////////
        location = new SimpleLocation(this);
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }
///////////////////////////////Thread/////////////////////
        thr = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {

                        Thread.sleep(2000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //     getAddress();

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        thr.start();


        list = new ArrayList<String>();
        addresses = new ArrayList<Address>();

        try{  getAddress();}
        catch (Exception e){}

        link="http://maps.google.com/?q="+latitude+","+longitude+"\n";
        strMessage = pref.getString("msg", "");
        strMessage = strMessage + "\n\n" + "My Location is : " + address + ", " + city + ", " + state + ", " + country + ", " + postalCode+"\n\n"+"Google Maps : "+link;

/////////////////get Contacts/////////////////////////////

/*
        j = pref.getInt("size", 1);
        for (int i = 1; i <= j; i++) {
            a = pref.getString("contacts" + i, "");
            if (!a.equals("")) {
                list.add(a);
            }
        }
        adapter = new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_list_item_1, list);

        lv.setAdapter(adapter);
*/
        int vnt=pref.getInt("adpcount",0);
        for (int i = 0;i<vnt; ++i){
            a = pref.getString(String.valueOf(i)+"c", "");

            if (!a.equals("")){
                list.add(a);
            } else {
                break; // Empty String means the default value was returned.
            }
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,list);
        lv.setAdapter(adapter);


        if (a != "" && a != null) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        }

////////////////////////////panic button////////////////////////////////////////////////////
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendsms();

            }
        });

        toucheffect();
//////////////////////////////////camera////////////////////////////////////////
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);

                }
            }
        });

/////////////////////////////////video/////////////////////////////////////////////
        vid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, 2);

                }*/

                sub.toggleShow();

            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getAddress();
                } catch (IOException e) {

                }
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, strMessage);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getAddress();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, strMessage);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.facebook.katana");
                startActivity(sendIntent);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/*F
                try {
                    getAddress();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.twitter");
                startActivity(sendIntent);     */
            }
        });

    }     //END OF ON_CREATE////////////..................///////////////////.......................////////////////////

    private void custommenu() {

        if (sub != null) {
            sub.setOnFanButtonClickListener(new FanMenuButtons.OnFanClickListener() {
                @Override
                public void onFanButtonClicked(int index) {
                    Toast.makeText(MainActivity.this, "ters", Toast.LENGTH_SHORT).show();
                }
            });

            sub.setOnFanAnimationListener(new FanMenuButtons.OnFanAnimationListener() {
                @Override
                public void onAnimateInStarted() {
                }

                @Override
                public void onAnimateOutStarted() {
                }

                @Override
                public void onAnimateInFinished() {
                }

                @Override
                public void onAnimateOutFinished() {
                }
            });
        }


    }

    /////////////////////////////address///////////////////////////////////////
    public void getAddress() throws IOException {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        //try {
        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        //} catch (IOException e) {
        //   e.printStackTrace();
        // }
        address = addresses.get(0).getAddressLine(0);
        city = addresses.get(0).getLocality();
        state = addresses.get(0).getAdminArea();
        country = addresses.get(0).getCountryName();
        postalCode = addresses.get(0).getPostalCode();
        loc.setText(address + ", " + city + ", " + state + ", " + country + ", " + postalCode);


    }

    //////////////////////////////////effect//////////////////////////////////////////
    public void toucheffect() {

        send.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        send.setBackgroundResource(R.drawable.press);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        send.setBackgroundResource(R.drawable.norm);
                        break;
                    }
                }
                return false;
            }
        });

        cam.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        cam.setBackgroundResource(R.drawable.camera_pressed);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        cam.setBackgroundResource(R.drawable.camera);
                        break;
                    }
                }
                return false;
            }
        });

        mic.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        mic.setBackgroundResource(R.drawable.mic_pressed);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        mic.setBackgroundResource(R.drawable.mic);
                        break;
                    }
                }
                return false;
            }
        });

        vid.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        vid.setBackgroundResource(R.drawable.video_pressed);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        vid.setBackgroundResource(R.drawable.video);
                        break;
                    }
                }
                return false;
            }
        });

        custommenu();

    }

    //////////////////send sms..///////////////////////////////////////

    void sendsms(){
        if (a != "" && a != null) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
            for (int h = 0; h < lv.getCount(); h++) {

                try {

                    strPhone = String.valueOf(lv.getItemAtPosition(h));
                    Toast.makeText(MainActivity.this, ""+strPhone, Toast.LENGTH_SHORT).show();

                    sms.sendTextMessage(strPhone, null, strMessage, null, null);

                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);

                } catch (Exception e) {
                }
            }



            Toast.makeText(MainActivity.this, "Sent.", Toast.LENGTH_SHORT).show();

        } else {

            //stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);

            Snackbar.with(getApplicationContext()).color(Color.GREEN).textColor(Color.BLACK) // context
                    .text("No Contacts Selected")// text to display
                    .show(MainActivity.this);


        }
    }

    //////////////////////////display pic,video////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {

            pic.setVisibility(View.VISIBLE);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            pic.setImageBitmap(imageBitmap);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {

            vidv.setVisibility(View.VISIBLE);
            Uri videoUri = data.getData();
            vidv.setVideoURI(videoUri);
        }
    }


    ///////////////////menu////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.home).setVisible(false);

        return true;
    }

    ////////////////////////////////menu items////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {

            case R.id.settings:
                Intent i = new Intent(MainActivity.this, settingspane.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // make the device update its location
        location.beginUpdates();
        try {
            getAddress();
        }catch (Exception e) {

        }

        // ...
    }

    @Override
    protected void onPause() {
        // stop location updates (saves battery)
        location.endUpdates();

        // ...

        super.onPause();
    }

    void getMessage(String addr,String msg) {

        request=0;
        requestno=addr;


        String lockeyword;
        lockeyword = pref.getString("lockeyword","");


            for(int i=0;i<lv.getCount();i++){
                if(requestno.contains(String.valueOf(lv.getItemAtPosition(i)))) {
                    if (msg.equals(lockeyword)) {
                        request = 1;
                        //  Toast.makeText(inst, "getloc", Toast.LENGTH_SHORT).show();

                    }
                }
        }

        if(request==1){
            sms.sendTextMessage(requestno, null, strMessage, null, null);
        }

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_UP) {
                    sendsms();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    Toast.makeText(inst, "down", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }



}
