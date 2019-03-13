package co.amazing.dto;

import co.amazing.enums.NodePosition;

public class NodeDTO {

	private String parentId;
	private NodePosition nodePosition;

	public NodeDTO() {
		super();
		this.nodePosition = NodePosition.LEFT;
	}

	public NodeDTO(String parentId, NodePosition nodePosition) {
		super();
		this.parentId = parentId;
		this.setNodePosition(nodePosition);
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public NodePosition getNodePosition() {
		return nodePosition;
	}

	public void setNodePosition(NodePosition nodePosition) {
		this.nodePosition = nodePosition;
	}
}
