package it.unipr.ce.dsg.deus.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


public class ConfidenceInterval {

	public static double computeYAvg(ArrayList<Double> samples) {
		int n = samples.size();
		double sum = 0;
		for (int i=0; i<n; i++)
			sum += samples.get(i);
		double yAvg = sum/n;
		return yAvg;
	}
	
	public static double computeSY(ArrayList<Double> samples, double yAvg) {
		int n = samples.size();
		double sum = 0;
		for (int i=0; i<n; i++)
			sum += (samples.get(i)-yAvg)*(samples.get(i)-yAvg);
		double Sy = Math.sqrt(sum/(n-1));
		return Sy;
	}
	
	public static double computeT(double beta, int f) {
		double t = T.tVal(beta/2, f); // tail = beta/2
		return t;
	}
	
	public static double computeDelta(double beta, double yAvg, double Sy, int n) {
		int f = n-1;
		double t = computeT(beta, f);
		System.out.println("t = " + t);
		double delta = t*Sy;
		return delta;
	}
	
	public static double computeInternalAvg(String fileName, String varName, float VTinit) {
		double yIntAvg = 0;
		float currentVT = 0;
		FileReader reader = null;
		try {
			reader = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		double sum = 0;
		int m = 0;
		Scanner in = new Scanner(reader);
		while (in.hasNextLine()) {
			 String line = in.nextLine(); 
			 if (line.contains("VT"))
				 currentVT = Float.parseFloat(line.substring(line.indexOf("=")+1));
			 if (currentVT>=VTinit) {
				 if (line.contains(varName)) {
					 sum += Double.parseDouble(line.substring(line.indexOf("=")+1));
					 m++;
				 }
			 }
		}
		in.close();
		yIntAvg = sum/m;
		return yIntAvg;
	}
	
	public static void main(String[] args) {
		
		ArrayList<Double> samples = new ArrayList<Double>();
		
		File folder = new File("temp/");
	    File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	    	if (listOfFiles[i].isFile()) {
	    		String fileName = listOfFiles[i].getName();
	    		if (fileName.contains("-0-"))
	    			samples.add(Double.valueOf(computeInternalAvg("temp/"+fileName, "X", ((float)1.296E8))));
	      } else if (listOfFiles[i].isDirectory()) {
	        System.out.println("Directory " + listOfFiles[i].getName());
	      }
	    }
	    
	    int n = samples.size();
	    System.out.println("samples size = " + n);	
	    
		double yAvg = computeYAvg(samples);
		System.out.println("yAvg = " + yAvg);
		double Sy = computeSY(samples, yAvg);
		System.out.println("Sy = " + Sy);
		double delta = computeDelta(0.05, yAvg, Sy, n);
		System.out.println("delta = " + delta);
		double lowerLimit = yAvg - delta;
		double upperLimit = yAvg + delta;
		System.out.println("I95 = ["+lowerLimit+","+upperLimit+"]");
	}

}
