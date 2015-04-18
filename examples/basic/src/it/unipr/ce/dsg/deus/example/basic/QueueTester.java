package it.unipr.ce.dsg.deus.example.basic;

import java.util.PriorityQueue;
import java.util.Random;


public class QueueTester {

	Random rng = null;
	
	public QueueTester() {
		rng = new Random(12435697);
	}
	
	public int getNextComparatorInt() {
		int i = rng.nextInt(2);
		if (i == 0)
			i = -1;
		return i;
	}
	
	public static void main(String[] args) {
		QueueTester qt = new QueueTester();
		
		RandomComparator rc = new RandomComparator(qt.rng);
		
		PriorityQueue<TestEvent> pq = new PriorityQueue<TestEvent>(1, rc);
		
		TestEvent e0 = new TestEvent("e0",0);
		TestEvent e1 = new TestEvent("e1",1);
		TestEvent e2 = new TestEvent("e2",1);
		TestEvent e3 = new TestEvent("e3",1);
	
		System.out.println("new add");
		pq.add(e2);
		System.out.println("new add");
		pq.add(e1);
		System.out.println("new add");
		pq.add(e0);
		System.out.println("new add");
		pq.add(e3);
		
		while (pq.size() > 0) {
			TestEvent e = pq.remove();
			System.out.println("e: " + e.getId());
		}
			
	}
}
