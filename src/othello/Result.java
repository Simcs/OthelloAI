package othello;

public enum Result {
	WIN("�� ��"), LOSE("�� ��"), DRAW("���º�");
	
	private String description;
	private Result(String Deescription) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
}
