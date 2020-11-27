package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Attack;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
    public static void main(String[] args){
        Reader jfile;
        Gson gson;
        Map<?, ?> map = null;
        try {
            jfile = Files.newBufferedReader(Paths.get(args[0]));
            gson = new Gson();
            map = gson.fromJson(jfile, Map.class);

            // print map entries
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }
            System.out.println(map.get("attacks"));
            System.out.println(map.get("attacks").getClass());
            // close reader
            jfile.close();
        }
        catch (IOException e) {
            System.out.println("IO Exception");
            e.printStackTrace();
        }
        long R2d2Time = (long)map.get("R2D2");
        long LandoTime = (long)map.get("Lando");
        int ewoksNum = (int)map.get("Ewoks");
        ArrayList<Map<String, ?>> attacks_from_json = (ArrayList<Map<String, ?>>) map.get("attacks");
        Attack[] attacks = new Attack[attacks_from_json.size()];

    }
}
