package com.pudkipz.chordplayer;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.pudkipz.chordplayer.util.Chord;
import com.pudkipz.chordplayer.util.ChordType;
import com.pudkipz.chordplayer.util.Note;
import com.pudkipz.chordplayer.util.Progression;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceLoader {

    /**
     * Saves a progression to app specific storage. Does not use the pre-defined chords, but simply
     * copies the attributes from each chord in the Progression.
     *
     * @param progression Progression to write to storage.
     * @param name       Title and also filename. (May make these separate in the future.)
     * @throws JSONException
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void saveProgression(Progression progression, String name, Context context) {
        JSONObject jProg = new JSONObject();
        JSONArray jChords = new JSONArray();

        for (Chord c : progression.getChordTrack()) {
            JSONObject jChord = new JSONObject();
            try {
                jChord.put("name", c.getChordType().getName());
                jChord.put("root", c.getRoot().getMidiValue());
                jChord.put("suffix", c.getSuffix());
                jChord.put("intervals", new JSONArray(c.getIntervals()));
                jChord.put("numerator", c.getNumerator());
                jChord.put("denominator", c.getDenominator());
                jChords.put(jChord);

                jProg.put("title", name);
                jProg.put("chords", jChords);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // TODO: Does not do anything if blank file name! (Handle that... but maybe inside fragment not here.)
        File file = new File(context.getFilesDir(), String.format("%s.json", name));
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(jProg.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * @param name
     * @return
     */
    public Progression loadProgression(String name, Context context) {
        Writer writer = new StringWriter();
        FileInputStream is = null;
        char[] buffer = new char[1024];
        String jsonString = writer.toString();

        Progression progression = new Progression();
        List<Chord> chordList = new ArrayList<>();


        try {
             is = context.openFileInput(name);
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            JSONObject jObject = new JSONObject(jsonString);
            JSONArray jChords = jObject.getJSONArray("chords");

            for (int i=0; i<jChords.length(); i++) {
                String jName = jObject.optString("name");
                int root = jObject.optInt("root");
                String suffix = jObject.optString("suffix");
                int numerator = jObject.optInt("numerator");
                int denominator = jObject.optInt("denominator");

                JSONArray jIntervals = jObject.getJSONArray("intervals");
                int[] intervals = new int[jIntervals.length()];
                for (int j=0; j<jIntervals.length(); j++){
                    intervals[j] = jIntervals.optInt(j);
                }

                chordList.add(new Chord(root, new ChordType(intervals, name, suffix), numerator, denominator));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return progression;
    }

    // TODO: I find it strange that ResourceLoader accesses ChordType directly (by a static method). Is there a better solution?
    /**
     * Loads chord types defined in /res/raw/chordtypes.json and writes them to a Map in ChordType.
     *
     * @param is
     */
    public static void loadChordTypes(InputStream is) {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // System.out.println(writer.toString());
        String jsonString = writer.toString();
        Map<String, ChordType> chordTypes = new HashMap<>();

        try {
            JSONObject jObject = new JSONObject(jsonString);
            JSONArray jArray = jObject.getJSONArray("chordtypes");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject o = jArray.getJSONObject(i);

                JSONArray jsonIntervals = o.getJSONArray("intervals");
                int[] intervals = new int[jsonIntervals.length()];
                for (int j = 0; j < jsonIntervals.length(); j++) {
                    intervals[j] = (int) jsonIntervals.get(j);
                }
                //int[] intervals, String name, String suffix
                ChordType ct = new ChordType(intervals, (String) o.get("name"), (String) o.get("suffix"));
                chordTypes.put((String) o.get("name"), ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // System.out.println("ct length in resource loader " + chordTypes.values().toArray().length);

        ChordType.setMap(chordTypes);
    }
}
