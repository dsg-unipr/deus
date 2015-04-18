package it.unipr.ce.dsg.deus.editor;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.layout.ObservableCachingLayout;

/**
 * Class with any utility
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 */

public class DeusUtility {

	public static DeusVertex getVertexById(String id,
			Collection<DeusVertex> vert) {
		for (Iterator<DeusVertex> it = vert.iterator(); it.hasNext();) {
			DeusVertex p = it.next();
			if (p.getId().equals(id))
				return p;
		}
		return null;
	}

	public static DeusVertex getVertexById(String id, ArrayList<DeusVertex> vert) {
		for (Iterator<DeusVertex> it = vert.iterator(); it.hasNext();) {
			DeusVertex p = it.next();
			if (p.getId().equals(id))
				return p;
		}
		return null;
	}

	public static void writeJPEGImage(File file,
			VisualizationViewer<DeusVertex, DeusEdge> visView) {
		int width = visView.getWidth();
		int height = visView.getHeight();

		if ((file.getName().endsWith(".jpeg") || file.getName()
				.endsWith(".jpg")) == false) {
			try {
				file = new File(file.getCanonicalPath() + ".jpg");
			} catch (IOException e1) {
				// e1.printStackTrace();
			}
		}

		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bi.createGraphics();
		visView.paint(graphics);
		graphics.dispose();

		try {
			ImageIO.write(bi, "jpeg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void openFile(
			VisualizationViewer<DeusVertex, DeusEdge> visView) {

		int numVert = visView.getGraphLayout().getGraph().getVertexCount();
		if (numVert != 0) {
			int n = JOptionPane.showConfirmDialog(MainGUI.frame,
					"The graph will be deleted. Open file?", "Open file",
					JOptionPane.YES_NO_OPTION);

			if (n == 0) {
				Graph<DeusVertex, DeusEdge> graph = visView.getGraphLayout()
						.getGraph();
				DeusVertex vert;
				for (int i = 0; i < numVert; i++) {
					vert = graph.getVertices().iterator().next();
					graph.removeVertex(vert);
				}
				visView.repaint();
				doUnmarshalling(visView);
			}
		} else {
			doUnmarshalling(visView);

		}
	}

	private static void doUnmarshalling(
			VisualizationViewer<DeusVertex, DeusEdge> visView) {

		Unmarshalling unmarshalling = new Unmarshalling();
		unmarshalling.setVisualizationView(visView);
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(MainGUI.lastDirectory);
		chooser.addChoosableFileFilter(new XmlFilter("xml, bup"));
		int option = chooser.showOpenDialog(MainGUI.frame);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			MainGUI.lastDirectory = file.getParentFile();
			if (file.length() != 0) {
				try {
					unmarshalling.createGraphAutomator(file);

				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (JAXBException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					e1.printStackTrace();
				} catch (SAXException e1) {
					e1.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(MainGUI.frame,
						"The file selected is empty.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public static void saveVertexPosition(
			VisualizationViewer<DeusVertex, DeusEdge> vv, String pathname) {

		String newFileName;
		Map<DeusVertex, Point2D> map;

		final ObservableCachingLayout<DeusVertex, DeusEdge> OCLayout = new ObservableCachingLayout<DeusVertex, DeusEdge>(
				vv.getGraphLayout());

		map = new HashMap<DeusVertex, Point2D>();

		for (DeusVertex v : OCLayout.getGraph().getVertices()) {

			map.put(v, OCLayout.transform(v));

		}

		newFileName = pathname.replaceAll(".xml", ".pos");
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(newFileName));

			oos.writeObject(map);
			oos.close();

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static void restoreVertexPosition(
			VisualizationViewer<DeusVertex, DeusEdge> vv, String pathname) {
		Map<DeusVertex, Point2D> map = new HashMap<DeusVertex, Point2D>();

		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(pathname));
			try {
				map = (Map) ois.readObject();
				ois.close();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Set<Entry<DeusVertex, Point2D>> DeusVertexSet = map.entrySet();
		Iterator<Entry<DeusVertex, Point2D>> ita = DeusVertexSet.iterator();
		Entry<DeusVertex, Point2D> VertexEntry;
		Point2D ss = new Point2D.Double();

		while (ita.hasNext()) {
			VertexEntry = ita.next();
			ss.setLocation(VertexEntry.getValue().getX(), VertexEntry
					.getValue().getY());
			vv.getGraphLayout().setLocation(
					getVertexById(VertexEntry.getKey().getId(), vv
							.getGraphLayout().getGraph().getVertices()), ss);
			vv.repaint();

		}
	}

}
