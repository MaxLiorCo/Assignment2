package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageWrap implements Message {
    private int roundRobinCounter;
    private Class<? extends Message> messageType;
    // this list contains the subscribed MS for this type
    private CopyOnWriteArrayList<MicroService> subscribedMS;

    public MessageWrap(Class<? extends Message> messageType, int counterInit){
        this.messageType = messageType;
        roundRobinCounter = counterInit;
        subscribedMS = new CopyOnWriteArrayList<>();
    }

    /**
     *  all following operations are synchronized since we
     *  do not want multiple services accessing it simultaneously.
     *  Note, that the traffic in this part of the program is minimal and so
     *  synchronization barely hinders performance
     */

    public synchronized void addMS(MicroService ms){ subscribedMS.add(ms); }

    public synchronized void removeMS(MicroService ms){ //TODO
        subscribedMS.remove(ms);
    }

    public synchronized List<MicroService> getSubscribedMS(){
        return subscribedMS;
    }

    public synchronized MicroService getCurrMicroService(){ //TODO may need to be synchronized
        int currMSIndex = roundRobinCounter;
        roundRobinCounter = (roundRobinCounter++) % subscribedMS.size();
        return subscribedMS.get(currMSIndex);
    }

}
