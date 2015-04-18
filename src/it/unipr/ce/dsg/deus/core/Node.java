package it.unipr.ce.dsg.deus.core;

import java.util.ArrayList;
import java.util.Properties;

/**
 * <p>
 * The Node class represents the "entity" inside the simulation. Nodes are
 * simulation object that are being created, deleted and used to store
 * information.
 * </p>
 * <p>
 * Each node is identified by the configuration id, a set of properties and a
 * set of resources.
 * </p>
 * 
 * @author Matteo Agosti 
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public abstract class Node extends SimulationObject implements
		Comparable<Node>, Cloneable {
	protected String id;

	protected int key;
	
	protected ArrayList<Resource> resources = null;

	protected Properties params = null;

	/**
	 * Class constructor that builds the node with its minimal set of
	 * properties. Each implementing class should call the super constructor and
	 * immediately after invoke the initialize method to check that the node
	 * parameters are correct.
	 * 
	 * @param id
	 *            the identifier of the node as specified in the configuration
	 *            file.
	 * @param params
	 *            the properties that will be handled by the node.
	 * @param resources
	 *            the set of resources associated to the node.
	 * 
	 * @throws InvalidParamsException
	 *             if the given parameter are wrong.
	 */
	public Node(String id, Properties params, ArrayList<Resource> resources)
			throws InvalidParamsException {
		this.id = id;
		this.resources = resources;
		this.params = params;
	}

	/**
	 * Provides the initialization of the node according to the given
	 * parameters. This method should also perform a check on the parameters
	 * values.
	 * 
	 * @throws InvalidParamsException
	 *             if the parameters passed to the node are wrong.
	 */
	public abstract void initialize() throws InvalidParamsException;

	/**
	 * Performs the standard Object.equals comparison by using the node key as
	 * the criteria.
	 */
	public boolean equals(Object o) {
		return key == (((Node) o).key);
	}

	/**
	 * Standard implementation of the compareTo method that uses the node key as
	 * sorting criteria.
	 */
	public int compareTo(Node n) {
		int result = 0;
		if (key < n.key)
			result = -1;
		else if (key == n.key)
			result = 0;
		else if (key > n.key)
			result = 1;
		return result;
	}

	/**
	 * Returns the list of resources associated to the node.
	 * 
	 * @return the list of resources associated to the node.
	 */
	public ArrayList<Resource> getResources() {
		return resources;
	}

	/**
	 * Returns the node id.
	 * 
	 * @return the node id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Create an instance of the current node by cloning it and updating the key
	 * with the one provided.
	 * 
	 * @param key
	 *            the key of the newly created node instance.
	 * @return the newly created node instance.
	 */
	public Node createInstance(int key) {
		Node clone = (Node) clone();
		clone.key = key;
		return clone;
	}

	/**
	 * Clone the node.
	 */
	public Object clone() {
		try {
			Node clone = (Node) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
}
