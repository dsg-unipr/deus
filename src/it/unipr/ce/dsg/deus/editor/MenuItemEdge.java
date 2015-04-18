package it.unipr.ce.dsg.deus.editor;

import javax.swing.JPopupMenu;


/**
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 * 
 */
public class MenuItemEdge extends JPopupMenu{

	private static final long serialVersionUID = 1L;

	public MenuItemEdge(){	
		super("Edge Menu");
		this.add(new DeleteEdgeMenuItem<DeusEdge>());
		this.add(new EditNumberReference());
	}

}
