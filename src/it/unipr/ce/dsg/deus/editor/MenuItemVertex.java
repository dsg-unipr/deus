package it.unipr.ce.dsg.deus.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * A class that add a menu to all vertices and run the window dialog to set
 * attribute
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 * 
 */
public class MenuItemVertex extends JPopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2385714465466492719L;
	private JFrame setFrame;

	public MenuItemVertex(JFrame frame) {
		super("Vertex Menu");
		this.setFrame = frame;
		this.add(new DeleteVertexMenuItem<DeusVertex>());
		this.add(new goPopup(setFrame));
	}

	public class goPopup extends JMenuItem implements
			VertexMenuListener<DeusVertex> {

		private static final long serialVersionUID = 4359017805560449341L;
		DeusVertex vertex;
		VisualizationViewer<DeusVertex, DeusEdge> visComp;
		Point2D point;

		@Override
		public void setVertexAndView(DeusVertex vertex,
				VisualizationViewer<DeusVertex, DeusEdge> visComp) {
			this.vertex = vertex;
			this.visComp = visComp;
		}

		public void setPoint(Point2D point) {
			this.point = point;
		}

		public goPopup(final JFrame frame) {
			super("Edit Vertex Properties...");
			this.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					VertexPropertiesDialog createDialog = new VertexPropertiesDialog(
							frame, vertex);
					createDialog.setLocationRelativeTo(frame);
					createDialog.setVisible(true);
					// update vertex label
					visComp.repaint();

				}

			});
		}

	}

}
