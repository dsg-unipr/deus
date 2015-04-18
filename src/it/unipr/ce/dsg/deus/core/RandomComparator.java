package it.unipr.ce.dsg.deus.core;

/**
 * <p>
 * The RandomComparator class is used to insert Event objects 
 * in the event queue of the Engine, according to their timestamps.
 * If two events have the same timestamp, their order is randomly chosen.
 * </p>
 * 
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
import java.util.Comparator;
import java.util.Random;

public class RandomComparator implements Comparator<Event> {

	Random rng = null;
	
	public RandomComparator(Random rng) {
		this.rng = rng;
	}
	
	@Override
	public int compare(Event o1, Event o2) {
		//System.out.println(o1.id + " compared to event " + o2.id);
        int result = 0;
        if (o1.triggeringTime < o2.triggeringTime)
                result = -1;
        else if (o1.triggeringTime == o2.triggeringTime) {
                result = this.getNextComparatorInt();
                //System.out.println("result = " + result);
        }
        else if (o1.triggeringTime > o2.triggeringTime)
                result = 1;
        return result;
	}

	public int getNextComparatorInt() {
		int i = rng.nextInt(2);
		if (i == 0)
			i = -1;
		return i;
	}

	
}
