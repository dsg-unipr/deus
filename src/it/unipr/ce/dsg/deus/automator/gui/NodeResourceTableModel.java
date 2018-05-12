package it.unipr.ce.dsg.deus.automator.gui;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * 
 */
@SuppressWarnings("serial")
public class NodeResourceTableModel extends AbstractTableModel {

	// String array that contains the names of the columns
	protected String columnNames[] = { "Node Id", "Handler Name",
			"ResParam Value", "Initial Value", "Final Value", "Step Value" };

	// Array that contains the values for the rows in the table model
	protected Object dataValues[][];

	int rowcount;

	int columncount = 6;

	private ArrayList<NodeResource> nodeResourceList;

	public void set_FileTableModel(ArrayList<NodeResource> nodeResourceList) {

		this.nodeResourceList = nodeResourceList;

		// Create some data
		Object dataValues_app[][] = new Object[nodeResourceList.size()][columncount];

		rowcount = nodeResourceList.size();

		dataValues = dataValues_app;

		for (int i = 0; i < nodeResourceList.size(); i++) {
			dataValues[i][0] = nodeResourceList.get(i).getNodeId();
			dataValues[i][1] = nodeResourceList.get(i).getHandlerName();
			dataValues[i][2] = nodeResourceList.get(i).getResParamValue();
			dataValues[i][3] = nodeResourceList.get(i).getInitialValue();
			dataValues[i][4] = nodeResourceList.get(i).getFinalValue();
			dataValues[i][5] = nodeResourceList.get(i).getStepValue();
		}
	}

	/**
	 * Returns the number of rows
	 */
	public int getRowCount() {
		return rowcount;
	}

	/**
	 * Returns the nome of the column, given its number
	 */
	public String getColumnName(int col) {
		return columnNames[col];
	}

	/**
	 * Returns the type of object, given the column number
	 */
	@SuppressWarnings("unchecked")
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/**
	 * Returns the number of columns
	 */
	public int getColumnCount() {
		return columncount;
	}

	/**
	 * Returns the object in the table, given its row and column numbers
	 */
	public Object getValueAt(int row, int col) {

		return dataValues[row][col];
	}

	/**
	 * Returns the object in the first column, given the row number
	 */
	public Object getRowObject(int row) {

		return (String) dataValues[row][0];
	}

	/**
	 * Allows to set a value in the table, given the row and column numbers
	 */
	public void setValueAt(Object value, int row, int col) {
		dataValues[row][col] = value;

		nodeResourceList.get(row).setNodeId((String) dataValues[row][0]);
		nodeResourceList.get(row).setHandlerName((String) dataValues[row][1]);
		nodeResourceList.get(row).setResParamValue((String) dataValues[row][2]);
		nodeResourceList.get(row).setInitialValue((Double) dataValues[row][3]);
		nodeResourceList.get(row).setFinalValue((Double) dataValues[row][4]);
		nodeResourceList.get(row).setStepValue((Double) dataValues[row][5]);

		fireTableCellUpdated(row, col);
	}

	/**
	 * Defines which cells can be edited
	 */
	public boolean isCellEditable(int row, int col) {

		return true;
	}

}