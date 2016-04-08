package br.ufsc.inf.ine5430.graph;

public class Edge {
	private int value;
	private Node node;
	
	public Edge(int value, Node node) {
		super();
		this.value = value;
		this.node = node;
	}
	
	public Node getNode() {
		return node;
	}
	public void setNode(Node node) {
		this.node = node;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		result = prime * result + value;
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
		Edge other = (Edge) obj;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		if (value != other.value)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "-" + value + "- " + node;
	}
	
}
