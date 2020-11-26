package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.Main;

import java.util.concurrent.atomic.AtomicReferenceArray;

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
     * ewoks at index zero will be garbage, DO NOT ACCESS IT
     */
    private static <Ewok> ewoks;

    private static class SingletonHolder{
        private static Ewoks ewoksInstance = new Ewoks;
    }

    private Ewoks(){
        ewoks = new AtomicReferenceArray<Ewok>(ewokNum + 1);
        for (int i=1; i<ewokNum; i++)
            ewoks.set(i,new Ewok(i));
    }

    public static Ewoks getInstance(){
        return SingletonHolder.ewoksInstance;
    }
]
