package it.unipr.ce.dsg.deus.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import org.apache.commons.math3.random.ISAACRandom;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.Well1024a;
import org.apache.commons.math3.random.Well19937a;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.random.Well44497a;
import org.apache.commons.math3.random.Well44497b;
import org.apache.commons.math3.random.Well512a;

/**
 * DeusRandom allows to specify the desired PRNG among the one available in the org.apache.commons.math3 and the basic Java random
 * 
 * @author Stefano Sebastio
 *
 */
public class DeusRandom extends Random {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1941434120893263898L;
	//
	private final static String mersenneTwister = "MersenneTwister";
	private final static String isaac = "ISAAC";
	private final static String well1024a = "WELL1024a";
	private final static String well19937a = "WELL19937a";
	private final static String well19937c = "WELL19937c";
	private final static String well44497a = "WELL44497a";
	private final static String well44497b = "WELL44497b";
	private final static String well512a = "WELL512a";
	
	
	private Object random = new Random();//;null;
	//private Method method = null;

	public DeusRandom(int seed){
		this(null, seed);
	}
	
	public DeusRandom(String randomGenerator, int seed) {
		
		//random = new Random();
		super(seed);
		//System.out.println("Call randomGenerator " + randomGenerator);
		if (randomGenerator == null){
			//System.out.println("Basic chosen by null");
			random = new Random(seed);
		}
		else {
			switch (randomGenerator) {
				case mersenneTwister:
					random = new MersenneTwister(seed);
					break;
					
				case isaac:
					random = new ISAACRandom(seed);
					break;
					
				case well1024a:
					random = new Well1024a(seed);
					break;
					
				case well19937a:
					random = new Well19937a(seed);
					break;
					
				case well19937c:
					random = new Well19937c(seed);
					break;
					
				case well44497a:
					random = new Well44497a(seed);
					break;
					
				case well44497b:
					random = new Well44497b(seed);
					break;
					
				case well512a:
					random = new Well512a(seed);
					break;
					
				default :
					random = new Random(seed);
					break;
			}
		}
		 
	}
	
	protected int next(int bits) {
		//Method method;
		try {
			//this.method = 
			return (int) random.getClass().getMethod("next", int.class).invoke(random, bits);
			//this.method.invoke(random, bits);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean nextBoolean(){
		try {
			return (boolean) random.getClass().getMethod("nextBoolean").invoke(random);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void nextBytes(byte[] bytes){
		try {
			random.getClass().getMethod("nextBytes", byte[].class).invoke(random, bytes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public double nextDouble(){
		try {
			return (double) random.getClass().getMethod("nextDouble").invoke(random);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0.0;
	}
	
	public float nextFloat(){
		try {
			return (float) random.getClass().getMethod("nextFloat").invoke(random);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return (float) 0.0;
	}
	
	public double nextGaussian(){
		try {
			return (double) random.getClass().getMethod("nextGaussian").invoke(random);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0.0;
	}
	
	public int nextInt(){
		try {
			return (int) random.getClass().getMethod("nextInt").invoke(random);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int nextInt(int n){
		try {
			//System.out.println("class " + random.getClass());
			return (int) random.getClass().getMethod("nextInt", int.class).invoke(random, n);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public long nextLong(){
		try {
			return (long) random.getClass().getMethod("nextLong").invoke(random);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return (long) 0;
	}
	
	public void setSeed(long seed){
		try {
			if (random == null)
				super.setSeed(seed);
			else 
				random.getClass().getMethod("setSeed", long.class).invoke(random, seed);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	
//	public int nextInt(){
//		Method method;
//		random.getClass().getMethod(name, parameterTypes);
//		method.invoke(random, args)
//		return 1;
//	}
	
	
	
}
