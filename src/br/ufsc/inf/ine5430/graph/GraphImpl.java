package br.ufsc.inf.ine5430.graph;

import java.util.HashSet;

public class GraphImpl implements Graph {
	
	private HashSet<Node> nodes;
	private HashSet<Edge> edges;
	
	public GraphImpl() {
		super();
		nodes = new HashSet<>();
		edges = new HashSet<>();
	}

	@Override
	public HashSet<Node> getNodes() {
		return nodes;
	}

	@Override
	public HashSet<Edge> getEdges() {
		return edges;
	}

	@Override
	public void addNode(Node node) {
		nodes.add(node);
	}

	@Override
	public void addEdge(Node node, Edge edge) {
		node.addEdge(edge);
		edges.add(edge);
	}

	@Override
	public void addEdge(Node node1, Node node2, int value) {
		Edge edge = new Edge(value, node2);
		node1.addEdge(edge);
		edges.add(edge);
	}
	
	@Override
	public boolean isLeafNode(Node node) {
		return node.getEdges().isEmpty();
	}

}
