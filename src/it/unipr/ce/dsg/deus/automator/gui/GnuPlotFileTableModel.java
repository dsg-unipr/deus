package it.unipr.ce.dsg.deus.automator.gui;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * 
 */
@SuppressWarnings("serial")
public class GnuPlotFileTableModel extends AbstractTableModel {

	// String array that contains column names
	protected String columnNames[] = { "File Name", "X Label", "Y Label" };

	// Array that contains the values for the different rows in the model of the table
	protected Object dataValues[][];

	int rowcount;

	int columncount = 3;

	private ArrayList<GnuPlotFileElement> gnuPlotFileList;

	/**
	 *
	 */
	public void set_FileTableModel(
			ArrayList<GnuPlotFileElement> nodeResourceList) {

		this.gnuPlotFileList = nodeResourceList;

		// Create some data
		Object dataValues_app[][] = new Object[nodeResourceList.size()][columncount];

		rowcount = nodeResourceList.size();

		dataValues = dataValues_app;

		for (int i = 0; i < nodeResourceList.size(); i++) {
			dataValues[i][0] = nodeResourceList.get(i).getFileName();
			dataValues[i][1] = nodeResourceList.get(i).getXLabel();
			dataValues[i][2] = nodeResourceList.get(i).getYLabel();
		}
	}

	/**
	 * Returns the number of rows of the model
	 */
	public int getRowCount() {
		return rowcount;
	}

	/**
	 * Returns the name of the column
	 */
	public String getColumnName(int col) {
		return columnNames[col];
	}

	/**
	 * Returns the object type based on the value of the column
	 */
	@SuppressWarnings("unchecked")
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/**
	 * Returns the number of columns in the model
	 */
	public int getColumnCount() {
		return columncount;
	}

	/**
	 * Returns the value, given its position in the table
	 */
	public Object getValueAt(int row, int col) {

		return dataValues[row][col];
	}

	/**
	 * Returns the first element of a row
	 */
	public Object getRowObject(int row) {

		return (String) dataValues[row][0];
	}

	/**
	 * Set a value, given its position in the table
	 */
	public void setValueAt(Object value, int row, int col) {

		dataValues[row][col] = value;

		gnuPlotFileList.get(row).setFileName((String) dataValues[row][0]);
		gnuPlotFileList.get(row).setXLabel((String) dataValues[row][1]);
		gnuPlotFileList.get(row).setYLabel((String) dataValues[row][2]);

		fireTableCellUpdated(row, col);
	}

	/**
	 * Defines which cells can be edited
	 */
	public boolean isCellEditable(int row, int col) {

		return true;
	}

}