package it.unipr.ce.dsg.deus.automator.gui;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * 
 */
@SuppressWarnings("serial")
public class NodeParameterTableModel extends AbstractTableModel {

	// String array that contains the names of the columns
	protected String columnNames[] = { "Node Id", "Param Name",
			"Initial Value", "Final Value", "Step Value" };

	// Array that contains the values for the rows in the table model
	protected Object dataValues[][];

	int rowcount;

	int columncount = 5;

	private ArrayList<NodeParameter> nodeParametersList;

	public void set_FileTableModel(ArrayList<NodeParameter> nodeParametersList) {

		this.nodeParametersList = nodeParametersList;

		// Create some data
		Object dataValues_app[][] = new Object[nodeParametersList.size()][columncount];

		rowcount = nodeParametersList.size();

		dataValues = dataValues_app;

		for (int i = 0; i < nodeParametersList.size(); i++) {
			dataValues[i][0] = nodeParametersList.get(i).getNodeId();
			dataValues[i][1] = nodeParametersList.get(i).getParamName();
			dataValues[i][2] = nodeParametersList.get(i).getInitialValue();
			dataValues[i][3] = nodeParametersList.get(i).getFinalValue();
			dataValues[i][4] = nodeParametersList.get(i).getStepValue();
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

		nodeParametersList.get(row).setNodeId((String) dataValues[row][0]);
		nodeParametersList.get(row).setParamName((String) dataValues[row][1]);
		nodeParametersList.get(row)
				.setInitialValue((Double) dataValues[row][2]);
		nodeParametersList.get(row).setFinalValue((Double) dataValues[row][3]);
		nodeParametersList.get(row).setStepValue((Double) dataValues[row][4]);

		fireTableCellUpdated(row, col);
	}

	/**
	 * Defines which cells can be edited
	 */
	public boolean isCellEditable(int row, int col) {

		return true;
	}

}