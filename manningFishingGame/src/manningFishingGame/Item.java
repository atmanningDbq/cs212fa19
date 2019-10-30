package manningFishingGame;

public class Item {

	// private fields
	private String sTitle = "nothing";
	private int pointValue = 0;
	
	// Constructor
	Item( String s, int n) {
		sTitle = s;
		pointValue = n;
	}
	
	// mutators and getters
	public String getTitle() {
		return sTitle;
	}
	
	public int getValue() {
		return pointValue;
	}
			
	
}
