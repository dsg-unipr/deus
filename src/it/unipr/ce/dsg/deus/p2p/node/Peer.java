package it.unipr.ce.dsg.deus.p2p.node;

import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;

/**
 * Extension of the Node class for the p2p package. The Peer is a node with an
 * associated list of nodes (neighbors).
 * 
 * @author Matteo Agosti (agosti@ce.unipr.it)
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class Peer extends Node {

	protected ArrayList<Peer> neighbors = null;
	protected boolean isConnected = false;

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public Peer(String id, Properties params, ArrayList<Resource> resources)
			throws InvalidParamsException {
		super(id, params, resources);
		this.neighbors = new ArrayList<Peer>();
		initialize();
	}

	public void initialize() throws InvalidParamsException {

	}

	public Object clone() {
		Peer clone = (Peer) super.clone();
		clone.neighbors = new ArrayList<Peer>();
		// clone.isReachable = false;
		return clone;
	}

	/**
	 * Adds a neighbor to the node.
	 * 
	 * @param newNeighbor
	 *            the neighbor to add.
	 * @return <code>true</code> if the adding procedure went fine,
	 *         <code>false</code> otherwise.
	 */
	public boolean addNeighbor(Peer newNeighbor) {
		// check if newNeighbor is already in the neighbors list
		boolean isAlreadyNeighbor = false;
		for (Iterator<Peer> it = neighbors.iterator(); it.hasNext();)
			if (((Peer) it.next()).key == newNeighbor.key)
				isAlreadyNeighbor = true;
		if (!isAlreadyNeighbor) {
			neighbors.add(newNeighbor);
			Collections.sort(neighbors); // sort by node id
			return true;
		} else
			return false;
	}

	/**
	 * Remove the given peer from the list of neighbors.
	 * 
	 * @param neighbor
	 *            the peer to remove from the list of neighbors.
	 */
	public void removeNeighbor(Peer neighbor) {
		// We can't use the remove function of the arraylist because it will
		// destroy the object (sets it to null) so we basically need to copy the
		// whole arraylist into a new one avoiding the node to be removed
		ArrayList<Peer> newNeighbors = new ArrayList<Peer>();
		for (Iterator<Peer> it = neighbors.iterator(); it.hasNext();) {
			Peer n = it.next();
			if (!n.equals(neighbor))
				newNeighbors.add(n);
		}
		neighbors = newNeighbors;
	}

	/**
	 * Returns the list of neighbors.
	 * 
	 * @return the list of neighbors.
	 */
	public ArrayList<Peer> getNeighbors() {
		return neighbors;
	}

	/**
	 * Resets the list of neighbors.
	 */
	public void resetNeighbors() {
		neighbors = new ArrayList<Peer>();
	}
	
	public String toString() {
		return Integer.toString(key);
	}
}
