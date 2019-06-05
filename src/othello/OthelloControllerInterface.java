package othello;

public interface OthelloControllerInterface {

	void changeStone();
	
	void changeAI(Stage stage);
	
	void restart();
	
	void undo();
	
	void putStone(int r, int c);
}
