package it.unipr.ce.dsg.deus.automator.gui;

import it.unipr.ce.dsg.deus.automator.DeusAutomatorException;
import it.unipr.ce.dsg.deus.automator.RunnerGui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * 
 */
@SuppressWarnings("serial")
public class DeusAutomatorFrame extends javax.swing.JFrame {

	private int simulationCount = 0;
	private JLabel removeSimulationLabel;
	private String originalXmlPath;

	private String outFileName = "automator.xml";

	private JFrame chooser_frame;
	private JFileChooser chooser;
	protected String fileName = null;
	protected String saveFileName;

	/** Creates new form DeusAutomator */
	public DeusAutomatorFrame() {
		initComponents();
	}

	public DeusAutomatorFrame(String originalXmlPath, String outFileName) {
		this.originalXmlPath = originalXmlPath;
		this.outFileName = outFileName;

		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		this.setTitle("Deus Automator - DSG Parma");
		/*
		 * Set the better look and feel for the running platform
		 */
		// Default System
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			// Win32
			try {
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception e2) {
				// Linux gtk
				try {
					UIManager
							.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
				} catch (Exception e3) {
					// MacOS
					try {
						UIManager
								.setLookAndFeel("javax.swing.plaf.mac.MacLookAndFeel");
					} catch (Exception e4) {
						try {
							// Cross platform metal
							UIManager.setLookAndFeel(UIManager
									.getCrossPlatformLookAndFeelClassName());
						} catch (Exception e5) {
							e5.printStackTrace();
							System.exit(1);
						}
					}
				}
			}

		}

		/*
		 * try { //UIManager.setLookAndFeel(
		 * "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		 * UIManager.setLookAndFeel
		 * ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"); } catch (Exception e)
		 * { // System.err.println("Unsupported Windows Look And Feel !");
		 * System.err.println("Unsupported GTK Look And Feel !"); }
		 */

		dsgLogoLabel = new javax.swing.JLabel();
		simulationTabbedPane = new javax.swing.JTabbedPane();
		openLabel = new javax.swing.JLabel();
		saveLabel = new javax.swing.JLabel();
		addSimulationLabel = new javax.swing.JLabel();
		simulationProgressBar = new javax.swing.JProgressBar();
		simulationStatusLabel = new javax.swing.JLabel();
		runLabel = new javax.swing.JLabel();
		removeSimulationLabel = new javax.swing.JLabel();

		/* @author Mirco Rosa (mirco.rosa.91@gmail.com) [Multithreading, Data Cleaning] */
		multithreadingCheckBox = new javax.swing.JCheckBox("Multithreading");
		clearResultsLabel = new javax.swing.JLabel();


		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setForeground(java.awt.Color.white);
		setResizable(true);

		dsgLogoLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13));
		dsgLogoLabel.setForeground(new java.awt.Color(204, 0, 0));
		dsgLogoLabel.setIcon(new javax.swing.ImageIcon(
				("res/dsgLogo_noBack_small.png"))); // NOI18N
		dsgLogoLabel.setText(" - Deus Automator");

		simulationTabbedPane.addTab("tab1", new DeusSimulationPanel(
				simulationTabbedPane));

		openLabel.setIcon(new javax.swing.ImageIcon(("res/open.png"))); // NOI18N
		openLabel.setText("Open");
		openLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				openLabelMousePressed(evt);
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				openLabelMouseReleased(evt);
			}

			public void mouseClicked(java.awt.event.MouseEvent evt) {
				openLabelMouseClicked(evt);
			}
		});

		saveLabel.setIcon(new javax.swing.ImageIcon(("res/save.png"))); // NOI18N
		saveLabel.setText("Save");
		saveLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				saveLabelMousePressed(evt);
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				saveLabelMouseReleased(evt);
			}

			public void mouseClicked(java.awt.event.MouseEvent evt) {
				saveLabelMouseClicked(evt);
			}
		});

		addSimulationLabel.setIcon(new javax.swing.ImageIcon(("res/add.png"))); // NOI18N
		addSimulationLabel.setText("Add Simulation Tab");
		addSimulationLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				addSimulationLabelMousePressed(evt);
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				addSimulationLabelMouseReleased(evt);
			}

			public void mouseClicked(java.awt.event.MouseEvent evt) {
				addSimulationLabelMouseClicked(evt);
			}
		});

		simulationStatusLabel.setText("Simulation Status :");

		removeSimulationLabel.setIcon(new javax.swing.ImageIcon(
				"res/remove.png")); // NOI18N
		removeSimulationLabel.setText("Remove Simulation Tab");
		removeSimulationLabel
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mousePressed(java.awt.event.MouseEvent evt) {
						removeSimulationLabelMousePressed(evt);
					}

					public void mouseReleased(java.awt.event.MouseEvent evt) {
						removeSimulationLabelMouseReleased(evt);
					}

					public void mouseClicked(java.awt.event.MouseEvent evt) {
						removeSimulationLabelMouseClicked(evt);
					}
				});

		runLabel.setIcon(new javax.swing.ImageIcon(("res/run.png"))); // NOI18N
		runLabel.setText("Run");
		runLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				runLabelMousePressed(evt);
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				runLabelMouseReleased(evt);
			}

			public void mouseClicked(java.awt.event.MouseEvent evt) {
				try {
					runLabelMouseClicked(evt);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		/* @author Mirco Rosa (mirco.rosa.91@gmail.com) [Data Cleaning] */
		clearResultsLabel.setIcon(new javax.swing.ImageIcon(("res/Delete_01.png"))); // NOI18N
		clearResultsLabel.setText("Clear Results");
		clearResultsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				clearResultsLabelMousePressed(evt);
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				clearResultsLabelMouseReleased(evt);
			}

			public void mouseClicked(java.awt.event.MouseEvent evt) {
				clearResultsLabelMouseClicked(evt);
			}
		});


		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout
						.createSequentialGroup()
						.addContainerGap()
						.add(layout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.LEADING)
								.add(simulationTabbedPane,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										1100, Short.MAX_VALUE)
								.add(dsgLogoLabel)
								.add(layout
										.createSequentialGroup()
										.add(openLabel)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(saveLabel)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(addSimulationLabel)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(removeSimulationLabel)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.UNRELATED)
										.add(clearResultsLabel)
										.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
										.add(runLabel)
										.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
										.add(multithreadingCheckBox)
										.addPreferredGap(
										org.jdesktop.layout.LayoutStyle.RELATED,
										276, Short.MAX_VALUE)
										.add(simulationStatusLabel)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.UNRELATED)
										.add(simulationProgressBar,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												261,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout
						.createSequentialGroup()
						.add(layout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.TRAILING)
								.add(layout
										.createSequentialGroup()
										.add(dsgLogoLabel)
										.add(18, 18, 18)
										.add(layout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.BASELINE)
												.add(openLabel).add(saveLabel)
												.add(addSimulationLabel)
												.add(simulationStatusLabel)
												.add(removeSimulationLabel)
												.add(clearResultsLabel)
												.add(runLabel)
										.add(multithreadingCheckBox)))
								.add(simulationProgressBar,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(
								org.jdesktop.layout.LayoutStyle.RELATED)
//						.add(simulationTabbedPane,
//								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
//								600,
//								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.add(simulationTabbedPane,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								550, Short.MAX_VALUE)
						.addContainerGap(15, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	protected void removeSimulationLabelMouseClicked(MouseEvent evt) {
		int index = this.simulationTabbedPane.getSelectedIndex();
		this.simulationTabbedPane.remove(index);
	}

	protected void removeSimulationLabelMouseReleased(MouseEvent evt) {
		removeSimulationLabel.setIcon(new javax.swing.ImageIcon(
				"res/remove.png"));

	}

	protected void removeSimulationLabelMousePressed(MouseEvent evt) {
		removeSimulationLabel.setIcon(new javax.swing.ImageIcon(
				"res/remove_BN.png"));

	}


	/* @author Mirco Rosa (mirco.rosa.91@gmail.com) [Data Cleaning] */
	protected void clearResultsLabelMouseClicked(MouseEvent evt) {
		clearResults();
	}

	protected void clearResultsLabelMouseReleased(MouseEvent evt) {
		//Does nothing
	}

	protected void clearResultsLabelMousePressed(MouseEvent evt) {
		//Does nothing
	}

	private void openLabelMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_openLabelMouseClicked

		chooser_frame = new JFrame();
		chooser_frame.setBounds(0, 0, 450, 450);
		chooser = new JFileChooser();

		chooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chooser.getSelectedFile() != null) {
					String app = chooser.getSelectedFile().toString();

					if (app.indexOf('/') != -1)
						app = app + "/";
					else
						app = app + "\\";

					fileName = app;

					if (fileName != null) {
						chooser_frame.removeAll();
						chooser_frame.setVisible(false);

						clearAllSimulation();

						// Set the title of the window
						setTitle("Deus Automator - DSG Parma " + fileName);

						try {
							readXML(fileName);
						} catch (DeusAutomatorException e1) {
							e1.printStackTrace();
						} catch (JAXBException e1) {
							e1.printStackTrace();
						} catch (SAXException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				} else
					chooser_frame.dispose();
			}
		});

		chooser_frame.setLayout(new BorderLayout());
		chooser_frame.add(chooser, BorderLayout.NORTH);
		chooser.setVisible(true);
		chooser_frame.setVisible(true);

	}// GEN-LAST:event_openLabelMouseClicked

	private void clearAllSimulation() {

		for (int index = 0; index < this.simulationTabbedPane
				.getComponentCount(); index++) {
			((DeusSimulationPanel) this.simulationTabbedPane
					.getComponentAt(index)).clearAllData();
		}

		// Close all active tabs of the simulations
		this.simulationTabbedPane.removeAll();
	}

	private void openLabelMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_openLabelMousePressed
		openLabel.setIcon(new javax.swing.ImageIcon(("res/open_BN.png")));
	}// GEN-LAST:event_openLabelMousePressed

	private void openLabelMouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_openLabelMouseReleased
		openLabel.setIcon(new javax.swing.ImageIcon(("res/open.png")));
	}// GEN-LAST:event_openLabelMouseReleased

	private void saveLabelMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_saveLabelMouseClicked

		chooser_frame = new JFrame();
		chooser_frame.setBounds(0, 0, 450, 450);
		chooser = new JFileChooser();
		int result = chooser.showSaveDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {

			if (chooser.getSelectedFile() != null) {
				String app = chooser.getSelectedFile().toString();

				if (app.indexOf('/') != -1)
					app = app + "/";
				else
					app = app + "\\";

				saveFileName = app;

				if (saveFileName != null) {
					chooser_frame.removeAll();
					chooser_frame.setVisible(false);

					try {
						writeAutomatorXML(saveFileName);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				} else
					chooser_frame.dispose();
			}
		}

		/*
		 * chooser.addActionListener ( new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { System.out.println("Salvo");
		 * 
		 * if(chooser.getSelectedFile() != null) { String app =
		 * chooser.getSelectedFile().toString();
		 * 
		 * if( app.indexOf('/') != -1 ) app = app + "/"; else app = app + "\\";
		 * 
		 * saveFileName = app;
		 * 
		 * if(saveFileName != null) { chooser_frame.removeAll();
		 * chooser_frame.setVisible(false);
		 * 
		 * try { writeAutomatorXML(saveFileName); } catch (IOException e1) {
		 * e1.printStackTrace(); }
		 * 
		 * } else chooser_frame.dispose(); } }});
		 */

		chooser_frame.setLayout(new BorderLayout());
		chooser_frame.add(chooser, BorderLayout.NORTH);
		chooser.setVisible(true);
		// chooser_frame.setVisible(true);
	}// GEN-LAST:event_saveLabelMouseClicked

	private void saveLabelMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_saveLabelMousePressed
		saveLabel.setIcon(new javax.swing.ImageIcon(("res/save_BN.png")));
	}// GEN-LAST:event_saveLabelMousePressed

	private void saveLabelMouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_saveLabelMouseReleased
		saveLabel.setIcon(new javax.swing.ImageIcon(("res/save.png")));
	}// GEN-LAST:event_saveLabelMouseReleased

	private void addSimulationLabelMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_addSimulationLabelMouseClicked
		simulationCount++;
		simulationTabbedPane.addTab("Sim" + simulationCount,
				new DeusSimulationPanel(simulationTabbedPane));
	}// GEN-LAST:event_addSimulationLabelMouseClicked

	private void addSimulationLabelMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_addSimulationLabelMousePressed
		addSimulationLabel
				.setIcon(new javax.swing.ImageIcon(("res/add_BN.png")));
	}// GEN-LAST:event_addSimulationLabelMousePressed

	private void addSimulationLabelMouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_addSimulationLabelMouseReleased
		addSimulationLabel.setIcon(new javax.swing.ImageIcon(("res/add.png")));
	}// GEN-LAST:event_addSimulationLabelMouseReleased

	private void runLabelMouseClicked(java.awt.event.MouseEvent evt)
			throws IOException {// GEN-FIRST:event_runLabelMouseClicked

		if(checkOldData())
			switch (JOptionPane.showConfirmDialog(this,"Results or temporary files from previous simulations have been detected.\nDo you want to remove them? This operation can't be undone.","Clear Results and Temporary files",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)) {
				case 0:
					clearData();
					break;
				case 1:
					//No data deleted
					break;
			}
		
		writeAutomatorXML(this.outFileName);

		// Run saved file
		//Runner runner = new Runner(this.originalXmlPath, this.outFileName);
		RunnerGui runner = new RunnerGui(this.originalXmlPath, this.outFileName);
		runner.setMultithreading(multithreadingCheckBox.isSelected()); /*@author Mirco Rosa (mirco.rosa.91@gmail.com) [multithreading]*/
		
		//check for gnuplot incompatibility
		boolean gnuPlotCheck = runner.checkGnuPlotIncompatibility();
		if (!gnuPlotCheck){
			System.err.println("GnuPlot config error");
			JOptionPane.showMessageDialog(null, "DEUS-GnuPlot configuration error.\nAt least one among X and Y must be a valid log variable.", "DEUS-GnuPlot configuration error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
		runner.setSimulationProgressBar(simulationProgressBar);

		Thread automatorRunner = new Thread(runner, "Automator Thread Runner");
		automatorRunner.setPriority(10);
		automatorRunner.start();
	}// GEN-LAST:event_runLabelMouseClicked

	private void writeAutomatorXML(String fileName) throws IOException {
		int tabCount = simulationTabbedPane.getTabCount();

		String xmlString = "";

		xmlString = xmlString + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "\n\n";
		xmlString = xmlString
				+ "<deusAutomator xmlns=\"http://dsg.ce.unipr.it/software/deus/schema/deusAutomator\""
				+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
				+ " xsi:schemaLocation=\"http://dsg.ce.unipr.it/software/deus/schema/deusAutomator ../../schema/automator/deusAutomator.xsd\">"
				+ "\n\n";

		for (int i = 0; i < tabCount; i++){
			//@Stefano: check to allow to don't specify any seed from GUI if a seed is already specified in the xml
			//System.out.println("check tab-pane");
			//System.out.println(((DeusSimulationPanel) simulationTabbedPane.getComponent(i)).getEngineParameterList().size());
			if ( ((DeusSimulationPanel) simulationTabbedPane.getComponent(i)).getEngineParameterList().size() == 0){
				
				//if no seed is specified from the gui check if there is a seed specified in the simulation xml
				//System.out.println("scannerOf " + this.originalXmlPath);
				//System.out.println("NO seed has been specified from the AutomatorGUI. Looking in the configuration xml...");
				this.originalXmlPath.replace("\\", File.separator);
				//System.out.println("scannerOf2 " + this.originalXmlPath);
				Scanner scanner = new Scanner(new File(this.originalXmlPath));
				while (scanner.hasNextLine()) {
				   final String lineFromFile = scanner.nextLine();
				  // System.out.println("line " + lineFromFile);
				   if(lineFromFile.contains("<aut:engine")) { 
					   
					   String[] seed = StringUtils.substringsBetween(lineFromFile , "seed=\"", "\"");
					   if (seed.length > 0){
						  // System.out.println("seed from config xml is " + seed[0]);
						   ((DeusSimulationPanel) simulationTabbedPane.getComponent(i)).getEngineParameterList().add(new EngineParameter(seed[0]));
					   }
//					   Pattern pattern = Pattern.compile(".*seed=\"(.*)\".*");
//					   Matcher matcher = pattern.matcher(lineFromFile);
//					   if (matcher.find())
//					   {
//						   System.out.println("seed from configuration xml is " + matcher.group(1));
//						   ((DeusSimulationPanel) simulationTabbedPane.getComponent(i)).getEngineParameterList().add(new EngineParameter(matcher.group(1)));
//					   }
//
				       break;
				   }
				
				//this.originalXmlPath
				}
				scanner.close();
			}
			
			xmlString = xmlString
					+ ((DeusSimulationPanel) simulationTabbedPane
							.getComponent(i)).createSimulationXML(
							simulationTabbedPane.getTitleAt(i), i) + "\n";
		}
			
			
		xmlString = xmlString + "</deusAutomator>";

		// System.out.println(xmlString);

		FileOutputStream fos = new FileOutputStream(fileName);

		fos.write(xmlString.getBytes());

	}

	private void runLabelMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_runLabelMousePressed
		runLabel.setIcon(new javax.swing.ImageIcon(("res/run_BN.png")));
	}// GEN-LAST:event_runLabelMousePressed

	private void runLabelMouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_runLabelMouseReleased
		runLabel.setIcon(new javax.swing.ImageIcon(("res/run.png")));
	}// GEN-LAST:event_runLabelMouseReleased

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				DeusAutomatorFrame deusAutomatorFrame = new DeusAutomatorFrame(
						args[0], args[1]);
				deusAutomatorFrame.setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel addSimulationLabel;
	private javax.swing.JLabel dsgLogoLabel;
	private javax.swing.JLabel openLabel;
	private javax.swing.JLabel runLabel;
	private javax.swing.JLabel saveLabel;
	private javax.swing.JProgressBar simulationProgressBar;
	private javax.swing.JLabel simulationStatusLabel;
	private javax.swing.JTabbedPane simulationTabbedPane;

	/* @author Mirco Rosa (mirco.rosa.91@gmail.com) [Multithreading, Data Cleaning] */
	private JCheckBox multithreadingCheckBox;
	private javax.swing.JLabel clearResultsLabel;


	// End of variables declaration//GEN-END:variables
	public String getOriginalXmlPath() {
		return originalXmlPath;
	}

	public void setOriginalXmlPath(String originalXmlPath) {
		this.originalXmlPath = originalXmlPath;
	}

	public String getOutFileName() {
		return outFileName;
	}

	public void setOutFileName(String outFileName) {
		this.outFileName = outFileName;
	}

	/**
	 * Funzione che si occupa di leggere il file .xml per l'automazione delle
	 * simulazioni
	 * 
	 * @param path
	 *            , percorso del file da leggere
	 * @return un ArrayList<> contenete tutte le varie simulazioni da effettuare
	 * @throws DeusAutomatorException
	 * @throws JAXBException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void readXML(String path) throws DeusAutomatorException,
			JAXBException, SAXException, IOException {

		JAXBContext jc = JAXBContext
				.newInstance("it.unipr.ce.dsg.deus.schema.automator");
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema = schemaFactory.newSchema(new File(
				"schema/automator/deusAutomator.xsd"));

		Unmarshaller unmarshaller = jc.createUnmarshaller();
		unmarshaller.setSchema(schema);
		unmarshaller.setEventHandler(new ValidationEventHandler() {

			public boolean handleEvent(ValidationEvent ve) {
				if (ve.getSeverity() == ValidationEvent.FATAL_ERROR
						|| ve.getSeverity() == ValidationEvent.ERROR
						|| ve.getSeverity() == ValidationEvent.WARNING) {
					ValidationEventLocator locator = ve.getLocator();
					System.out.println("Invalid configuration file: "
							+ locator.getURL());
					System.out.println("Error at column "
							+ locator.getColumnNumber() + ", line "
							+ locator.getLineNumber());
					System.out.println("Error: " + ve.getMessage());
					return false;
				}
				return true;
			}

		});

		unmarshaller.unmarshal(new File(path));

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();

			File f = new File(path);

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(f);

			document.getDocumentElement().normalize();

			// Root element
			NodeList simulationLst = document
					.getElementsByTagName("simulation");

			// list of simulations
			for (int w = 0; w < simulationLst.getLength(); w++) {

				// DeusSimulationPanel on which values read from file are added
				DeusSimulationPanel deusSimulationPanel = null;

				Node fstSimulation = simulationLst.item(w);

				if (fstSimulation.getAttributes()
						.getNamedItem("simulationName").getNodeValue() == null
						|| fstSimulation.getAttributes()
								.getNamedItem("simulationNumberSeed")
								.getNodeValue() == null)
					throw new DeusAutomatorException(
							"Errore manca simulationNumberSeed e/o simulationName nel tag simulation");

				// String resultFolder = null;
				// String inputFolder = null;
				//
				// String simulationNumberSeed =
				// fstSimulation.getAttributes().getNamedItem("simulationNumberSeed").getNodeValue();

				// name of the simulation
				String simulationName = fstSimulation.getAttributes()
						.getNamedItem("simulationName").getNodeValue();

				// Create new Panel and set it to be modified with new data read
				// from the XML file
				deusSimulationPanel = this.addSimulationPanel(simulationName);

				// if(fstSimulation.getAttributes().getNamedItem("resultFolder")
				// != null)
				// resultFolder =
				// fstSimulation.getAttributes().getNamedItem("resultFolder").getNodeValue();
				//
				// if(fstSimulation.getAttributes().getNamedItem("inputFolder")
				// != null)
				// inputFolder =
				// fstSimulation.getAttributes().getNamedItem("inputFolder").getNodeValue();

				NodeList nodeLst = document.getElementsByTagName("node");

				// list of nodes
				for (int s = 0; s < nodeLst.getLength(); s++) {

					Node fstNode = nodeLst.item(s);

					if (fstNode.getParentNode().equals(simulationLst.item(w))) {

						// name of the node
						String messageType = fstNode.getAttributes()
								.getNamedItem("id").getNodeValue();

						Element fstElmnt = (Element) fstNode;
						NodeList fstNmElmntLst = fstElmnt
								.getElementsByTagName("paramName");

						// Retrieve all params in node's ParamName
						for (int j = 0; j < fstNmElmntLst.getLength(); j++) {

							Element paramElement = (Element) fstNmElmntLst
									.item(j);

							// name of the param
							String paramName = ((Node) fstNmElmntLst.item(j))
									.getAttributes().getNamedItem("name")
									.getNodeValue();

							NodeList initialValue = paramElement
									.getElementsByTagName("initialValue");

							NodeList finalValue = paramElement
									.getElementsByTagName("finalValue");

							NodeList stepValue = paramElement
									.getElementsByTagName("stepValue");

							if (initialValue == null || finalValue == null
									|| stepValue == null) {
								throw new DeusAutomatorException(
										"Errore in initalValue , finalValue e stepValue in "
												+ simulationName + " di Node "
												+ messageType + " in "
												+ paramName);
							}

							// value of params
							Double init = Double.parseDouble(initialValue.item(
									0).getTextContent());
							Double fin = Double.parseDouble(finalValue.item(0)
									.getTextContent());
							Double step = Double.parseDouble(stepValue.item(0)
									.getTextContent());

							// create new object to be inserted in the GUI
							NodeParameter nodeParameter = new NodeParameter(
									fin, init, messageType, paramName, step);

							deusSimulationPanel.addNodeParameter(nodeParameter);

						}

						NodeList paramName = fstElmnt
								.getElementsByTagName("resourceParamName");

						// retrieve all params in node's resourceParamName
						for (int j = 0; j < paramName.getLength(); j++) {

							Element paramElement = (Element) paramName.item(j);

							// handler name
							String handlerName = ((Node) paramName.item(j))
									.getAttributes()
									.getNamedItem("handlerName").getNodeValue();

							// resource param name
							String resParamValueName = ((Node) paramName
									.item(j)).getAttributes()
									.getNamedItem("resParamValue")
									.getNodeValue();

							NodeList initialValue = paramElement
									.getElementsByTagName("initialValue");

							NodeList finalValue = paramElement
									.getElementsByTagName("finalValue");

							NodeList stepValue = paramElement
									.getElementsByTagName("stepValue");

							if (initialValue == null || finalValue == null
									|| stepValue == null) {
								throw new DeusAutomatorException(
										"Errore in initalValue , finalValue e stepValue in "
												+ simulationName + " di Node"
												+ messageType + " in "
												+ paramName);
							}

							// values of params
							Double init = Double.parseDouble(initialValue.item(
									0).getTextContent());
							Double fin = Double.parseDouble(finalValue.item(0)
									.getTextContent());
							Double step = Double.parseDouble(stepValue.item(0)
									.getTextContent());

							NodeResource nodeResource = new NodeResource(fin,
									handlerName, init, messageType,
									resParamValueName, step);

							deusSimulationPanel.addNodeResource(nodeResource);

						}

					}

				}

				NodeList processLst = document.getElementsByTagName("process");

				// list of processes
				for (int s = 0; s < processLst.getLength(); s++) {

					Node fstNode = processLst.item(s);

					if (fstNode.getParentNode().equals(simulationLst.item(w))) {

						// process name
						String messageType = fstNode.getAttributes()
								.getNamedItem("id").getNodeValue();

						Element fstElmnt = (Element) fstNode;
						NodeList fstNmElmntLst = fstElmnt
								.getElementsByTagName("paramName");

						// Retrieve all params in process' ParamName
						for (int j = 0; j < fstNmElmntLst.getLength(); j++) {

							Element paramElement = (Element) fstNmElmntLst
									.item(j);

							// name of process param
							String paramName = ((Node) fstNmElmntLst.item(j))
									.getAttributes().getNamedItem("name")
									.getNodeValue();

							NodeList initialValue = paramElement
									.getElementsByTagName("initialValue");

							NodeList finalValue = paramElement
									.getElementsByTagName("finalValue");

							NodeList stepValue = paramElement
									.getElementsByTagName("stepValue");

							// values of params
							Double init = Double.parseDouble(initialValue.item(
									0).getTextContent());
							Double fin = Double.parseDouble(finalValue.item(0)
									.getTextContent());
							Double step = Double.parseDouble(stepValue.item(0)
									.getTextContent());

							ProcessParameter processParameter = new ProcessParameter(
									messageType, paramName, init, fin, step);

							deusSimulationPanel
									.addProcessParameter(processParameter);
						}

					}

				}


				/* @author Mirco Rosa (mirco.rosa.91@gmail.com) [Event Parametrization] */
				NodeList eventList = document.getElementsByTagName("event");

				// list of events
				for (int s = 0; s < eventList.getLength(); s++) {

					Node fstNode = eventList.item(s);

					if (fstNode.getParentNode().equals(simulationLst.item(w))) {

						// event name
						String messageType = fstNode.getAttributes()
								.getNamedItem("id").getNodeValue();

						Element fstElmnt = (Element) fstNode;
						NodeList fstNmElmntLst = fstElmnt
								.getElementsByTagName("paramName");

						// Retrieve all params in event' ParamName
						for (int j = 0; j < fstNmElmntLst.getLength(); j++) {

							Element paramElement = (Element) fstNmElmntLst
									.item(j);

							// name of event param
							String paramName = ((Node) fstNmElmntLst.item(j))
									.getAttributes().getNamedItem("name")
									.getNodeValue();

							NodeList initialValue = paramElement
									.getElementsByTagName("initialValue");

							NodeList finalValue = paramElement
									.getElementsByTagName("finalValue");

							NodeList stepValue = paramElement
									.getElementsByTagName("stepValue");

							// values of params
							Double init = Double.parseDouble(initialValue.item(
									0).getTextContent());
							Double fin = Double.parseDouble(finalValue.item(0)
									.getTextContent());
							Double step = Double.parseDouble(stepValue.item(0)
									.getTextContent());

							EventParameter eventParameter = new EventParameter(
									messageType, paramName, init, fin, step);

							deusSimulationPanel
									.addEventParameter(eventParameter);
						}

					}

				}
				///////////////////////////////


				NodeList engineLst = document.getElementsByTagName("engine");

				// engine
				for (int s = 0; s < engineLst.getLength(); s++) {

					Node fstNode = engineLst.item(s);

					if (fstNode.getParentNode().equals(simulationLst.item(w))) {

						/*
						 * // the following lines are useless [Michele] String
						 * startVt = ""; String endVt = ""; String stepVt = "";
						 * 
						 * boolean vt = true;
						 * if(fstNode.getAttributes().getNamedItem("startVT") !=
						 * null ) startVt =
						 * fstNode.getAttributes().getNamedItem(
						 * "startVT").getNodeValue(); else vt = false;
						 * 
						 * if(fstNode.getAttributes().getNamedItem("endVT") !=
						 * null ) endVt =
						 * fstNode.getAttributes().getNamedItem("endVT"
						 * ).getNodeValue(); else vt = false;
						 * 
						 * if(fstNode.getAttributes().getNamedItem("stepVT") !=
						 * null ) stepVt =
						 * fstNode.getAttributes().getNamedItem("stepVT"
						 * ).getNodeValue(); else vt = false;
						 */

						Element fstElmnt = (Element) fstNode;
						NodeList fstNmElmntLst = fstElmnt
								.getElementsByTagName("seed");

						// Retrieve all seedValues in seed
						for (int j = 0; j < fstNmElmntLst.getLength(); j++) {

							Element paramElement = (Element) fstNmElmntLst
									.item(j);

							NodeList seedValue = paramElement
									.getElementsByTagName("seedValue");

							for (int o = 0; o < seedValue.getLength(); o++) {
								// values of the seeds
								String seedvalue = seedValue.item(o)
										.getTextContent();

								EngineParameter engineParameter = new EngineParameter(
										seedvalue);

								deusSimulationPanel
										.addEngineParameter(engineParameter);
							}

						}

					}

				}

				// NodeList resultLogLst =
				// document.getElementsByTagName("resultVT");
				//
				// for (int i = 0; i < resultLogLst.getLength(); i++) {
				//
				// Node fileLog = resultLogLst.item(i);
				//
				// if(fileLog.getParentNode().equals(simulationLst.item(w))){
				//
				// sim.setFileLog(fileLog.getAttributes().getNamedItem("outputLogFile").getNodeValue());
				// }
				//
				// }
				//
				// GNUPLOT
				NodeList GnuPlotLst = document
						.getElementsByTagName("resultXYFile");

				for (int i = 0; i < GnuPlotLst.getLength(); i++) {

					Node GnuPlotNode = GnuPlotLst.item(i);

					if (GnuPlotNode.getParentNode().equals(
							simulationLst.item(w))) {

						String fileName = GnuPlotNode.getAttributes()
								.getNamedItem("fileName").getNodeValue();

						String asseX = GnuPlotNode.getAttributes()
								.getNamedItem("axisX").getNodeValue();

						String asseY = GnuPlotNode.getAttributes()
								.getNamedItem("axisY").getNodeValue();

						GnuPlotFileElement gnuPlotFileElement = new GnuPlotFileElement(
								fileName, asseX, asseY);

						deusSimulationPanel
								.addGnuPlotFileElement(gnuPlotFileElement);
					}

				}

			}

		} catch (SAXException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		}

	}

	public DeusSimulationPanel addSimulationPanel(String simulationName) {

		int oldIndex = this.simulationTabbedPane.indexOfTab(simulationName);

		if (oldIndex == -1) {
			// Add simulation Tab
			simulationCount++;
			simulationTabbedPane.addTab(simulationName,
					new DeusSimulationPanel(simulationTabbedPane));

		} else {
			JFrame errorFrame = new JFrame();
			errorFrame.setTitle("ERROR !");
			errorFrame.setBounds(0, 0, 350, 80);
			errorFrame.setVisible(true);

			errorFrame
					.add(new JLabel(
							"ERROR ! Simulations with the same name ! Only the first was loaded "));
		}

		int tabIndex = this.simulationTabbedPane.indexOfTab(simulationName);

		return (DeusSimulationPanel) this.simulationTabbedPane
				.getComponentAt(tabIndex);
	}

	/* @author Mirco Rosa (mirco.rosa.91@gmail.com) [Data Cleaning] */
	private void clearResults() {
		if(checkOldData())
			switch (JOptionPane.showConfirmDialog(this,"Do you want to remove all the results and temporary files of previous simulations?\nThis operation can't be undone.","Clear Results and Temporary files",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)) {
				case 0:
					clearData();
					break;
				case 1:
					//No data deleted
					break;
			}
		else
			JOptionPane.showMessageDialog(this,"No old results or temporary files to be deleted.","Clear Results",JOptionPane.INFORMATION_MESSAGE);

	}

	private boolean checkOldData() {
		File results = new File("results");
		File gnuplot = new File("results/gnuplot");
		File temp = new File("temp");
		File xml = new File("xml");

		return !((results.exists() ? results.listFiles().length : 1) == 1 &&
				(gnuplot.exists() ? gnuplot.listFiles().length : 0) == 0 &&
				(temp.exists() ? temp.listFiles().length : 0) == 0 &&
				(xml.exists() ? xml.listFiles().length : 0) == 0);
	}

	private void clearData() {
		File results = new File("results");
		File gnuplot = new File("results/gnuplot");
		File temp = new File("temp");
		File xml = new File("xml");

		if(results.exists())
			for(File file : results.listFiles())
				if(!file.isDirectory())
					file.delete();
		if(gnuplot.exists())
			for(File file : gnuplot.listFiles())
				file.delete();
		if(temp.exists())
			for(File file : temp.listFiles())
				file.delete();
		if(xml.exists())
			for(File file : xml.listFiles())
				file.delete();
		System.out.println("Data cleared.");
	}

}
