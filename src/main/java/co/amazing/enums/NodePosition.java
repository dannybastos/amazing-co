package co.amazing.enums;

public enum NodePosition {
	LEFT("left"),
	RIGHT("right");
		
	@SuppressWarnings("unused")
	private final String position;
	
	NodePosition(String position) {
		this.position = position;
	}

}
