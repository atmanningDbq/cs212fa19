package manningFishingGame;

public class Item {

	private String title = "Unnamed";
	private int pointValue = 0;
	
	// Constructor - assign name an point value to new instance of this class
	Item(String s, int n){
		title = s;
		pointValue = n;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getValue() {
		return pointValue;
	}
}
