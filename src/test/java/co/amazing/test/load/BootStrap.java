package co.amazing.test.load;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import co.amazing.dto.NodeDTO;
import co.amazing.enums.NodePosition;
import co.amazing.model.Node;
import co.amazing.service.NodeService;

@Profile("load-test")
@Component
public class BootStrap implements ApplicationListener<ContextRefreshedEvent> {
	
	private NodeService nodeService;
	public BootStrap(NodeService nodeService) {
		this.nodeService = nodeService;
	}
	
	/**
	 * Used only for test purposes, to load a lot of nodes
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		NodeDTO nodeDTO = new NodeDTO();
		Node parentNode = nodeService.save(nodeDTO);
		
		for (int i = 0; i < 1000000; i++) {
			NodeDTO child = new NodeDTO();
			child.setParentId(parentNode.getId());
			Node childNode;
			if ((i % 2) == 0) {
				child.setNodePosition(NodePosition.LEFT);				
				childNode = nodeService.save(child);
				parentNode = nodeService.findById(childNode.getId());
			}
			else {
				child.setNodePosition(NodePosition.RIGHT);
				childNode = nodeService.save(child);
				parentNode = nodeService.findById(childNode.getId());
			}
		}
	}
}
