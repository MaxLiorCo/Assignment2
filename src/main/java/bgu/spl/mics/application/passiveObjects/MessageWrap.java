package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;

import java.util.LinkedList;
import java.util.List;

public class MessageWrap implements Message {
    private static int roundRobinCounter;
    private Class<? extends Message> messageType;
    private List<MicroService> subscribedMS; // this lisy contains the subscribed MS for this type

    public MessageWrap(Class<? extends Message> messageType, int counterInit){
        this.messageType = messageType;
        subscribedMS = new LinkedList<>();
        roundRobinCounter = counterInit;
    }
    public void addMS(MicroService ms){
        subscribedMS.add(ms);
    }

    public void removeMS(MicroService ms){
        subscribedMS.remove(ms);
    }

    public List<MicroService> getSubscribedMS(){
        return subscribedMS;
    }

    public MicroService getCurrMicroService(){
        int currMSIndex = roundRobinCounter;
        roundRobinCounter = (roundRobinCounter++) % subscribedMS.size();
        return subscribedMS.get(currMSIndex);
    }
}
