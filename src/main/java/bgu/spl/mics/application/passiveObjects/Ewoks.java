package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.Main;

import java.util.ArrayList;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {

    /**
     * @Attention: Ewoks array at index 0 will be garbage, DO NOT ACCESS IT
     */
    private static ArrayList<Ewok> ewoks;

    private static class SingletonHolder{
        private static ArrayList<Ewok> ewoksInstance = new ArrayList<Ewok>();
    }

    private Ewoks() {
        ewoks = new ArrayList<Ewok>();
    }

    public static ArrayList<Ewok> getInstance() {
        return SingletonHolder.ewoksInstance;
    }

}
