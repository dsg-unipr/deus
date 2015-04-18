package it.unipr.ce.dsg.deus.automator.gui;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * 
 */
@SuppressWarnings("serial")
public class EngineParameterTableModel extends AbstractTableModel {

	// String array that contains the names of the columns
	protected String columnNames[] = { "Seed Value" };

	// Array that contains the values for the rows in the table model
	protected Object dataValues[][];

	int rowcount;

	int columncount = 1;

	private ArrayList<EngineParameter> engineParameterList;

	public void set_FileTableModel(
			ArrayList<EngineParameter> engineParameterList) {

		this.engineParameterList = engineParameterList;

		// Create some data
		Object dataValues_app[][] = new Object[engineParameterList.size()][columncount];

		rowcount = engineParameterList.size();

		dataValues = dataValues_app;

		for (int i = 0; i < engineParameterList.size(); i++) {
			dataValues[i][0] = engineParameterList.get(i).getSeedValue();
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

		engineParameterList.get(row).setSeedValue((String) dataValues[row][0]);

		fireTableCellUpdated(row, col);
	}

	/**
	 * Defines which cells can be edited
	 */
	public boolean isCellEditable(int row, int col) {

		return true;
	}

}