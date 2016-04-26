package br.ufsc.inf.ine5430.graph;

import java.util.HashSet;

public class Node {
	private int id;
	private String label;
	private HashSet<Edge> edges;
	private Node parent;

	public Node(int id, String label, Node parent, HashSet<Edge> edges) {
		super();
		this.id = id;
		this.label = label;
		this.parent = parent;
		if (edges != null && !edges.isEmpty())
			this.edges = edges;
		else
			this.edges = new HashSet<>();
	}

	public Node(int id, String label) {
		super();
		this.id = id;
		this.label = label;
		this.edges = new HashSet<>();
	}

	public Node(int id, String label, Node parent) {
		super();
		this.id = id;
		this.label = label;
		this.parent = parent;
		this.edges = new HashSet<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public HashSet<Edge> getEdges() {
		return edges;
	}

	public void setEdges(HashSet<Edge> edges) {
		this.edges = edges;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public void addEdge(Edge edge) {
		edges.add(edge);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (edges == null) {
			if (other.edges != null)
				return false;
		} else if (!edges.equals(other.edges))
			return false;
		if (id != other.id)
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}

}
