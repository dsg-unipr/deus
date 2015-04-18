package it.unipr.ce.dsg.deus.editor;

import it.unipr.ce.dsg.deus.editor.MenuPanelRight;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.algorithms.layout.util.VisRunner;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;

import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.util.Animator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.Factory;


/**
 * Main class
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 */
public class MainGUI {

	Factory<DeusVertex> vertexFactory;
	Factory<DeusEdge> edgeFactory;

	SpringLayout<DeusVertex, DeusEdge> springLayout;
	FRLayout<DeusVertex, DeusEdge> frLayout;
	ISOMLayout<DeusVertex, DeusEdge> isomLayout;
	CircleLayout<DeusVertex, DeusEdge> circleLayout;
	DAGLayout<DeusVertex, DeusEdge> dagLayout;
	EditingModalGraphMouse<DeusVertex, DeusEdge> gm;
	VisualizationViewer<DeusVertex, DeusEdge> vv;

	private Graph<DeusVertex, DeusEdge> graph;
	private GraphZoomScrollPane scrollPane;
	private Marshalling marshGraph;
	private String[] typeLayout = { "Static", "DAGLayout", "FRLayout",
			"ISOMLayout", "CircleLayout", "Spring" };
	private JPanel jpRight;
	static JFrame frame;
	protected static File lastDirectory;
	protected static Layout<DeusVertex, DeusEdge> layout;
	protected static String VERTEX_TYPE = "Event";

