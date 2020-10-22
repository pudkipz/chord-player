package com.pudkipz.chordplayer;

import com.pudkipz.chordplayer.util.ChordType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ResourceLoader {

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

        System.out.println(writer.toString());
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

        System.out.println("ct length in resource loader " + chordTypes.values().toArray().length);

        ChordType.setMap(chordTypes);
    }
}
