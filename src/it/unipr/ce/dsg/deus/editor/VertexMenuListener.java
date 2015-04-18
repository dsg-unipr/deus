package it.unipr.ce.dsg.deus.editor;

import edu.uci.ics.jung.visualization.VisualizationViewer;


/**
 * Used to indicate that this class wishes to be told of a selected vertex
 * along with its visualization component context. 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 */
public interface VertexMenuListener<MyVertex> {
    void setVertexAndView(MyVertex v, VisualizationViewer<MyVertex, DeusEdge> visView);    
}
