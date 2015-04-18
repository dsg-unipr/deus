package it.unipr.ce.dsg.deus.editor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;

import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


/**
 * This class manages the dialog window to set the attributes and any more of
 * all vertices
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 * 
 * @author Stefano Sebastio (stefano.sebastio@imtlucca.it)
 * 
 */
public class VertexPropertiesDialog extends JDialog implements ActionListener,
		ItemListener, ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DeusVertex vert;
	private JPanel basic;
	private JFormattedTextField textFieldId;
	private JFormattedTextField textFieldHandler;

	private JFormattedTextField textFieldSchedList;
	private JFormattedTextField textFieldDisLevel;
	private JFormattedTextField textFieldMaxVt;
	private JFormattedTextField textFieldSeed;
	private JFormattedTextField textFieldKeySS;
	private JFormattedTextField textFieldPrng;

	private JFormattedTextField textFieldPathPrefix;
	private JFormattedTextField textFieldParamName;
	private JFormattedTextField textFieldParamValue;

	private JComboBox comboLevel;
	private JComboBox comboOneShot;
	private JComboBox comboPrng;

	private JCheckBox checkBoxDisLevel;
	private JCheckBox checkBoxOneShot;
	private JCheckBox checkBoxSListener;
	private JCheckBox checkBoxKeySS;
	private JCheckBox checkBoxPrng;
	private JCheckBox checkBoxLog;
	private JCheckBox checkBoxParam;

	private DefaultListModel modelParamName;
	private DefaultListModel modelParamValue;

	private JButton addButton;
	private JButton remButton;

	private JList listParamName;
	private JList listParamValue;

	private ListSelectionModel listSelectionModelName;
	private ListSelectionModel listSelectionModelValue;

	private String choiceOS[] = { "Select...", "FALSE", "TRUE" };

	private String choiceLevel[] = { "Select...", "OFF", "SEVERE", "WARNING",
			"INFO", "CONFIG", "FINE", "FINER", "FINEST", "ALL" };
	
	private String choicePrng[] = {"std Java", "MersenneTwister", "ISAAC", "WELL1024a", "WELL19937a", "WELL19937c", "WELL44497a", "WELL44497b", "WELL512a"};

	private JFormattedTextField textFieldResParamName;
	private JFormattedTextField textFieldResParamValue;
	private JFormattedTextField textFieldResHandler;
	private DefaultListModel modelResParamName;
	private DefaultListModel modelResParamValue;
	private JList listResParamName;
	private JList listResParamValue;
	private ListSelectionModel listSelectionModelResName;
	private ListSelectionModel listSelectionModelResValue;
	private JButton addResParamButton;
	private JButton remResParamButton;
	private JComboBox comboResources;
	private JCheckBox checkBoxResources;
	private JButton addResButton;
	private JButton remResButton;

	private int numComboRes = 0;
	private ArrayList<ResourceParam> resParamList;

	VertexPropertiesDialog(JFrame f, DeusVertex v) {
		super(f, true);
		this.vert = v;
		setTitle(vert.getElementType() + " Properties");
		// setSize(new Dimension(580,435));

		if (this.vert.getElementType() == "Engine")
			//setSize(new Dimension(580, 315));
			setSize(new Dimension(580, 345));
		else if (this.vert.getElementType() == "Process")
			setSize(new Dimension(580, 410));
		else if (this.vert.getElementType() == "Node")
			setSize(new Dimension(580, 425));
		else if (this.vert.getElementType() == "Network")
			setSize(new Dimension(580, 310));
		else if (this.vert.getElementType() == "Event")
			setSize(new Dimension(580, 455));

		designDialogPanel();
	}

	private void designDialogPanel() {

		basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.PAGE_AXIS));
		basic.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

		JPanel attPanel = new JPanel(new GridLayout(0, 1));
		attPanel.setBorder(new javax.swing.border.TitledBorder("Attributes"));
		attPanel.add(attAddPanel());

		JPanel optAttPanel = new JPanel(new GridLayout(0, 1));
		optAttPanel.setBorder(new javax.swing.border.TitledBorder(
				"Optional attributes"));
		optAttPanel.add(optAttAddPanel());

		JPanel loggerPanel = new JPanel(new GridLayout(0, 1));
		loggerPanel.setBorder(new javax.swing.border.TitledBorder("Logger"));
		loggerPanel.add(loggerAddPanel());

		basic.add(attPanel);
		basic.add(optAttPanel);
		basic.add(loggerPanel);

		if (vert.getElementType() != "Engine"
				&& vert.getElementType() != "Network") {

			JPanel paramPanel = new JPanel(new GridLayout(0, 1));
			paramPanel.setBorder(new javax.swing.border.TitledBorder(
					"Parameter"));
			paramPanel.add(paramAddPanel());

			basic.add(paramPanel);
		}
		if (vert.getElementType() == "Node") {

			basic.add(createButtons());

			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.add("Main", basic);
			tabbedPane.add("Resources", resourcesPanel());

			add(tabbedPane);

		} else {

			basic.add(createButtons());
			add(basic);

		}

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

	}

	private JComponent resourcesPanel() {

		JPanel resPanel = new JPanel();

		resPanel.setLayout(new BoxLayout(resPanel, BoxLayout.PAGE_AXIS));
		resPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

		JPanel resourcesPanel = new JPanel(new GridLayout(0, 1));
		resourcesPanel.setBorder(new TitledBorder("Resources"));
		resourcesPanel.add(resourceAddPanel());

		JPanel handlerPanel = new JPanel(new GridLayout(0, 1));
		handlerPanel.setBorder(new TitledBorder("Resource Handler"));
		handlerPanel.add(handlerAddPanel());

		JPanel paramPanel = new JPanel(new GridLayout(0, 1));
		paramPanel.setBorder(new TitledBorder("Resource Parameters"));
		paramPanel.add(paramResAddPanel());

		resPanel.add(resourcesPanel);
		resPanel.add(handlerPanel);
		resPanel.add(paramPanel);
		resPanel.add(createButtons());

		return resPanel;

	}

	private JComponent attAddPanel() {

		JPanel attributePanel = new JPanel(new SpringLayout());

		if (vert.getElementType() != "Engine") {

			// Process, Event and Node component

			JLabel labelID = new JLabel("Id:");
			textFieldId = new JFormattedTextField();
			// textFieldId.setColumns(20);
			textFieldId.setText(vert.getId());

			JLabel labelHandler = new JLabel("Handler:");
			textFieldHandler = new JFormattedTextField();
			// textFieldHandler.setColumns(20);
			textFieldHandler.setText(vert.getHandler());

			attributePanel.add(labelID);
			attributePanel.add(textFieldId);
			attributePanel.add(labelHandler);
			attributePanel.add(textFieldHandler);

			SpringForm.makeCompactGrid(attributePanel, 2, 2, 5, 5, 5, 8);
		} else {

			// Engine component
			JLabel labelMaxVT = new JLabel("MaxVT:");
			textFieldMaxVt = new JFormattedTextField();
			// textFieldMaxVt.setColumns(15);
			textFieldMaxVt.setText(Float.toString(vert.getMaxVT()));

			JLabel labelSeed = new JLabel("Seed:");
			textFieldSeed = new JFormattedTextField();
			// textFieldSeed.setColumns(15);
			textFieldSeed.setText(Integer.toString(vert.getSeed()));

			attributePanel.add(labelMaxVT);
			attributePanel.add(textFieldMaxVt);
			attributePanel.add(labelSeed);
			attributePanel.add(textFieldSeed);

			SpringForm.makeCompactGrid(attributePanel, 2, 2, 5, 5, 5, 8);

		}

		return attributePanel;
	}

	private JComponent optAttAddPanel() {

		JPanel optAttributePanel = new JPanel(new SpringLayout());

		if (vert.getElementType() == "Event") {

			// Event component (Optional attributes)

			JLabel labelSListener = new JLabel("Scheduler Listener:");
			textFieldSchedList = new JFormattedTextField();
			textFieldSchedList.setEditable(vert.getSelectSL());
			textFieldSchedList.setText(vert.getSchedListener());
			// textFieldSchedList.setColumns(15);

			checkBoxSListener = new JCheckBox();
			checkBoxSListener.setSelected(vert.getSelectSL());
			checkBoxSListener.addItemListener(this);

			JLabel labelOShot = new JLabel("One Shot:");
			comboOneShot = new JComboBox(choiceOS);
			comboOneShot.setEnabled(vert.getSelectOS());
			comboOneShot.addItemListener(this);
			comboOneShot.setSelectedIndex(getIndexOS(vert.getOneShot()));

			checkBoxOneShot = new JCheckBox();
			checkBoxOneShot.setSelected(vert.getSelectOS());
			checkBoxOneShot.addItemListener(this);

			JLabel labelDistLev = new JLabel("Distribution level:");
			textFieldDisLevel = new JFormattedTextField();
			textFieldDisLevel.setEditable(vert.getSelectDisLevel());
			textFieldDisLevel.setText(vert.getDisLevel());

			checkBoxDisLevel = new JCheckBox();
			checkBoxDisLevel.setSelected(vert.getSelectDisLevel());
			checkBoxDisLevel.addItemListener(this);

			optAttributePanel.add(labelSListener);
			optAttributePanel.add(textFieldSchedList);
			optAttributePanel.add(checkBoxSListener);
			optAttributePanel.add(labelOShot);
			optAttributePanel.add(comboOneShot);
			optAttributePanel.add(checkBoxOneShot);
			optAttributePanel.add(labelDistLev);
			optAttributePanel.add(textFieldDisLevel);
			optAttributePanel.add(checkBoxDisLevel);

			SpringForm.makeCompactGrid(optAttributePanel, 3, 3, 5, 5, 5, 8);

		} else if (vert.getElementType() == "Engine") {

			// Engine component (Optional attributes)
			JLabel labelKeySS = new JLabel("Key space size:");
			textFieldKeySS = new JFormattedTextField();
			// textFieldKeySS.setColumns(15);
			textFieldKeySS.setText(Integer.toString(vert.getKeySS()));
			textFieldKeySS.setEditable(vert.getSelectKSS());

			checkBoxKeySS = new JCheckBox();
			checkBoxKeySS.setSelected(vert.getSelectKSS());
			checkBoxKeySS.addItemListener(this);
			
			
			// Stefano to add PRNG
			JLabel labelPrng = new JLabel("PRNG:");
			//TODO: change in combobox
//			textFieldPrng = new JFormattedTextField();
//			textFieldPrng.setText(Integer.toString(vert.getKeySS())); //
//			textFieldPrng.setEditable(vert.getSelectKSS()); //
			
			/**
			 * 			comboLevel = new JComboBox(choiceLevel);
			comboLevel.setEnabled(vert.getSelectLog());

			comboLevel.setSelectedIndex(getIndexLevel(vert.getLogLevel()));
			 * 
			 */
			comboPrng = new JComboBox(choicePrng);
			comboPrng.setEnabled(vert.getSelectPrng());
			//comboPrng.setEnabled(true);
			comboPrng.setSelectedIndex(getIndexPrng(vert.getPrng()));

			checkBoxPrng = new JCheckBox();
			checkBoxPrng.setSelected(vert.getSelectPrng());
			checkBoxPrng.addItemListener(this);
			
			
			

			optAttributePanel.add(labelKeySS);
			optAttributePanel.add(textFieldKeySS);
			optAttributePanel.add(checkBoxKeySS);
			
			optAttributePanel.add(labelPrng);
			optAttributePanel.add(comboPrng);
			optAttributePanel.add(checkBoxPrng);
			

			//SpringForm.makeCompactGrid(optAttributePanel, 1, 3, 5, 5, 5, 8);
			SpringForm.makeCompactGrid(optAttributePanel, 2, 3, 5, 5, 5, 8);

		} else {

			JLabel noOpt = new JLabel("No options");
			noOpt.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
			optAttributePanel.add(noOpt);

			SpringForm.makeCompactGrid(optAttributePanel, 1, 1, 5, 5, 5, 8);
		}

		return optAttributePanel;
	}

	private JComponent loggerAddPanel() {

		JPanel logPanel = new JPanel(new SpringLayout());

		if (vert.getElementType() != "Network") {

			JLabel labelLevel = new JLabel("Level:");
			comboLevel = new JComboBox(choiceLevel);
			comboLevel.setEnabled(vert.getSelectLog());

			comboLevel.setSelectedIndex(getIndexLevel(vert.getLogLevel()));

			JLabel labelPath = new JLabel("Path prefix:");
			textFieldPathPrefix = new JFormattedTextField();
			textFieldPathPrefix.setEditable(vert.getSelectLog());
			textFieldPathPrefix.setText(vert.getLogPathPrefix());
			// textFieldPathPrefix.setColumns(20);

			checkBoxLog = new JCheckBox();
			checkBoxLog.setSelected(vert.getSelectLog());
			checkBoxLog.addItemListener(this);

			logPanel.add(labelLevel);
			logPanel.add(comboLevel);
			logPanel.add(labelPath);
			logPanel.add(textFieldPathPrefix);
			logPanel.add(checkBoxLog);

			SpringForm.makeCompactGrid(logPanel, 1, 5, 5, 5, 8, 8);

		} else {

			JLabel labelLevel = new JLabel("No logger");
			labelLevel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
			logPanel.add(labelLevel);

			SpringForm.makeCompactGrid(logPanel, 1, 1, 5, 5, 5, 8);
		}
		return logPanel;
	}

	private JComponent paramAddPanel() {

		JPanel paramPanel = new JPanel(new SpringLayout());

		JLabel labelName = new JLabel("Name:");
		textFieldParamName = new JFormattedTextField();
		textFieldParamName.setEditable(vert.getSelectParam());
		// textFieldParam.setColumns(20);

		JLabel labelValue = new JLabel("Value:");
		textFieldParamValue = new JFormattedTextField();
		textFieldParamValue.setEditable(vert.getSelectParam());

		JLabel labelListName = new JLabel("");

		JLabel labelListValue = new JLabel("");
		JLabel labelNo = new JLabel("");

		checkBoxParam = new JCheckBox();
		checkBoxParam.setSelected(vert.getSelectParam());
		checkBoxParam.addItemListener(this);

		modelParamName = new DefaultListModel();
		modelParamValue = new DefaultListModel();

		listParamName = new JList(modelParamName);
		listParamName.setLayoutOrientation(JList.VERTICAL);
		listParamName.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// listParamName.addListSelectionListener(this);

		listSelectionModelName = listParamName.getSelectionModel();
		listSelectionModelName.addListSelectionListener(this);

		listParamValue = new JList(modelParamValue);
		listParamValue.setLayoutOrientation(JList.VERTICAL);
		listParamValue.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		listSelectionModelValue = listParamValue.getSelectionModel();
		listSelectionModelValue.addListSelectionListener(this);

		if (vert.getSelectParam()) {

			String name;
			String value;

			Iterator<String> iterName = vert.getParamName().iterator();
			Iterator<String> iterValue = vert.getParamValue().iterator();

			while (iterName.hasNext()) {

				name = iterName.next().toString();

				value = iterValue.next().toString();

				modelParamName.addElement(name);
				modelParamValue.addElement(value);
			}
		}

		addButton = new JButton("Add");
		addButton.addActionListener(this);
		addButton.setEnabled(vert.getSelectParam());
		addButton.setMnemonic(KeyEvent.VK_A);

		remButton = new JButton("Remove");
		remButton.addActionListener(this);
		remButton.setEnabled(vert.getSelectParam());
		remButton.setMnemonic(KeyEvent.VK_R);

		paramPanel.add(labelName);
		paramPanel.add(textFieldParamName);
		paramPanel.add(labelValue);
		paramPanel.add(textFieldParamValue);
		paramPanel.add(addButton);
		paramPanel.add(checkBoxParam);

		paramPanel.add(labelListName);
		paramPanel.add(new JScrollPane(listParamName));
		paramPanel.add(labelListValue);
		paramPanel.add(new JScrollPane(listParamValue));
		paramPanel.add(remButton);
		paramPanel.add(labelNo);

		SpringForm.makeCompactGrid(paramPanel, 2, 6, 5, 5, 8, 8);

		return paramPanel;
	}

	private JComponent paramResAddPanel() {

		JPanel paramPanel = new JPanel(new SpringLayout());
		paramPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));
		JLabel labelName = new JLabel("Name:");
		textFieldResParamName = new JFormattedTextField();
		textFieldResParamName.setEditable(vert.getSelectResources());
		// textFieldParam.setColumns(20);

		JLabel labelValue = new JLabel("Value:");
		textFieldResParamValue = new JFormattedTextField();
		textFieldResParamValue.setEditable(vert.getSelectResources());

		JLabel labelListName = new JLabel("");

		JLabel labelListValue = new JLabel("");
		// JLabel labelNo = new JLabel("");

		modelResParamName = new DefaultListModel();
		modelResParamValue = new DefaultListModel();

		listResParamName = new JList(modelResParamName);
		listResParamName.setLayoutOrientation(JList.VERTICAL);
		listResParamName.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// listParamName.addListSelectionListener(this);

		listSelectionModelResName = listResParamName.getSelectionModel();
		listSelectionModelResName.addListSelectionListener(this);

		listResParamValue = new JList(modelResParamValue);
		listResParamValue.setLayoutOrientation(JList.VERTICAL);
		listResParamValue.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		listSelectionModelResValue = listResParamValue.getSelectionModel();
		listSelectionModelResValue.addListSelectionListener(this);

		/*
		 * if(vert.getSelectParam()){
		 * 
		 * String name; String value; //ArrayList<String>
		 * pName=vert.getParamName(); //ArrayList<String>
		 * pValue=vert.getParamValue();
		 * 
		 * Iterator<String> iterName=vert.getParamName().iterator();
		 * Iterator<String> iterValue=vert.getParamValue().iterator();
		 * 
		 * while(iterName.hasNext()) {
		 * 
		 * name=iterName.next().toString();
		 * 
		 * value=iterValue.next().toString();
		 * 
		 * modelParamName.addElement(name); modelParamValue.addElement(value); }
		 * }
		 */

		addResParamButton = new JButton("Add param");
		addResParamButton.addActionListener(this);
		addResParamButton.setEnabled(vert.getSelectResources());
		// addButton.setMnemonic(KeyEvent.VK_A);

		remResParamButton = new JButton("Remove param");
		remResParamButton.addActionListener(this);
		remResParamButton.setEnabled(vert.getSelectResources());
		// remButton.setMnemonic(KeyEvent.VK_R);

		paramPanel.add(labelName);
		paramPanel.add(textFieldResParamName);
		paramPanel.add(labelValue);
		paramPanel.add(textFieldResParamValue);
		paramPanel.add(addResParamButton);

		paramPanel.add(labelListName);
		paramPanel.add(new JScrollPane(listResParamName));
		paramPanel.add(labelListValue);
		paramPanel.add(new JScrollPane(listResParamValue));
		paramPanel.add(remResParamButton);

		SpringForm.makeCompactGrid(paramPanel, 2, 5, 5, 5, 8, 8);

		return paramPanel;
	}

	private JComponent resourceAddPanel() {

		JPanel resPanel = new JPanel(new SpringLayout());
		resPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

		resParamList = new ArrayList<ResourceParam>();

		JLabel labelResources = new JLabel("Resource:");

		comboResources = new JComboBox();

		if (vert.getSelectResources()) {

			Iterator<ResourceParam> itResParam = vert.getResource().iterator();

			while (itResParam.hasNext()) {

				comboResources.addItem("Resource" + numComboRes);
				resParamList.add(itResParam.next());

				numComboRes++;

			}
		}

		addResButton = new JButton("Add resource");
		addResButton.addActionListener(this);
		addResButton.setEnabled(vert.getSelectResources());

		remResButton = new JButton("Remove resource");
		remResButton.addActionListener(this);
		remResButton.setEnabled(vert.getSelectResources());

		checkBoxResources = new JCheckBox();
		checkBoxResources.setSelected(vert.getSelectResources());
		checkBoxResources.addItemListener(this);

		resPanel.add(labelResources);
		resPanel.add(comboResources);
		resPanel.add(addResButton);
		resPanel.add(remResButton);
		resPanel.add(checkBoxResources);

		SpringForm.makeCompactGrid(resPanel, 1, 5, 5, 5, 8, 8);

		return resPanel;

	}

	private JComponent handlerAddPanel() {

		JPanel handlerPanel = new JPanel();
		handlerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

		JLabel labelListName = new JLabel("Handler:");

		textFieldResHandler = new JFormattedTextField();
		textFieldResHandler.setColumns(40);
		textFieldResHandler.setEditable(vert.getSelectResources());

		handlerPanel.add(labelListName);
		handlerPanel.add(textFieldResHandler);

		return handlerPanel;
	}

	private JComponent createButtons() {

		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		bottom.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

		JButton okButton = new JButton("OK");
		okButton.setMnemonic(KeyEvent.VK_O);
		okButton.addActionListener(this);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setMnemonic(KeyEvent.VK_C);
		cancelButton.addActionListener(this);

		bottom.add(okButton);
		bottom.add(cancelButton);

		return bottom;
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {

		int pos = 0;
		int posRes = 0;

		if (e.getActionCommand() == "OK") {

			if (vert.getElementType() != "Engine") {

				// ******* Principal attribute for Event, Process and Node

				vert.setId((String) this.textFieldId.getText());
				vert.setHandler((String) this.textFieldHandler.getText());

				if (vert.getElementType() != "Network") {
					// ***** Param for Event, Process and Node
					if (this.checkBoxParam.isSelected()) {

						ArrayList<String> nameList = new ArrayList<String>();
						nameList = (ArrayList<String>) Collections
								.list(modelParamName.elements());
						ArrayList<String> valueList = new ArrayList<String>();
						valueList = (ArrayList<String>) Collections
								.list(modelParamValue.elements());

						vert.setParam(nameList, valueList, true);
					}
				}

				if (vert.getElementType() == "Node") {

					// ***** Node resources
					if (this.checkBoxResources.isSelected()
							&& resParamList.size() != 0) {

						vert.setResource(resParamList, true);

					}
				}

				if (vert.getElementType() == "Event") {

					// *********** Event

					// check select item
					if (this.checkBoxSListener.isSelected()
							&& !(this.textFieldSchedList.getText().equals("")))
						vert.setSchedListener(
								(String) this.textFieldSchedList.getText(),
								true);

					// check select item
					if (this.checkBoxOneShot.isSelected()
							&& this.comboOneShot.getSelectedIndex() != 0)
						vert.setOneShot(this.comboOneShot.getSelectedItem()
								.toString(), true);

					if (this.checkBoxDisLevel.isSelected()
							&& !(this.textFieldDisLevel.getText().equals(""))) {
						vert.setDisLevel(
								(String) this.textFieldDisLevel.getText(), true);

					}
				}

			} else {
				// ********* Engine
				//System.out.println("chnagedsomething and pressed ok");
				vert.setMaxVT(Float.valueOf(this.textFieldMaxVt.getText())
						.floatValue());
				vert.setSeed(Integer.valueOf(this.textFieldSeed.getText())
						.intValue());
				//vert.setPrng(String.valueOf(this.comboPrng.getSelectedItem()), true);

				// check select item
				if (this.checkBoxKeySS.isSelected()
						&& !(this.textFieldKeySS.getText().equals(""))) {
					vert.setKeySS(Integer
							.valueOf(this.textFieldKeySS.getText()).intValue(),
							true);
				}
				
				if (this.checkBoxPrng.isSelected() ) {
						//&& this.comboPrng.getSelectedIndex() != 0) {
					//System.out.println("prng box modified on OK");
					vert.setPrng(String.valueOf(this.comboPrng.getSelectedItem()),true);
				}

			}
			if (vert.getElementType() != "Network") {

				// ********* Logger for engine, event, process and node

				if (this.checkBoxLog.isSelected()
						&& this.comboLevel.getSelectedIndex() != 0) {
					vert.setLogger(
							String.valueOf(this.comboLevel.getSelectedItem()),
							this.textFieldPathPrefix.getText(), true);
				}

			}

			dispose();
		} else if (e.getActionCommand() == "Add") {

			if (this.textFieldParamName.getText().equals("")) {

				this.textFieldParamName.setText("Insert name");

				JOptionPane.showMessageDialog(basic,
						"Enter data in the field.", "Warning",
						JOptionPane.WARNING_MESSAGE);

			} else if (this.textFieldParamValue.getText().equals("")) {

				this.textFieldParamValue.setText("Insert value");

				JOptionPane.showMessageDialog(basic,
						"Enter data in the field.", "Warning",
						JOptionPane.WARNING_MESSAGE);

			} else {

				pos = listParamName.getModel().getSize();

				modelParamName.add(pos, this.textFieldParamName.getText());
				this.textFieldParamName.setText("");

				modelParamValue.add(pos, this.textFieldParamValue.getText());
				this.textFieldParamValue.setText("");

			}
		} else if (e.getActionCommand() == "Remove") {

			if (listParamName.isSelectionEmpty()) {
				// donothing
			} else {

				pos = listParamName.getSelectedIndex();

				modelParamName.remove(pos);
				modelParamValue.remove(pos);
			}

		} else if (e.getActionCommand() == "Add resource") {

			if (modelResParamName.isEmpty()
					|| textFieldResHandler.getText().equals("")) {
				JOptionPane
						.showMessageDialog(
								basic,
								"Insert Handler and Parameters, then click on \"Add resource\". ",
								"Warning", JOptionPane.WARNING_MESSAGE);

			} else {

				comboResources.addItem("Resource" + numComboRes);

				ResourceParam addResParam = new ResourceParam();
				ArrayList<String> nameResParam = new ArrayList<String>();
				ArrayList<String> valueResParam = new ArrayList<String>();

				nameResParam = (ArrayList<String>) Collections
						.list(modelResParamName.elements());
				valueResParam = (ArrayList<String>) Collections
						.list(modelResParamValue.elements());

				addResParam.setNameResParamList(nameResParam);
				addResParam.setValueResParmList(valueResParam);
				addResParam.setHandlerRes(textFieldResHandler.getText());

				resParamList.add(addResParam);

				modelResParamName.removeAllElements();
				modelResParamValue.removeAllElements();
				textFieldResHandler.setText("");

				numComboRes++;

			}

		} else if (e.getActionCommand() == "Remove resource") {

			if (comboResources.getItemCount() == 0)
				JOptionPane.showMessageDialog(basic,
						"No resources to eliminate", "Warning",
						JOptionPane.WARNING_MESSAGE);
			else {

				comboResources.removeItem(comboResources
						.getItemAt(comboResources.getSelectedIndex()));
				resParamList.remove(comboResources.getSelectedIndex() + 1);

			}

		} else if (e.getActionCommand() == "Add param") {

			if (this.textFieldResParamName.getText().equals("")) {

				this.textFieldResParamName.setText("Insert name");

				JOptionPane.showMessageDialog(basic,
						"Enter data in the field.", "Warning",
						JOptionPane.WARNING_MESSAGE);

			} else if (this.textFieldResParamValue.getText().equals("")) {

				this.textFieldResParamValue.setText("Insert value");

				JOptionPane.showMessageDialog(basic,
						"Enter data in the field.", "Warning",
						JOptionPane.WARNING_MESSAGE);
			} else {

				posRes = listResParamName.getModel().getSize();

				modelResParamName.add(posRes,
						this.textFieldResParamName.getText());
				this.textFieldResParamName.setText("");

				modelResParamValue.add(posRes,
						this.textFieldResParamValue.getText());
				this.textFieldResParamValue.setText("");

			}

		} else if (e.getActionCommand() == "Remove param") {

			if (listResParamName.isSelectionEmpty()) {
				// donothing
			} else {

				posRes = listResParamName.getSelectedIndex();

				modelResParamName.remove(posRes);
				modelResParamValue.remove(posRes);
			}

		} else
			dispose();

	}

	public void itemStateChanged(ItemEvent e) {

		Object source = e.getItemSelectable();
		//System.out.println("selection changed from " + source.toString() + " where checkBoxPrng is " + checkBoxPrng.toString());
		// se il checkbox è spuntato vengono abilitati i campi
		if (source == checkBoxSListener) {

			textFieldSchedList.setEditable(true);

		} else if (source == checkBoxOneShot) {

			comboOneShot.setEnabled(true);

		} else if (source == checkBoxKeySS) {

			textFieldKeySS.setEditable(true);

		} else if (source == checkBoxPrng) {
			
			//System.out.println("state change itemStateChanged");
			comboPrng.setEnabled(true);

		} else if (source == checkBoxDisLevel) {

			textFieldDisLevel.setEditable(true);
		} else if (source == checkBoxLog) {

			comboLevel.setEnabled(true);
			textFieldPathPrefix.setEditable(true);

		} else if (source == checkBoxParam) {

			textFieldParamName.setEditable(true);
			textFieldParamValue.setEditable(true);
			addButton.setEnabled(true);
			remButton.setEnabled(true);

		} else if (source == checkBoxResources) {

			textFieldResParamName.setEditable(true);
			textFieldResParamValue.setEditable(true);
			textFieldResHandler.setEditable(true);
			addResButton.setEnabled(true);
			remResButton.setEnabled(true);
			addResParamButton.setEnabled(true);
			remResParamButton.setEnabled(true);

		}

		if (e.getStateChange() == ItemEvent.DESELECTED) {
			//System.out.println("something disabled...");
			if (e.getSource().equals(checkBoxSListener)) {

				textFieldSchedList.setText(null);
				textFieldSchedList.setEditable(false);
				vert.setSchedListener(null, false);

			} else if (e.getSource().equals(checkBoxDisLevel)) {
				textFieldDisLevel.setText(null);
				textFieldDisLevel.setEditable(false);
				vert.setDisLevel(null, false);
			} else if (e.getSource().equals(checkBoxOneShot)) {

				comboOneShot.setSelectedIndex(0);
				vert.setOneShot(null, false);
				comboOneShot.setEnabled(false);
			} else if (e.getSource().equals(checkBoxKeySS)) {

				textFieldKeySS.setEditable(false);
				textFieldKeySS.setText("0");
				vert.setKeySS(0, false);
			} else if (e.getSource().equals(checkBoxLog)) {

				comboLevel.setSelectedIndex(0);
				textFieldPathPrefix.setText(null);
				textFieldPathPrefix.setEditable(false);
				comboLevel.setEnabled(false);
				vert.setLogger(null, null, false);
				
				
				
			} else if (e.getSource().equals(checkBoxPrng)) {
				
				//System.out.println("disablingCheckBoxPrng");
				comboPrng.setSelectedIndex(0);
				comboPrng.setEnabled(false);
				vert.setPrng(null, false);
				
				//System.out.println("comboPrng " + comboPrng.isEditable());
			}
			else if (e.getSource().equals(checkBoxParam)) {

				vert.setParam(null, null, false);
				textFieldParamName.setEditable(false);
				textFieldParamValue.setEditable(false);
				addButton.setEnabled(false);
				remButton.setEnabled(false);
			} else if (e.getSource().equals(checkBoxResources)) {
				vert.setResource(null, false);
				textFieldResParamName.setEditable(false);
				textFieldResParamValue.setEditable(false);
				textFieldResHandler.setEditable(false);
				addResButton.setEnabled(false);
				remResButton.setEnabled(false);
				addResParamButton.setEnabled(false);
				remResParamButton.setEnabled(false);

			}

		}

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

		ListSelectionModel lsm = (ListSelectionModel) e.getSource();

		if (lsm.equals(listSelectionModelName))
			listParamValue.setSelectedIndex(listParamName.getSelectedIndex());
		else if (lsm.equals(listSelectionModelValue))
			listParamName.setSelectedIndex(listParamValue.getSelectedIndex());
		else if (lsm.equals(listSelectionModelResName))
			listResParamValue.setSelectedIndex(listResParamName
					.getSelectedIndex());
		else if (lsm.equals(listSelectionModelResValue))
			listResParamName.setSelectedIndex(listResParamValue
					.getSelectedIndex());

	}

	private int getIndexOS(String word) {

		int j = 0;

		// ricerca dell'index in choiceOS
		if (word != null) {
			for (int i = 0; i < choiceOS.length; i++) {

				if (choiceOS[i].compareTo(word.toUpperCase()) == 0) {

					break;
				}
				j++;
			}

		}
		return j;
	}

	private int getIndexLevel(String word) {

		int j = 0;

		// ricerca dell'index in choiceOS
		if (word != null) {

			for (int i = 0; i < choiceLevel.length; i++) {

				if (choiceLevel[i].compareTo(word) == 0) {

					break;
				}
				j++;
			}

		}

		return j;
	}
	
	private int getIndexPrng(String prng) {

		int j = 0;

		// ricerca dell'index in Prng
		if (prng != null) {

			for (int i = 0; i < choicePrng.length; i++) {

				if (choicePrng[i].compareTo(prng) == 0) {

					break;
				}
				j++;
			}

		}

		return j;
	}

}
