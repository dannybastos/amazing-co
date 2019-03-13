package co.amazing.exceptions;

public class NodeNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 8569822099430955964L;
	public NodeNotFoundException(String nodeId) {
		super(String.format(String.format("Node not found id [%s]", nodeId)));
	}
}
