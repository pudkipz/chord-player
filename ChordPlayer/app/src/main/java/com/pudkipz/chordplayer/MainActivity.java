package com.pudkipz.chordplayer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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


}
