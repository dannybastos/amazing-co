package co.amazing.test;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import co.amazing.dto.NodeDTO;
import co.amazing.enums.NodePosition;
import co.amazing.model.Node;

/**
 * @author dannybastos
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations={"classpath:application-test.properties"})
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
public class AmazingCoApplicationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
    private ObjectMapper mapper;
	
	@Test
	public void create_root_success() throws Exception {
		Node rootNode = createNode(new NodeDTO());
		assertTrue(rootNode.isRoot());
	}

	@Test
	public void create_second_root_failed() throws Exception {
		createNode(new NodeDTO());
		
		NodeDTO secondRoot = new NodeDTO();
		secondRoot.getNodePosition();
		System.out.println(secondRoot.getNodePosition());
		String strSecondRoot = mapper.writeValueAsString(secondRoot);
		mockMvc.perform(post("/nodes")
				.content(strSecondRoot)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isConflict());
	}
	
	@Test
	public void create_invalid_node_parent_failed() throws Exception {
		NodeDTO node = new NodeDTO();
		node.setParentId("1010");
		String strNode = mapper.writeValueAsString(node);
		mockMvc.perform(post("/nodes")
				.content(strNode)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void create_left_node_with_a_parent_with_left_child_failed() throws Exception {
		Node root = createNode(new NodeDTO());
		NodeDTO leftNode = new NodeDTO();
		leftNode.setNodePosition(NodePosition.LEFT);
		leftNode.setParentId(root.getId());
		createNode(leftNode);

		NodeDTO leftNode2 = new NodeDTO();
		leftNode2.setNodePosition(NodePosition.LEFT);
		leftNode2.setParentId(root.getId());

		String strNode = mapper.writeValueAsString(leftNode2);
		mockMvc.perform(post("/nodes")
				.content(strNode)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void find_node_success() throws Exception {
		Node node = createNode(new NodeDTO());

		String url = String.format("/nodes/%s", node.getId());
		mockMvc.perform(get(url)).andDo(print()).andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void find_node_fail() throws Exception {
		String url = String.format("/nodes/%s", "1010");
		mockMvc.perform(get(url)).andDo(print()).andExpect(status().isNotFound());
	}
	
	@Test
	public void find_children_success() throws Exception {
		Node rootNode = createNode(new NodeDTO());
		Node rightNode = createNode(new NodeDTO(rootNode.getId(), NodePosition.RIGHT));
		createNode(new NodeDTO(rightNode.getId(), NodePosition.LEFT));
		createNode(new NodeDTO(rightNode.getId(), NodePosition.RIGHT));
		
		String url = String.format("/nodes/%s/children", rightNode.getId());
		MvcResult result = mockMvc.perform(get(url)).andDo(print()).andExpect(status().isOk()).andReturn();
		String jsonResult = result.getResponse().getContentAsString();
		
		@SuppressWarnings("unchecked")
		List<Node> lst = JsonPath.parse(jsonResult).read("$", List.class);
		assertTrue(lst.size() == 2);
	}
	
	@Test
	public void find_children_with_no_children_success() throws Exception {
		Node rootNode = createNode(new NodeDTO());
		String url = String.format("/nodes/%s/children", rootNode.getId());
		MvcResult result = mockMvc.perform(get(url)).andDo(print()).andExpect(status().isOk()).andReturn();
		String jsonResult = result.getResponse().getContentAsString();
		
		@SuppressWarnings("unchecked")
		List<Node> lst = JsonPath.parse(jsonResult).read("$", List.class);
		assertTrue(lst.isEmpty());
	}
	
	@Test
	public void update_node_to_parent_left_success() throws Exception {
		Node rootNode = createNode(new NodeDTO());
		Node leftNode1 = createNode(new NodeDTO(rootNode.getId(), NodePosition.LEFT));
		Node rightNode1 = createNode(new NodeDTO(rootNode.getId(), NodePosition.RIGHT));
		createNode(new NodeDTO(leftNode1.getId(), NodePosition.LEFT));
		Node rightNode2 = createNode(new NodeDTO(rightNode1.getId(), NodePosition.RIGHT));
		
		performChangeParent(rightNode2, leftNode1);		
	}
	@Test
	public void update_node_to_parent_right_success() throws Exception {
		Node rootNode = createNode(new NodeDTO());
		Node leftNode1 = createNode(new NodeDTO(rootNode.getId(), NodePosition.LEFT));
		Node rightNode1 = createNode(new NodeDTO(rootNode.getId(), NodePosition.RIGHT));
		Node leftNode2 = createNode(new NodeDTO(leftNode1.getId(), NodePosition.LEFT));
		createNode(new NodeDTO(rightNode1.getId(), NodePosition.RIGHT));
		
		performChangeParent(rightNode1, leftNode2);		
	}
	
	@Test
	public void update_parent_fail() throws Exception {
		Node rootNode = createNode(new NodeDTO());
		Node leftNode1 = createNode(new NodeDTO(rootNode.getId(), NodePosition.LEFT));
		Node rightNode1 = createNode(new NodeDTO(rootNode.getId(), NodePosition.RIGHT));
		createNode(new NodeDTO(leftNode1.getId(), NodePosition.LEFT));
		createNode(new NodeDTO(rightNode1.getId(), NodePosition.RIGHT));
		
		String nodeId = rightNode1.getId();
		String parentId = rootNode.getId();
		NodeDTO parentNode = new NodeDTO();
		parentNode.setParentId(parentId);
		String stringNode = mapper.writeValueAsString(parentNode);
		String url = String.format("/nodes/%s", nodeId);
		
		mockMvc.perform(patch(url)
				.content(stringNode)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	private void performChangeParent(Node node, Node parent) throws JsonProcessingException, Exception {
		String nodeId = node.getId();
		String parentId = parent.getId();
		NodeDTO parentNode = new NodeDTO();
		parentNode.setParentId(parentId);
		String stringNode = mapper.writeValueAsString(parentNode);
		String url = String.format("/nodes/%s", nodeId);
		
		mockMvc.perform(patch(url)
				.content(stringNode)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNoContent());
	}

	@Test
	public void update_parent_same_parent_fail() throws Exception {
		Node rootNode = createNode(new NodeDTO());
		Node leftNode1 = createNode(new NodeDTO(rootNode.getId(), NodePosition.LEFT));
		Node rightNode1 = createNode(new NodeDTO(rootNode.getId(), NodePosition.RIGHT));
		createNode(new NodeDTO(leftNode1.getId(), NodePosition.LEFT));
		createNode(new NodeDTO(rightNode1.getId(), NodePosition.RIGHT));
		
		String nodeId = rightNode1.getId();
		String parentId = rootNode.getId();
		NodeDTO parentNode = new NodeDTO();
		parentNode.setParentId(parentId);
		String stringNode = mapper.writeValueAsString(parentNode);
		String url = String.format("/nodes/%s", nodeId);
		
		mockMvc.perform(patch(url)
				.content(stringNode)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void update_parent_with_both_childs_fail() throws Exception {
		Node rootNode = createNode(new NodeDTO());
		Node leftNode1 = createNode(new NodeDTO(rootNode.getId(), NodePosition.LEFT));
		createNode(new NodeDTO(rootNode.getId(), NodePosition.RIGHT));
		Node leftNode2 = createNode(new NodeDTO(leftNode1.getId(), NodePosition.LEFT));
		
		String nodeId = leftNode2.getId();
		String parentId = rootNode.getId();
		NodeDTO parentNode = new NodeDTO();
		parentNode.setParentId(parentId);
		String stringNode = mapper.writeValueAsString(parentNode);
		String url = String.format("/nodes/%s", nodeId);
		
		mockMvc.perform(patch(url)
				.content(stringNode)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}
	
	private Node createNode(NodeDTO nodeDTO) throws Exception {
		MvcResult result = performPostCall(nodeDTO);
		String jsonResult = result.getResponse().getContentAsString();
		Node node = JsonPath.parse(jsonResult).read("$", Node.class);
		return node;
	}
	
	private MvcResult performPostCall(NodeDTO node) throws Exception  {
		String stringNode = mapper.writeValueAsString(node);
		MvcResult result = mockMvc.perform(post("/nodes")
							.content(stringNode)
							.contentType(MediaType.APPLICATION_JSON))
							.andDo(print())
							.andExpect(status().isCreated())
							.andReturn();
		return result;
	}
}
