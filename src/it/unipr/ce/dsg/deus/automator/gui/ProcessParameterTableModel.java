package it.unipr.ce.dsg.deus.automator.gui;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * 
 */
@SuppressWarnings("serial")
public class ProcessParameterTableModel extends AbstractTableModel {

	// String array that contains the names of the columns
	protected String columnNames[] = { "Process Id", "Param Name",
			"Initial Value", "Final Value", "Step Value" };

	// Array that contains the values for the rows in the table model
	protected Object dataValues[][];

	int rowcount;

	int columncount = 5;

	private ArrayList<ProcessParameter> processParametersList;

	/**
	 * Permette di settare tramite i valori delle Mail all'iterno del modello
	 * della tabella.
	 * 
	 * @param mail_list
	 */
	public void set_FileTableModel(
			ArrayList<ProcessParameter> processParametersList) {

		this.processParametersList = processParametersList;

		// Create some data
		Object dataValues_app[][] = new Object[processParametersList.size()][columncount];

		rowcount = processParametersList.size();

		dataValues = dataValues_app;

		for (int i = 0; i < processParametersList.size(); i++) {
			dataValues[i][0] = processParametersList.get(i).getProcessId();
			dataValues[i][1] = processParametersList.get(i).getParamName();
			dataValues[i][2] = processParametersList.get(i).getInitialValue();
			dataValues[i][3] = processParametersList.get(i).getFinalValue();
			dataValues[i][4] = processParametersList.get(i).getStepValue();
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

		processParametersList.get(row)
				.setProcessId((String) dataValues[row][0]);
		processParametersList.get(row)
				.setParamName((String) dataValues[row][1]);
		processParametersList.get(row).setInitialValue(
				(Double) dataValues[row][2]);
		processParametersList.get(row).setFinalValue(
				(Double) dataValues[row][3]);
		processParametersList.get(row)
				.setStepValue((Double) dataValues[row][4]);

		fireTableCellUpdated(row, col);
	}

	/**
	 * Defines which cells can be edited
	 */
	public boolean isCellEditable(int row, int col) {

		return true;
	}

}