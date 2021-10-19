package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {

    static Object lock = new Object();
    static final int NUM_OF_MICROSERVISES = 5;

    public static void main(String[] args){

        Reader jfile;
        Gson gson;
        Map<?, ?> map = null;
        try {
            jfile = Files.newBufferedReader(Paths.get(args[0]));
            gson = new Gson();
            map = gson.fromJson(jfile, Map.class);

            // close reader
            jfile.close();
        }
        catch (IOException e) {
            System.out.println("IO Exception");
            e.printStackTrace();
        }

        //---Reading all the necessary data from the "input" file
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

        //---Constructing the MicroServices
        LeiaMicroservice leia = new LeiaMicroservice(attacks, NUM_OF_MICROSERVISES, lock);  //Leia must receive num of total Mic-Services, and the lock that locks the Main thread
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

        //---After Leia started, the Main thread will wait till Leia is fully initialized and will notify the thread to continue
        synchronized (lock){
            try {
                     lock.wait();
            }
            catch (InterruptedException e) {}
        }
        //---Then all the other MicroServices threas will come to life
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

        //---After all the thread will join thr main Thread, we will make the "output" file with all the details in the Diary
        Gson output = new Gson();
        Map<String, Object> outputMap = new HashMap<>();
        outputMap.put("Total attacks", Diary.getTotalAttacks());
        outputMap.put("HanSoloFinish", Diary.getHanSoloFinish());
        outputMap.put("C3POFinish", Diary.getC3POFinish());
        outputMap.put("R2D2Deactivate", Diary.getR2D2Deactivate());
        outputMap.put("LeiaTerminate", Diary.getLeiaTerminate());
        outputMap.put("HanSoloTerminate", Diary.getHanSoloTerminate());
        outputMap.put("C3POTerminate", Diary.getC3POTerminate());
        outputMap.put("R2D2Terminate", Diary.getR2D2Terminate());
        outputMap.put("LandoTerminate", Diary.getLandoTerminate());
        try{
            FileWriter writer = new FileWriter(args[1]);
            output.toJson(outputMap, writer);
            writer.flush();
            writer.close();
        }
        catch (IOException e) {}
    }
}
