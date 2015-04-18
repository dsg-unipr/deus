package it.unipr.ce.dsg.deus.editor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Define a vertex
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 * 
 * @author Stefano Sebastio (stefano.sebastio@imtlucca.it)
 * 
 */
public class DeusVertex implements Serializable {

	private static final long serialVersionUID = 1L;
	private String Element;
	private String id;
	private String handler;
	private String schedulerListener;
	private String oneShot;
	private String disLevel;

	private String levelLog;
	private String prng;
	private String pathPLog;
	private ArrayList<String> nameParam;
	private ArrayList<String> valueParam;
	private ArrayList<ResourceParam> resParamList;

	private boolean selectOS;
	private boolean selectSL;
	private boolean selectDisLevel;
	private boolean selectKSS;
	private boolean selectPrng;
	private boolean selectLog;
	private boolean selectParam;
	private boolean selectResParam;

	private float maxVT;
	private int seed;
	private int keySpaceSize;

	public DeusVertex(String e_type) {
		setElement(e_type);
		this.id = e_type;
		setHandler(null);
		setSchedListener(null, false);
		setOneShot(null, false);
		setDisLevel(null, false);
		setMaxVT(0);
		setSeed(0);
		setKeySS(0, false);
		setLogger(null, null, false);
		setPrng(null, false);
		setParam(null, null, false);
		setResource(null, false);
	}

	public String toString() {
		return id;
	}

	public String getElementType() {
		return this.Element;
	}

	public String getId() {
		return this.id;
	}

	public String getHandler() {
		return this.handler;
	}

	public int getSeed() {
		return this.seed;
	}

	public float getMaxVT() {
		return this.maxVT;
	}

	public int getKeySS() {
		return this.keySpaceSize;
	}

	public String getOneShot() {
		return this.oneShot;
	}

	public boolean getSelectOS() {
		return this.selectOS;
	}

	public String getSchedListener() {
		return this.schedulerListener;
	}

	public boolean getSelectSL() {
		return this.selectSL;
	}

	public String getDisLevel() {
		return this.disLevel;
	}

	public boolean getSelectDisLevel() {
		return this.selectDisLevel;
	}

	public boolean getSelectKSS() {
		return this.selectKSS;
	}
	
	public boolean getSelectPrng() {
		return this.selectPrng;
	}

	public String getLogLevel() {
		return this.levelLog;
	}
	
	public String getPrng() {
		return this.prng;
	}

	public String getLogPathPrefix() {
		return this.pathPLog;
	}

	public boolean getSelectLog() {
		return this.selectLog;
	}

	public ArrayList<String> getParamName() {
		return this.nameParam;
	}

	public ArrayList<String> getParamValue() {
		return this.valueParam;
	}

	public boolean getSelectParam() {
		return this.selectParam;
	}

	public boolean getSelectResources() {
		return this.selectResParam;
	}

	public ArrayList<ResourceParam> getResource() {
		return this.resParamList;
	}

	public void setLogger(String level, String pathPrefix, boolean infoLog) {
		this.levelLog = level;
		this.pathPLog = pathPrefix;
		this.selectLog = infoLog;
	}
	
	public void setPrng(String prng, boolean selectPrng) {
		this.prng = prng;
		this.selectPrng = selectPrng;
		//System.out.println("prng selected is " + this.prng);
	}

	public void setParam(ArrayList<String> name, ArrayList<String> value,
			boolean infoParam) {
		this.nameParam = name;
		this.valueParam = value;
		this.selectParam = infoParam;
	}

	public void setResource(ArrayList<ResourceParam> resList, boolean infoRes) {
		this.resParamList = resList;
		this.selectResParam = infoRes;
	}

	public void setElement(String ele_type) {
		this.Element = ele_type;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public void setSchedListener(String shList, boolean infoChoose) {
		this.schedulerListener = shList;
		this.selectSL = infoChoose;
	}

	public void setOneShot(String oneShot, boolean infoChoose) {
		this.oneShot = oneShot;
		this.selectOS = infoChoose;
	}

	public void setDisLevel(String distribution, boolean infoChoose) {
		this.disLevel = distribution;
		this.selectDisLevel = infoChoose;
	}

	public void setMaxVT(float maxvt) {
		this.maxVT = maxvt;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public void setKeySS(int keyss, boolean infoChoose) {
		this.keySpaceSize = keyss;
		this.selectKSS = infoChoose;
	}

}
