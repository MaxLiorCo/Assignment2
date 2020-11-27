package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

import java.util.List;

public class AttackEvent implements Event<Boolean>{
	private Attack a;

	public AttackEvent(Attack a){
	    this.a = a;
    }

    public List<Integer> getSerials(){
	    return a.getSerials();
    }
    public int getDuration(){
		return a.getDuration();
	}
}
