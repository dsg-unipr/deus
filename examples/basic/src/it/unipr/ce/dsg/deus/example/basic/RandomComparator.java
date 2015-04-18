package it.unipr.ce.dsg.deus.example.basic;

import java.util.Comparator;
import java.util.Random;

public class RandomComparator implements Comparator<TestEvent> {

	Random rng = null;
	
	public RandomComparator(Random rng) {
		this.rng = rng;
	}
	
	@Override
	public int compare(TestEvent o1, TestEvent o2) {
		System.out.println(o1.id + " compared to event " + o2.id);
        int result = 0;
        if (o1.triggeringTime < o2.triggeringTime)
                result = -1;
        else if (o1.triggeringTime == o2.triggeringTime) {
                result = this.getNextComparatorInt();
                System.out.println("result = " + result);
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
