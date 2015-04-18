package it.unipr.ce.dsg.deus.editor;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;


/**
 * A class to implement the deletion of an edge from within a MenuManagePopup.
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 */
public class DeleteEdgeMenuItem<MyEdge> extends JMenuItem implements
		EdgeMenuListener<MyEdge> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyEdge edge;
	private VisualizationViewer<DeusVertex, MyEdge> visComp;

	/** Creates a new instance of DeleteEdgeMenuItem */
	public DeleteEdgeMenuItem() {
		super("Delete Edge");
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visComp.getPickedEdgeState().pick(edge, false);
				visComp.getGraphLayout().getGraph().removeEdge(edge);
				visComp.repaint();
			}
		});
	}

	/**
	 * Implements the EdgeMenuListener interface to update the menu item with
	 * info on the currently chosen edge.
	 * 
	 * @param edge
	 * @param visComp
	 */
	public void setEdgeAndView(MyEdge edge,
			VisualizationViewer<DeusVertex, MyEdge> visComp) {
		this.edge = edge;
		this.visComp = visComp;
		this.setText("Delete Edge");
	}

}
