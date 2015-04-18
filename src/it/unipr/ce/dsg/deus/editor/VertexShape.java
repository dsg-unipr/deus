package it.unipr.ce.dsg.deus.editor;

import java.awt.Shape;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;


/**
 * A class that draws a figure in relation to the type of the vertex
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 * 
 */
public class VertexShape extends AbstractVertexShapeTransformer<DeusVertex>
		implements Transformer<DeusVertex, Shape> {

	VertexShape() {

		// augment the size of the vertex with respect to default one 
		setSizeTransformer(new Transformer<DeusVertex, Integer>() {

			public Integer transform(DeusVertex v) {
				return 30;
			}
		});

	}

	// transform the figure in a square
	public Shape transform(DeusVertex v) {
		if (v.getElementType() == "Engine") {
			return factory.getRegularPolygon(v, 3);
		} else if (v.getElementType() == "Process") {
			return factory.getRectangle(v);
		} else if (v.getElementType() == "Event") {
			return factory.getEllipse(v);
		} else if (v.getElementType() == "Network") {
			return factory.getRegularStar(v, 7);
		}
		else
			return factory.getRegularPolygon(v, 8);
	}

}