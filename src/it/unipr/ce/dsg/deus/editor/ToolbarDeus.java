package it.unipr.ce.dsg.deus.editor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;


/**
 * A class for the toolbar with icon for the GUI
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 * 
 */
public class ToolbarDeus extends JToolBar implements ActionListener,
		SetVisualizationView {

	private static final long serialVersionUID = 1L;
	private Marshalling marshGraph;
	private VisualizationViewer<DeusVertex, DeusEdge> visView;

	public ToolbarDeus() {

		JToolBar.Separator separator = new JToolBar.Separator(new Dimension(15,
				5));

		ImageIcon saveSession = new ImageIcon("icon/disk.png");
		JButton bSaveSession = new JButton(saveSession);
		bSaveSession.setBorder(new EmptyBorder(0, 5, 0, 5));
		bSaveSession.setToolTipText("Save Session");
		bSaveSession.setActionCommand("savesession");

		ImageIcon saveXml = new ImageIcon("icon/disk_xml.png");
		JButton bSaveXml = new JButton(saveXml);
		bSaveXml.setBorder(new EmptyBorder(0, 5, 0, 5));
		bSaveXml.setToolTipText("Save as XML for Deus");
		bSaveXml.setActionCommand("savexml");

		ImageIcon saveJpg = new ImageIcon("icon/image.png");
		JButton bSaveJpg = new JButton(saveJpg);
		bSaveJpg.setBorder(new EmptyBorder(0, 5, 0, 5));
		bSaveJpg.setToolTipText("Save a Screenshot");
		bSaveJpg.setActionCommand("savejpg");

		ImageIcon openFile = new ImageIcon("icon/open.png");
		JButton bopenFile = new JButton(openFile);
		bopenFile.setBorder(new EmptyBorder(0, 5, 0, 5));
		bopenFile.setToolTipText("Open File");
		bopenFile.setActionCommand("openfile");

		ImageIcon vertDelete = new ImageIcon("icon/graph_delete.png");
		JButton bvertDelete = new JButton(vertDelete);
		bvertDelete.setBorder(new EmptyBorder(0, 5, 0, 5));
		bvertDelete.setToolTipText("Delete all Vertices");
		bvertDelete.setActionCommand("deleteall");

		ImageIcon vertSelectDelete = new ImageIcon("icon/cross.png");
		JButton bvertSelDelete = new JButton(vertSelectDelete);
		bvertSelDelete.setBorder(new EmptyBorder(0, 5, 0, 5));
		bvertSelDelete.setToolTipText("Delete selected Vertices");
		bvertSelDelete.setActionCommand("delselectvert");

		ImageIcon exit = new ImageIcon("icon/exit.png");
		JButton bExit = new JButton(exit);
		bExit.setBorder(new EmptyBorder(0, 5, 0, 5));
		bExit.setToolTipText("Exit");
		bExit.setActionCommand("exit");

		bSaveSession.addActionListener(this);
		bSaveXml.addActionListener(this);
		bSaveJpg.addActionListener(this);
		bopenFile.addActionListener(this);
		bvertDelete.addActionListener(this);
		bvertSelDelete.addActionListener(this);
		bExit.addActionListener(this);

		add(bSaveSession);
		add(bSaveXml);
		add(bSaveJpg);
		add(separator);
		add(bopenFile);
		add(bvertDelete);
		add(bvertSelDelete);
		add(separator);
		add(bExit);

	}

	@Override
	public void setVisualizationView(
			VisualizationViewer<DeusVertex, DeusEdge> vv) {
		// TODO Auto-generated method stub
		visView = vv;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand() == "savexml") {
			marshGraph = new Marshalling(visView);

			JFileChooser chooser = new JFileChooser();
			chooser.addChoosableFileFilter(new XmlFilter("xml"));
			chooser.setCurrentDirectory(MainGUI.lastDirectory);
			int option = chooser.showSaveDialog(MainGUI.frame);
			if (option == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				MainGUI.lastDirectory = file.getParentFile();
				if (file.getName().endsWith(".xml") == false) {
					// *** add extension
					try {
						file = new File(file.getCanonicalPath() + ".xml");
					} catch (IOException ex) {

						ex.printStackTrace();
					}
				}
				marshGraph.createMarshallFile(file, true);
			}

		} else if (e.getActionCommand() == "savesession") {
			marshGraph = new Marshalling(visView);
			JFileChooser chooser = new JFileChooser();
			chooser.addChoosableFileFilter(new XmlFilter("bup"));
			chooser.setCurrentDirectory(MainGUI.lastDirectory);
			int option = chooser.showSaveDialog(MainGUI.frame);
			if (option == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				MainGUI.lastDirectory = file.getParentFile();
				if (file.getName().endsWith(".bup") == false) {
					// *** add extension
					try {
						file = new File(file.getCanonicalPath() + ".bup");
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
				marshGraph.createMarshallFile(file, false);

			}
		}

		else if (e.getActionCommand() == "savejpg") {
			JFileChooser chooser = new JFileChooser();
			chooser.addChoosableFileFilter(new ImgFilter());
			chooser.setCurrentDirectory(MainGUI.lastDirectory);
			int option = chooser.showSaveDialog(MainGUI.frame);
			if (option == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				MainGUI.lastDirectory = file.getParentFile();
				DeusUtility.writeJPEGImage(file, visView);
			}
		} else if (e.getActionCommand() == "openfile") {

			DeusUtility.openFile(visView);
		} else if (e.getActionCommand() == "deleteall") {

			Graph<DeusVertex, DeusEdge> graph = visView.getGraphLayout()
					.getGraph();
			int numVert = graph.getVertexCount();
			if (numVert != 0) {
				int n = JOptionPane.showConfirmDialog(MainGUI.frame,
						"Delete all vertices?", "Delete vertices",
						JOptionPane.YES_NO_OPTION);

				if (n == 0) {
					for (int i = 0; i < numVert; i++) {
						DeusVertex vert = graph.getVertices().iterator().next();
						graph.removeVertex(vert);
					}
					visView.repaint();
				}

			}
		} else if (e.getActionCommand() == "delselectvert") {

			Set<DeusVertex> vertSelectedSet = visView.getPickedVertexState()
					.getPicked();
			if (vertSelectedSet.size() != 0) {
				int n = JOptionPane.showConfirmDialog(MainGUI.frame,
						"Delete selected vertices?", "Delete vertices",
						JOptionPane.YES_NO_OPTION);

				if (n == 0) {
					for (DeusVertex v : vertSelectedSet) {
						visView.getGraphLayout().getGraph().removeVertex(v);
					}
					visView.repaint();
				}
			}
		} else if (e.getActionCommand() == "exit") {

			int numVert = visView.getGraphLayout().getGraph().getVertexCount();
			if (numVert != 0) {
				int n = JOptionPane.showConfirmDialog(MainGUI.frame,
						"The graph will be deleted. Exit?", "Exit",
						JOptionPane.YES_NO_OPTION);
				if (n == 0)
					System.exit(0);

			} else
				System.exit(0);
		}

	}

}
