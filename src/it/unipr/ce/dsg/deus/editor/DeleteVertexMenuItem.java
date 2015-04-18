package it.unipr.ce.dsg.deus.editor;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/**
 * A class to implement the deletion of a vertex from within a MenuManagePopup.
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 */
public class DeleteVertexMenuItem<MyVertex> extends JMenuItem implements
		VertexMenuListener<MyVertex> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyVertex vertex;
	private VisualizationViewer<MyVertex, DeusEdge> visComp;

	/** Creates a new instance of DeleteVertexMenuItem */
	public DeleteVertexMenuItem() {
		super("Delete Vertex");
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visComp.getPickedVertexState().pick(vertex, false);
				visComp.getGraphLayout().getGraph().removeVertex(vertex);
				visComp.repaint();
			}
		});
	}

	/**
	 * Implements the VertexMenuListener interface.
	 * 
	 * @param v
	 * @param visComp
	 */
	public void setVertexAndView(MyVertex v,
			VisualizationViewer<MyVertex, DeusEdge> visComp) {
		this.vertex = v;
		this.visComp = visComp;
		this.setText("Delete Vertex " + v.toString());
	}

}
