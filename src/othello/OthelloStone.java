package othello;

public enum OthelloStone {
	
	BLACK("¡Ü"), WHITE("¡Û"), EMPTY("."), AVAILABLE("a");
	
	private String mark;
	
	private OthelloStone(String mark) {
		this.mark = mark;
	}
	
	public boolean isOppositeTo(OthelloStone stone) {
		if(!this.isStone())
			return false;
		return this==BLACK ? stone == WHITE : stone == BLACK;
	}
	
	public boolean isStone() {
		return this==BLACK || this==WHITE;
	}
	
	@Override
	public String toString() {
		return mark;
	}
}
