package it.unipr.ce.dsg.deus.automator.gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * @author Mirco Rosa (mirco.rosa.91@gmail.com) [Event Parametrization]
 * */

public class EventParameterTableModel extends AbstractTableModel{

	protected String columnNames[] = { "Event Id", "Param Name",
			"Initial Value", "Final Value", "Step Value" };

	protected Object dataValues[][];

	int rowcount;

	int columncount = 5;

	private ArrayList<EventParameter> eventParametersList;

	public void set_FileTableModel(ArrayList<EventParameter> eventParametersList) {

		this.eventParametersList = eventParametersList;

		Object dataValues_app[][] = new Object[eventParametersList.size()][columncount];

		rowcount = eventParametersList.size();

		dataValues = dataValues_app;

		for (int i = 0; i < eventParametersList.size(); i++) {
			dataValues[i][0] = eventParametersList.get(i).getEventId();
			dataValues[i][1] = eventParametersList.get(i).getParamName();
			dataValues[i][2] = eventParametersList.get(i).getInitialValue();
			dataValues[i][3] = eventParametersList.get(i).getFinalValue();
			dataValues[i][4] = eventParametersList.get(i).getStepValue();
		}
	}


	public int getRowCount() {
		return rowcount;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public int getColumnCount() {
		return columncount;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return dataValues[rowIndex][columnIndex];
	}

	public Object getRowObject(int row) {

		return (String) dataValues[row][0];
	}

	public void setValueAt(Object value, int row, int col) {
		dataValues[row][col] = value;

		eventParametersList.get(row).setEventId((String)dataValues[row][0]);
		eventParametersList.get(row).setParamName((String)dataValues[row][1]);
		eventParametersList.get(row).setInitialValue((Double)dataValues[row][2]);
		eventParametersList.get(row).setFinalValue((Double)dataValues[row][3]);
		eventParametersList.get(row).setStepValue((Double)dataValues[row][4]);

		fireTableCellUpdated(row, col);
	}

	public boolean isCellEditable(int row, int col) {
		return true;
	}
}
