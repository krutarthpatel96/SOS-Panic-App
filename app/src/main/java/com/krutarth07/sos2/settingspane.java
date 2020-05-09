package com.krutarth07.sos2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by Krutarth on 11-02-2017.
 */

public class settingspane extends ActionBarActivity{

    SharedPreferences pref;
    SharedPreferences.Editor edit;
    AlertDialog.Builder alert;
    String message,lockeyword;
    int batlevel;
    ListView lv;
    String[] mode = {"Add Contacts", "Distress Message", "Remote Location Keyword","Battery Level"};
    Integer[] imageId = {
            R.drawable.cont,
            R.drawable.message,
            R.drawable.location,
            R.drawable.battery
    };

    @Override
    public void onBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingspane);

        alert = new AlertDialog.Builder(this);;
        pref = getSharedPreferences("details",MODE_PRIVATE);
        edit=pref.edit();



        lv = (ListView) findViewById(R.id.listsettings);

        CustomList adapter = new
                CustomList(settingspane.this, mode, imageId);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {

                    startActivity(new Intent(settingspane.this,contacts.class));

                } else if (i == 1) {

                    setMessage();

                } else if (i == 2) {

                   setLocationKey();

                } else if (i == 3) {

                  setBatteryLevel();

                }

            }
        });


    }



    private void setMessage() {

        final EditText edittext = new EditText(getApplicationContext());
        alert.setMessage("Enter Your Distress Message");
        alert.setTitle("SOS");

        alert.setView(edittext);

        edittext.setText(pref.getString("msg","Help!"));

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                message = edittext.getText().toString();
                edit.putString("msg", message);
                //edit.putInt("size",i);
                //edit.putInt("i",i);
                edit.commit();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }


    private void setLocationKey() {

        final EditText edittext = new EditText(getApplicationContext());
        alert.setMessage("Enter Your Remote Location Keyword");
        alert.setTitle("SOS");

        alert.setView(edittext);

        edittext.setText(pref.getString("lockeyword","getloc"));

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                lockeyword = edittext.getText().toString();
                edit.putString("lockeyword", lockeyword);
                //edit.putInt("size",i);
                //edit.putInt("i",i);
                edit.commit();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();

    }



    private void setBatteryLevel() {

        final EditText edittext = new EditText(getApplicationContext());
        alert.setMessage("Enter Battery Level");
        alert.setTitle("SOS");

        alert.setView(edittext);
        int l;
        l=pref.getInt("battlevel",20);

        edittext.setText(String.valueOf(l));

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                lockeyword = edittext.getText().toString();
                edit.putInt("battlevel", batlevel);
                //edit.putInt("size",i);
                //edit.putInt("i",i);
                edit.commit();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.settings).setVisible(false);

        return true;
    }

    ////////////////////////////////menu items////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {

            case R.id.home:
                Intent i = new Intent(settingspane.this, MainActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
