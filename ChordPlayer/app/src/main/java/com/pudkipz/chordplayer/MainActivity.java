package com.pudkipz.chordplayer;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.pudkipz.chordplayer.util.ChordType;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InputStream is = getResources().openRawResource(R.raw.chordtypes);
        ResourceLoader.loadChordTypes(is);
        System.out.println(ChordType.getArray().length);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (id == R.id.action_save) {
            onSaveButtonPressed(findViewById(android.R.id.content));
            return true;
        }
        if (id == R.id.action_load) {
            onLoadButtonPressed(findViewById(android.R.id.content));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO: Actually save stuff, using m_text.
    // TODO: Increase padding for the input field? looks a bit stupid when it covers the whole window.
    /**
     * Called when saving a progression. Creates and shows an AlertDialog to enter a name.
     * @param view
     */
    public void onSaveButtonPressed(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.input_save_title);
        builder.setMessage(R.string.input_save_desc);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        builder.show();
    }

    // TODO. Possibly turn into a whole fragment? (Manage progressions...)
    public void onLoadButtonPressed(View view) {
        return;
    }
}
