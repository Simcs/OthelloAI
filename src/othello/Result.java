package othello;

public enum Result {
	WIN("Èæ ½Â"), LOSE("¹é ½Â"), DRAW("¹«½ÂºÎ");
	
	private String description;
	private Result(String Deescription) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
}
