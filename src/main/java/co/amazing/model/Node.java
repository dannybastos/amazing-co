package co.amazing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class Node {

	@Id
	private String id;
	private String parentId;
	private String leftNodeId;
	private String rightNodeId;
	private Integer height;
	private boolean root;

	public Node() {
		super();
		this.height = 0;
		this.root = false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getLeftNodeId() {
		return leftNodeId;
	}

	public void setLeftNodeId(String leftNodeId) {
		this.leftNodeId = leftNodeId;
	}

	public String getRightNodeId() {
		return rightNodeId;
	}

	public void setRightNodeId(String rightNodeId) {
		this.rightNodeId = rightNodeId;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Node node = (Node) o;
		return id.equals(node.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
