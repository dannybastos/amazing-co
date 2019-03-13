package co.amazing.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import co.amazing.model.Node;

public interface NodeRepository extends CrudRepository<Node, String> {

	Optional<Node> findByRoot(boolean isRoot);

}
