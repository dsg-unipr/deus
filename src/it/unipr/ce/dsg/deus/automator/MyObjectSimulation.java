package it.unipr.ce.dsg.deus.automator;

import java.util.ArrayList;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * @author Marco Pigoni
 *
 */
public class MyObjectSimulation {

	private ArrayList<ArrayList<MyObjectNode>> node = new ArrayList<ArrayList<MyObjectNode>>();
	private ArrayList<ArrayList<MyObjectProcess>> process = new ArrayList<ArrayList<MyObjectProcess>>();
	private ArrayList<MyObjectEngine> engine = new ArrayList<MyObjectEngine>();
	private ArrayList<MyObjectGnuplot> gnuplot = new ArrayList<MyObjectGnuplot>();
	private String simulationNumberSeed;
	private Integer simulationNumber = 0;
	private String simulationName;
	private String resultFolder;
	private String inputFolder;
	private String fileLog;
	private int step;
	
	
	public MyObjectSimulation() {
		super();
	}

	public ArrayList<ArrayList<MyObjectNode>> getNode() {
		return node;
	}

	public void setNode(ArrayList<ArrayList<MyObjectNode>> node) {
		this.node = node;
	}

	public ArrayList<ArrayList<MyObjectProcess>> getProcess() {
		return process;
	}

	public void setProcess(ArrayList<ArrayList<MyObjectProcess>> process) {
		this.process = process;
	}

	public ArrayList<MyObjectEngine> getEngine() {
		return engine;
	}

	public void setEngine(ArrayList<MyObjectEngine> engine) {
		this.engine = engine;
	}

	public Integer getSimulationNumber() {
		return simulationNumber;
	}

	public void setSimulationNumber(Integer integer) {
		this.simulationNumber = integer;
	}

	public String getSimulationName() {
		return simulationName;
	}

	public void setSimulationName(String simulationName) {
		this.simulationName = simulationName;
	}

	public String getResultFolder() {
		return resultFolder;
	}

	public void setResultFolder(String resultFolder) {
		this.resultFolder = resultFolder;
	}

	public String getInputFolder() {
		return inputFolder;
	}

	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
	}

	public String getSimulationNumberSeed() {
		return simulationNumberSeed;
	}

	public void setSimulationNumberSeed(String simulationNumberSeed) {
		this.simulationNumberSeed = simulationNumberSeed;
	}

	public String getFileLog() {
		return fileLog;
	}

	public void setFileLog(String fileLog) {
		this.fileLog = fileLog;
	}

	public ArrayList<MyObjectGnuplot> getGnuplot() {
		return gnuplot;
	}

	public void setGnuplot(ArrayList<MyObjectGnuplot> gnuplot) {
		this.gnuplot = gnuplot;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}
	
}
