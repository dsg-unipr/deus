package it.unipr.ce.dsg.deus.editor;

import edu.uci.ics.jung.visualization.VisualizationViewer;


/**
 * An interface for menu items that are interested in knowning the currently selected edge and
 * its visualization component context.  Used with MenuManagePopup.
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 */
public interface EdgeMenuListener<MyEdge> {
    /**
     * Used to set the edge and visulization component.
     * @param e 
     * @param visComp 
     */
     void setEdgeAndView(MyEdge e, VisualizationViewer<DeusVertex, MyEdge> visView);   
}
