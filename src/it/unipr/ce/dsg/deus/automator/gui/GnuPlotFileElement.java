package it.unipr.ce.dsg.deus.automator.gui;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * 
 */
public class GnuPlotFileElement {

	private String fileName = "";
	private String xLabel = "";
	private String yLabel = "";

	public GnuPlotFileElement() {
		this.fileName = "File Name";
		this.xLabel = "X Label";
		this.yLabel = "Y Label";
	}

	public GnuPlotFileElement(String fileName, String label, String label2) {
		super();
		this.fileName = fileName;
		xLabel = label;
		yLabel = label2;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getXLabel() {
		return xLabel;
	}

	public void setXLabel(String label) {
		xLabel = label;
	}

	public String getYLabel() {
		return yLabel;
	}

	public void setYLabel(String label) {
		yLabel = label;
	}

}
