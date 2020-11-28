package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        ArrayList<Map<String, ?>> attacks_from_json = (ArrayList<Map<String, ?>>) map.get("attacks");
        Attack[] attacks = new Attack[attacks_from_json.size()];
        int i=0;
        for (Map<String, ? > entry : attacks_from_json) {
            List<Double> temp = (ArrayList<Double>)entry.get("serials");
            List<Integer> serials = new ArrayList<Integer>();
            int duration = ((Double)entry.get("duration")).intValue();
            for(Double d : temp){
                serials.add(d.intValue());
            }
            Collections.sort(serials); //order the serials to prevent dead lock
            Attack a = new Attack(serials, duration);
            attacks[i] = a;
            i++;
        }
        long r2d2Time = ((Double)map.get("R2D2")).longValue();
        long landoTime = ((Double)map.get("Lando")).longValue();
        int ewoksNum = ((Double)map.get("Ewoks")).intValue();
        ArrayList<Ewok> ewoks = Ewoks.getInstance();
        ewoks.add(0, new Ewok(-1)); //there is no Ewok with serial Number 0
        for (i=1; i<=ewoksNum ; i++)
            ewoks.add(i, new Ewok(i));

        LeiaMicroservice leia = new LeiaMicroservice(attacks);
        HanSoloMicroservice hanSolo = new HanSoloMicroservice();
        C3POMicroservice c3po = new C3POMicroservice();
        R2D2Microservice r2d2 = new R2D2Microservice(r2d2Time);
        LandoMicroservice lando = new LandoMicroservice(landoTime);

        Thread tLeia = new Thread(leia);
        Thread tHanSolo = new Thread(hanSolo);
        Thread tC3po = new Thread(c3po);
        Thread tR2d2 = new Thread(r2d2);
        Thread tLando = new Thread(lando);

        tLeia.start();
        tHanSolo.start();
        tC3po.start();
        tR2d2.start();
        tLando.start();


        try {
            tLeia.join();
            tHanSolo.join();
            tC3po.join();
            tR2d2.join();
            tLando.join();
        }
        catch (InterruptedException e){}
        //create diary here

    }
}
