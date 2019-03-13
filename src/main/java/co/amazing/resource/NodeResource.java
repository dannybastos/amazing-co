package co.amazing.resource;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.amazing.dto.NodeDTO;
import co.amazing.exceptions.NodeNotFoundException;
import co.amazing.exceptions.RootNodeAlreadyExistsException;
import co.amazing.model.Node;
import co.amazing.service.NodeService;

@RestController
@RequestMapping("/nodes")
public class NodeResource {

	private NodeService nodeService;

	public NodeResource(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	@PostMapping
	public ResponseEntity<Node> createNode(@RequestBody NodeDTO nodeDTO) {
		Node node = nodeService.save(nodeDTO);
		return ResponseEntity.created(null).body(node);
	}

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Node> findNode(@PathVariable("id") String id) {
		Node node = nodeService.findById(id);
		return ResponseEntity.ok(node);
	}

	@GetMapping(path = "/{id}/children", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Node>> findChildren(@PathVariable("id") String id) {
		Set<Node> lst = nodeService.findChildren(id);
		return ResponseEntity.ok(lst);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<String> updateParent(@PathVariable String id, @RequestBody NodeDTO nodeDTO) {
		nodeService.changeParent(id, nodeDTO);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler({ IllegalArgumentException.class })
	public ResponseEntity<String> handleException(Exception ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler({ RootNodeAlreadyExistsException.class })
	public ResponseEntity<String> handleRootNodeAlreadyExistsException(Exception ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler({ NodeNotFoundException.class })
	public ResponseEntity<String> handleNodeNotFoundException(Exception ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

}