	/**
	 * Class constructor that instantiate VisualizationViewer, Graph, factory
	 * and layout. Manages the principal graphic panel such as toolbar, right
	 * panel and menu.
	 * 
	 */
	public MainGUI() {

		graph = new DirectedSparseMultigraph<DeusVertex, DeusEdge>();

		vertexFactory = new Factory<DeusVertex>() {
			// Vertex factory
			@Override
			public DeusVertex create() {
				DeusVertex newVertex = new DeusVertex(VERTEX_TYPE);

				return newVertex;
			}

		};

		edgeFactory = new Factory<DeusEdge>() {
			// edge factory
			public DeusEdge create() {

				DeusEdge newEdge = new DeusEdge();
				return newEdge;
			}
		};

		// Layout<V, E>, VisualizationComponent<V,E>
		layout = new StaticLayout<DeusVertex, DeusEdge>(graph, new Dimension(
				700, 550));
		// Spring Layout
		springLayout = new SpringLayout<DeusVertex, DeusEdge>(graph);
		// FR Layout Layout
		frLayout = new FRLayout<DeusVertex, DeusEdge>(graph);
		// ISOM Layout
		isomLayout = new ISOMLayout<DeusVertex, DeusEdge>(graph);
		// Circle Layout
		circleLayout = new CircleLayout<DeusVertex, DeusEdge>(graph);
		// DAG Layout
		dagLayout = new DAGLayout<DeusVertex, DeusEdge>(graph);

		vv = new VisualizationViewer<DeusVertex, DeusEdge>(layout);
		vv.setPreferredSize(new Dimension(700, 550));
		vv.setBackground(Color.white);
		// Applico un colore per ogni tipo di vertice
		Transformer<DeusVertex, Paint> vertexPaint = new Transformer<DeusVertex, Paint>() {
			public Paint transform(DeusVertex i) {
				if (i.getElementType() == "Process") {
					return Color.GREEN;
				} else if (i.getElementType() == "Event") {
					return Color.CYAN;
				} else if (i.getElementType() == "Engine") {

					return Color.ORANGE;
				} else if (i.getElementType() == "Network") {

					return Color.LIGHT_GRAY;
				} else
					return Color.RED;
			}
		};

		gm = new EditingModalGraphMouse<DeusVertex, DeusEdge>(
				vv.getRenderContext(), vertexFactory, edgeFactory);

		vv.setGraphMouse(gm);
		gm.setMode(ModalGraphMouse.Mode.EDITING);

		vv.addKeyListener(gm.getModeKeyListener());
		VertexShape vRect = new VertexShape();

		vv.getRenderer().getVertexLabelRenderer()
				.setPosition(Renderer.VertexLabel.Position.NE);
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setVertexLabelTransformer(
				new ToStringLabeller<DeusVertex>());
		vv.getRenderContext().setEdgeLabelTransformer(
				new ToStringLabeller<DeusEdge>());
		vv.getRenderContext().setVertexShapeTransformer(vRect);

		// design graph in frame panel
		scrollPane = new GraphZoomScrollPane(vv);

		vv.addMouseListener(new CheckLinkMouseListener());

		frame.getContentPane().add(scrollPane);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				exitCheck();

			}
		});

		// design swing panel for JUNG
		designPanelControl();

	}

	private void designPanelControl() {

		// *** Menu ***

		JMenuBar menuBar = new JMenuBar();

		// viene rimosso il menu di default
		gm.remove(gm.getPopupEditingPlugin());

		// menu per vertex e edge
		MenuManagePopup<DeusVertex, DeusEdge> popupMenuItem = new MenuManagePopup<DeusVertex, DeusEdge>();

		JPopupMenu vertexMenu = new MenuItemVertex(frame);
		JPopupMenu edgeMenu = new MenuItemEdge();
		popupMenuItem.setVertexPopup(vertexMenu);
		popupMenuItem.setEdgePopup(edgeMenu);

		gm.add(popupMenuItem);

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		JMenuItem openFile = new JMenuItem("Open File");
		openFile.setMnemonic(KeyEvent.VK_O);

		openFile.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				DeusUtility.openFile(vv);

			}
		});

		JMenuItem parzialXml = new JMenuItem("Save Session");
		parzialXml.setMnemonic(KeyEvent.VK_S);

		parzialXml.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				marshGraph = new Marshalling(vv);

				JFileChooser chooser = new JFileChooser();
				chooser.addChoosableFileFilter(new XmlFilter("bup"));
				chooser.setCurrentDirectory(lastDirectory);
				int option = chooser.showSaveDialog(frame);

				if (option == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					lastDirectory = file.getParentFile();

					if (file.getName().endsWith(".bup") == false) {

						// *** add extension
						try {
							file = new File(file.getCanonicalPath() + ".bup");
						} catch (IOException e) {

							e.printStackTrace();
						}
					}

					marshGraph.createMarshallFile(file, false);

				}
			}
		});

		JMenuItem fileXml = new JMenuItem("Save as Xml");
		fileXml.setMnemonic(KeyEvent.VK_X);
		fileXml.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				marshGraph = new Marshalling(vv);

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(lastDirectory);
				chooser.addChoosableFileFilter(new XmlFilter("xml"));
				int option = chooser.showSaveDialog(frame);

				if (option == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					lastDirectory = file.getParentFile();
					if (file.getName().endsWith(".xml") == false) {

						// *** add extension
						try {
							file = new File(file.getCanonicalPath() + ".xml");
						} catch (IOException e) {

							e.printStackTrace();
						}
					}

					marshGraph.createMarshallFile(file, true);

				}
			}
		});

		JMenuItem fileJpg = new JMenuItem("Save Screenshot");
		fileJpg.setMnemonic(KeyEvent.VK_T);
		fileJpg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				JFileChooser chooser = new JFileChooser();
				chooser.addChoosableFileFilter(new ImgFilter());
				chooser.setCurrentDirectory(lastDirectory);

				int option = chooser.showSaveDialog(frame);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					lastDirectory = file.getParentFile();
					DeusUtility.writeJPEGImage(file, vv);
				}
			}
		});

		JMenuItem fileClose = new JMenuItem("Exit");
		fileClose.setMnemonic(KeyEvent.VK_E);

		fileClose.setToolTipText("Exit application");
		fileClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				exitCheck();
			}
		});

		fileMenu.add(openFile);
		fileMenu.add(parzialXml);
		fileMenu.add(fileXml);
		fileMenu.add(fileJpg);
		fileMenu.add(fileClose);
		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);

		// *** Pannello laterale ***

		jpRight = new JPanel();

		MenuPanelRight panelRight = new MenuPanelRight();
		jpRight.add(panelRight.createPanel(gm));

		JPanel layoutPanel = new JPanel();
		layoutPanel.setBorder(BorderFactory.createTitledBorder("Set Layout"));
		JComboBox selectLayoutCombo = new JComboBox(typeLayout);
		layoutPanel.add(selectLayoutCombo);
		panelRight.getJpanelRight().add(layoutPanel);

		// Set Layout
		selectLayoutCombo.addActionListener(new ActionListener() {

			Dimension d = vv.getSize();

			@Override
			public void actionPerformed(ActionEvent e) {

				JComboBox cb = (JComboBox) e.getSource();

				if (cb.getSelectedItem().toString() == "Spring") {

					springLayout.setSize(d);

					Relaxer relaxer = new VisRunner(
							(IterativeContext) springLayout);
					relaxer.stop();
					relaxer.prerelax();

					LayoutTransition<DeusVertex, DeusEdge> lt = new LayoutTransition<DeusVertex, DeusEdge>(
							vv, vv.getGraphLayout(), springLayout);
					Animator animator = new Animator(lt);
					animator.start();

					vv.repaint();

					// vv.setGraphLayout(springLayout);
				} else if (cb.getSelectedItem().toString() == "Static") {

					layout.setSize(d);

					LayoutTransition<DeusVertex, DeusEdge> lt = new LayoutTransition<DeusVertex, DeusEdge>(
							vv, vv.getGraphLayout(), layout);
					Animator animator = new Animator(lt);
					animator.start();

					vv.repaint();

					// vv.setGraphLayout(layout);

				} else if (cb.getSelectedItem().toString() == "FRLayout") {

					frLayout.setSize(d);

					Relaxer relaxer = new VisRunner((IterativeContext) frLayout);
					relaxer.pause();

					LayoutTransition<DeusVertex, DeusEdge> lt = new LayoutTransition<DeusVertex, DeusEdge>(
							vv, vv.getGraphLayout(), frLayout);
					Animator animator = new Animator(lt);
					animator.start();

					relaxer.resume();
					relaxer.prerelax();
					vv.repaint();

					// vv.setGraphLayout(frLayout);
				} else if (cb.getSelectedItem().toString() == "ISOMLayout") {

					isomLayout.setSize(d);

					Relaxer relaxer = new VisRunner(
							(IterativeContext) isomLayout);
					relaxer.pause();

					LayoutTransition<DeusVertex, DeusEdge> lt = new LayoutTransition<DeusVertex, DeusEdge>(
							vv, vv.getGraphLayout(), isomLayout);
					Animator animator = new Animator(lt);
					animator.start();

					relaxer.resume();
					relaxer.prerelax();
					vv.repaint();

					// vv.setGraphLayout(isomLayout);
				} else if (cb.getSelectedItem().toString() == "CircleLayout") {

					circleLayout.setSize(d);

					LayoutTransition<DeusVertex, DeusEdge> lt = new LayoutTransition<DeusVertex, DeusEdge>(
							vv, vv.getGraphLayout(), circleLayout);
					Animator animator = new Animator(lt);
					animator.start();

					vv.repaint();

					// vv.setGraphLayout(circleLayout);
				} else if (cb.getSelectedItem().toString() == "DAGLayout") {

					dagLayout.setSize(d);

					LayoutTransition<DeusVertex, DeusEdge> lt = new LayoutTransition<DeusVertex, DeusEdge>(
							vv, vv.getGraphLayout(), dagLayout);
					Animator animator = new Animator(lt);
					animator.start();

					vv.repaint();

					// vv.setGraphLayout(dagLayout);
				}

			}
		});

		ToolbarDeus toolbar = new ToolbarDeus();
		toolbar.setVisualizationView(vv);

		frame.getContentPane().add(toolbar, BorderLayout.NORTH);

		frame.getContentPane().add(jpRight, BorderLayout.EAST);
	}

	private void exitCheck() {

		int numVert = vv.getGraphLayout().getGraph().getVertexCount();
		if (numVert != 0) {
			int n = JOptionPane.showConfirmDialog(MainGUI.frame,
					"The graph will be deleted. Exit?", "Exit",
					JOptionPane.YES_NO_OPTION);
			if (n == 0)
				System.exit(0);

		} else
			System.exit(0);
	}

	public static void main(String[] args) {

		frame = new JFrame("DEUS Visual Editor");

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// Build the graph
		final MainGUI sgv = new MainGUI();

		frame.pack();
		frame.setVisible(true);
		frame.setResizable(true);
	}

}
