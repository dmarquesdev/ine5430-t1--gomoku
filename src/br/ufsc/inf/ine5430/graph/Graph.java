package br.ufsc.inf.ine5430.graph;

import java.util.HashSet;

public interface Graph extends Cloneable {
	public HashSet<Node> getNodes();

	public HashSet<Edge> getEdges();

	public void addNode(Node node);

	public void addEdge(Node node, Edge edge);

	public void addEdge(Node node1, Node node2, int value);

	public boolean isLeafNode(Node node);
}
