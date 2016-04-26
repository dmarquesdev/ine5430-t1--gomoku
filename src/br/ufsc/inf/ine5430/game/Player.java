package br.ufsc.inf.ine5430.game;

public class Player {
	private String name;
	private int pieceType;

	public Player(String name, int pieceType) {
		super();
		this.name = name;
		this.pieceType = pieceType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPieceType() {
		return pieceType;
	}

	public void setPieceType(int pieceType) {
		this.pieceType = pieceType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + pieceType;
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
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pieceType != other.pieceType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
}
