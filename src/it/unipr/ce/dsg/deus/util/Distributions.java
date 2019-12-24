package it.unipr.ce.dsg.deus.util;

import java.util.Random;


/**
 * A collection of functions that return numbers with different statistical distributions
 * 
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class Distributions {
	
	// DISCRETE R.V.
	
	/**
	 * returns true with probabilty p, false with probability (1-p)
	 */
	public static boolean bernoulli(Random random, double p) {
		return random.nextDouble() < p;
	}
	
	/**
	 * returns an integer according to the geometric distribution
	 */
	public static int geometric(Random random, double p) {
		return (int) Math.ceil(Math.log(random.nextDouble()) / Math.log(1.0 - p));
	}
	
	/**
	 * returns an integer chosen according to the Poisson distribution with average value alpha
	 */
	public static int poisson(Random random, double alpha) {
		int k = 0;
		double p = 1.0;
		double L = Math.exp(-alpha);
		do {
			k++;
			p *= random.nextDouble();
		} while (p > L);
		return k-1;
	}
	
	
	// CONTINUOUS R.V.
	
	/**
	 * returns a double in [a,b], chosen according to the uniform distribution
	 */
	public static double uniform(Random random, double a, double b) {
		return a + random.nextDouble() * (b-a);
	} 
	
	/**
	 * returns a double, chosen according to the normal distribution
	 */
	public static double gaussian(Random random, double mean, double stddev) {
		double r, x, y;
		do {
			x = uniform(random, -1.0, 1.0);
			y = uniform(random, -1.0,1-0);
			r = x*x + y*y;
		} while (r >= 1 || r == 0);
		return mean + stddev * (x * Math.sqrt(-2 * Math.log(r) / r)); 
	}
	
	/**
	 * returns the value of the normal PDF computed in x
	 */
	public static double normalPDF(double mean, double stddev, double x) {
		return (1 / (stddev * Math.sqrt(2*Math.PI))) * Math.exp(- (x - mean) * (x - mean) / (2 * stddev * stddev));
	}
	
	/**
	 * returns a double, chosen according to the exponential distribution with arrival rate lambda
	 */
	public static double exp(Random random, double lambda) {
		return -Math.log(1 - random.nextDouble()) / lambda;
	}
	
	/**
	 * returns a double, chosen according to the Pareto distribution with Pareto index k
	 */
	public static double pareto(Random random, double k) {
		return Math.pow(1 - random.nextDouble(), -1.0/k) - 1.0;
	}
	
	/**
	 * returns a double, chosen according to the Shifted Pareto distribution
	 */ 
	public static double shiftedPareto(Random random, double alpha, double beta) {
		double x = random.nextDouble(); // Random in [0,1]
		return beta * Math.pow(1-x, -1/alpha) - beta;
	}
	
	/**
	 * returns a double, chosen according to the Weibull distribution
	 */ 
	public static double weibull(Random random, double k, double alpha) {
		return alpha * (Math.pow(-Math.log( 1 - random.nextDouble() ), 1/k));
	}
	
	/**
	 * returns a double, chosen according to the multimodal distribution
	 * c must be: double c = min(stddevs) * means.length * Math.sqrt(2 * Math.PI);
	 * if the passed c <= 0, it is computed with getC()
	 */
	public static double multimodal(double[] means, 
			double[] stddevs, 
			double[] weights, 
			double xMin, 
			double xMax, 
			double c, 
			Random rng) {
		
		if (c <= 0)
			c = getC(means, stddevs);
		
		boolean condition = false;
		double x = 0;
		
		while (!condition) {
			// genero numero casuale unif. tra xMin e xMax
			x = xMin + (xMax - xMin) * rng.nextDouble();
			double r = c / gmixDensity(means, stddevs, weights, x);
			double u = rng.nextDouble();
			if (u*r < 1)
				condition = true;
		}
		
		return x;
	}
	
	private static double getC(double[] means, double[] stddevs) {
		return min(stddevs) * means.length * Math.sqrt(2 * Math.PI);
	}
	
	private static double gmixDensity(double[] means, double[] stddevs, double[] weights, double x) {
		double g = 0;
		for (int i = 0; i < means.length; i++) {
			g += weights[i] * normalPDF(means[i],stddevs[i],x);
		}
		return g;
	}
	
	private static double min(double[] array) {
		double min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min)
				min = array[i];
		}
		return min;
	}
}
