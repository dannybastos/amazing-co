package co.amazing.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import co.amazing.MessageUtil;
import co.amazing.dto.NodeDTO;
import co.amazing.enums.NodePosition;
import co.amazing.exceptions.NodeNotFoundException;
import co.amazing.exceptions.RootNodeAlreadyExistsException;
import co.amazing.model.Node;
import co.amazing.repository.NodeRepository;

@Service
public class NodeService {
	
	@Autowired
	private MessageUtil messageUtil;
	private NodeRepository nodeRepository;

	public NodeService(NodeRepository nodeRepository) {
		this.nodeRepository = nodeRepository;
	}

	public Node save(NodeDTO nodeDTO) {
		Assert.notNull(nodeDTO, messageUtil.UNABLE_TO_SAVE_NULL);
		Node result = new Node();
		boolean isRootCandidate = StringUtils.isEmpty(nodeDTO.getParentId());
		if (isRootCandidate) {
			checkIfRootNodeExists();
			result.setRoot(true);
			result = nodeRepository.save(result);
		} else {
			Node parentNode = nodeRepository.findById(nodeDTO.getParentId())
					.orElseThrow(() -> new NodeNotFoundException(nodeDTO.getParentId()));
			
			validateBeforeSave(nodeDTO);

			result = addChildNode(parentNode, nodeDTO);
		}
		return result;
	}

	private void validateBeforeSave(NodeDTO nodeDTO) {
		//validate parent left and right
		Node parent = this.findById(nodeDTO.getParentId());
		
		if (nodeDTO.getNodePosition().equals(NodePosition.LEFT)) {
			Assert.isTrue(StringUtils.isEmpty(parent.getLeftNodeId()),
					messageUtil.PARENT_ALREADY_HAVE_A_LEFT_CHILD_NODE);
		} else {
			Assert.isTrue(StringUtils.isEmpty(parent.getRightNodeId()),
					messageUtil.PARENT_ALREADY_HAVE_A_RIGHT_CHILD_NODE);
		}
	}

	private void checkIfRootNodeExists() {
		if (nodeRepository.findByRoot(true).isPresent())
			throw new RootNodeAlreadyExistsException();
	}

	/**
	 *
	 * @param parent
	 * @param nodeDTO
	 * @return
	 */
	private Node addChildNode(Node parent, NodeDTO nodeDTO) {
		Node childNode = new Node();
		childNode.setHeight(parent.getHeight() + 1);
		childNode.setParentId(parent.getId());
		childNode = nodeRepository.save(childNode);
		if (nodeDTO.getNodePosition().equals(NodePosition.LEFT))
			parent.setLeftNodeId(childNode.getId());
		else
			parent.setRightNodeId(childNode.getId());
		nodeRepository.save(parent);
		return childNode;		
	}

	/**
	 * Find all the child nodes from the id informed.
	 * If the Tree is too big, the return can take a while  
	 * @param id
	 * @return
	 */
	public Set<Node> findChildren(String id) {
		Assert.notNull(id, messageUtil.ID_MUST_NOT_BE_NULL);
		List<Node> lst = new ArrayList<>();
		Set<Node> result = new HashSet<>();
		Node node = findById(id);
		lst.add(node);
		while (!lst.isEmpty()) {
			node = lst.remove(0);
			String nodeId = node.getLeftNodeId();
			if (!StringUtils.isEmpty(nodeId)) {
				lst.add(addChildNode(result, nodeId));				
			}
			nodeId = node.getRightNodeId();
			if (!StringUtils.isEmpty(nodeId)) {
				lst.add(addChildNode(result, nodeId));
			}
				
		}
		return result;
	}

	private Node addChildNode(Set<Node> lst, String nodeId) {
		Node childNode = findById(nodeId);
		lst.add(childNode);
		return childNode;
	}

	public Node findById(String nodeId) {
		Assert.notNull(nodeId, messageUtil.ID_MUST_NOT_BE_NULL);
		return nodeRepository.findById(nodeId)
				.orElseThrow(() -> new NodeNotFoundException(nodeId));
	}

	public Node changeParent(String id, NodeDTO nodeDTO) {
		Assert.notNull(id, messageUtil.ID_MUST_NOT_BE_NULL);
		Assert.notNull(nodeDTO.getParentId(), messageUtil.PARENT_NODE_MUST_NOT_BE_NULL);
		Node node = this.findById(id);
		Node parent = this.findById(nodeDTO.getParentId());
		
		validateChangeUpdate(node, parent);
		
		if (!StringUtils.isEmpty(parent.getLeftNodeId()))
			parent.setLeftNodeId(node.getId());
		else 
			parent.setRightNodeId(node.getId());
		
		node.setParentId(parent.getId());
		
		nodeRepository.save(parent);
		nodeRepository.save(node);
		
		return node;
	}

	/**
	 * 
	 * @param node
	 * @param parent
	 */
	private void validateChangeUpdate(Node node, Node parent) {
		//same node
		Assert.isTrue(!node.equals(parent), messageUtil.NODE_AND_PARENT_ARE_THE_SAME);
		//root
		Optional<Node> root = nodeRepository.findByRoot(true);
		root.ifPresent( r ->
			Assert.isTrue(!node.equals(r), messageUtil.ROOT_NODE_CANT_CHANGE_PARENT)
		);

		//same parent
		Assert.isTrue(!node.getParentId().equals(parent.getId()), messageUtil.NODE_ALREADY_ASSIGNED_TO_THIS_PARENT);
		
		// parent have both nodes
		Assert.isTrue(StringUtils.isEmpty(parent.getLeftNodeId()) || StringUtils.isEmpty(parent.getRightNodeId()),
				messageUtil.PARENT_ALREADY_HAVE_BOTH_CHILDS);
	}
}
