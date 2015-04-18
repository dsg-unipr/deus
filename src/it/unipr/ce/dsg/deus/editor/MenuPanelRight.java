package it.unipr.ce.dsg.deus.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;


/**
 * Panel right of GUI
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 * 
 */
public class MenuPanelRight implements ActionListener {

	private String target;
	private JPanel jpanel;
	private EditingModalGraphMouse<DeusVertex, DeusEdge> modalGM;

	public JPanel createPanel(EditingModalGraphMouse<DeusVertex, DeusEdge> gm) {

		modalGM = gm;

		jpanel = new JPanel();

		jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.PAGE_AXIS));
		jpanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel control_panel = new JPanel();
		control_panel.setBorder(new TitledBorder("Vertices"));

		JPanel vertex_panel = new JPanel();
		vertex_panel.setLayout(new BoxLayout(vertex_panel, BoxLayout.Y_AXIS));
		vertex_panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 30));

		JRadioButton engine = new JRadioButton("Engine");
		JRadioButton process = new JRadioButton("Process");
		JRadioButton event = new JRadioButton("Event");
		JRadioButton node = new JRadioButton("Node");
		JRadioButton network = new JRadioButton("Network");

		event.setSelected(true);

		engine.addActionListener(this);
		process.addActionListener(this);
		event.addActionListener(this);
		node.addActionListener(this);
		network.addActionListener(this);

		ButtonGroup groupVert = new ButtonGroup();
		groupVert.add(engine);
		groupVert.add(process);
		groupVert.add(event);
		groupVert.add(node);
		groupVert.add(network);

		vertex_panel.add(engine);
		vertex_panel.add(process);
		vertex_panel.add(event);
		vertex_panel.add(node);
		vertex_panel.add(network);

		control_panel.add(vertex_panel);

		JPanel mouseModePanel = new JPanel();
		mouseModePanel
				.setBorder(BorderFactory.createTitledBorder("Mouse Mode"));

		JPanel modePanel = new JPanel();
		modePanel.setLayout(new BoxLayout(modePanel, BoxLayout.Y_AXIS));
		JRadioButton editing = new JRadioButton("Editing");
		JRadioButton transforming = new JRadioButton("Transforming");
		JRadioButton picking = new JRadioButton("Picking");

		editing.setSelected(true);

		editing.addActionListener(this);
		transforming.addActionListener(this);
		picking.addActionListener(this);

		ButtonGroup groupMode = new ButtonGroup();

		groupMode.add(editing);
		groupMode.add(transforming);
		groupMode.add(picking);

		modePanel.add(editing);
		modePanel.add(transforming);
		modePanel.add(picking);
		mouseModePanel.add(modePanel);

		jpanel.add(control_panel);
		jpanel.add(mouseModePanel);

		return jpanel;
	}

	public JPanel getJpanelRight() {
		return this.jpanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Editing") {
			modalGM.setMode(ModalGraphMouse.Mode.EDITING);
		} else if (e.getActionCommand() == "Picking") {
			modalGM.setMode(ModalGraphMouse.Mode.PICKING);
		} else if (e.getActionCommand() == "Transforming") {
			modalGM.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		} else {
			target = e.getActionCommand();
			MainGUI.VERTEX_TYPE = target;
		}
	}

}
