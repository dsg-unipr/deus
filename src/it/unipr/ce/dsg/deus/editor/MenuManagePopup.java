package it.unipr.ce.dsg.deus.editor;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JPopupMenu;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;


/**
 * This class manage the menu of all vertices and all edge on Graph
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 * 
 */
public class MenuManagePopup<V,E> extends AbstractPopupGraphMousePlugin{
	private JPopupMenu vertexPopup, edgePopup;
	
	public MenuManagePopup(){
		 this(MouseEvent.BUTTON3_MASK);
	}

	public MenuManagePopup(int button3Mask) {
		super(button3Mask);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handlePopup(MouseEvent e) {
		
    final VisualizationViewer<V,E> vv =
        (VisualizationViewer<V,E>)e.getSource();
		Point2D p = e.getPoint();
		
		GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
		if(pickSupport != null) {
		    final V v = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
		    if(v != null) {
		        updateVertexMenu(v, vv, p);
		        vertexPopup.show(vv, e.getX(), e.getY());
		    } else {
		        final E edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
		        if(edge != null) {
		            updateEdgeMenu(edge, vv, p);
		            edgePopup.show(vv, e.getX(), e.getY());
		        }
		    }
		}
    
		
	}

    private void updateVertexMenu(V v, VisualizationViewer vv, Point2D point) {
        if (vertexPopup == null) return;
        Component[] menuComps = vertexPopup.getComponents();
        for (Component comp: menuComps) {
            if (comp instanceof VertexMenuListener) {
                ((VertexMenuListener)comp).setVertexAndView(v, vv);
            }
            if (comp instanceof MenuPointListener) {
                ((MenuPointListener)comp).setPoint(point);
            }
        }
        
    }
    
    private void updateEdgeMenu(E edge, VisualizationViewer vv, Point2D point) {
        if (edgePopup == null) return;
        Component[] menuComps = edgePopup.getComponents();
        for (Component comp: menuComps) {
            if (comp instanceof EdgeMenuListener) {
                ((EdgeMenuListener)comp).setEdgeAndView(edge, vv);
            }
            if (comp instanceof MenuPointListener) {
                ((MenuPointListener)comp).setPoint(point);
            }
        }
    }
    
    public void setVertexPopup(JPopupMenu vertexPopup) {
        this.vertexPopup = vertexPopup;
    }
    
    public void setEdgePopup(JPopupMenu edgePopup) {
        this.edgePopup = edgePopup;
    }
    
}
