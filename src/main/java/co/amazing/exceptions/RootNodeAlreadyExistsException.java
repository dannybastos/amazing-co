package co.amazing.exceptions;

public class RootNodeAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = -7262483803548338588L;
	public RootNodeAlreadyExistsException() {
		super("Root node already exists!");
	}
}
