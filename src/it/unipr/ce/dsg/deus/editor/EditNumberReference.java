package it.unipr.ce.dsg.deus.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import edu.uci.ics.jung.visualization.VisualizationViewer;


/**
 * Class that creates the form to insert the number reference
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 */
public class EditNumberReference extends JMenuItem implements
		EdgeMenuListener<DeusEdge> {

	private static final long serialVersionUID = 1L;
	private DeusEdge edge;
	private VisualizationViewer<DeusVertex, DeusEdge> visComp;

	public EditNumberReference() {
		super("Edit Reference Number");

		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String s = (String) JOptionPane.showInputDialog(MainGUI.frame,
						"Insert the number reference", "Reference number",
						JOptionPane.OK_CANCEL_OPTION, null, null,
						edge.getNumber());

				if (s != null) {
					edge.setNumber(Integer.valueOf(s));
				}

			}
		});

	}

	public void setEdgeAndView(DeusEdge e,
			VisualizationViewer<DeusVertex, DeusEdge> visView) {
		this.edge = e;
		this.visComp = visView;
		this.setText("Edit Reference Number");
	}

}
