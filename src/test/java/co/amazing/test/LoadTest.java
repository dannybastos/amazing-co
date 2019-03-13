package co.amazing.test;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

import co.amazing.model.Node;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@TestPropertySource(locations={"classpath:application-load-test.properties"})
public class LoadTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@Test
	public void find_children_success() throws Exception {
//		Node rootNode = new Node();
//		rootNode.setId("5c881bc2bc145c19226fba5a");
//
//		String url = String.format("/nodes/%s/children", rootNode.getId());
//		MvcResult result = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();
//		String jsonResult = result.getResponse().getContentAsString();
//
//		@SuppressWarnings("unchecked")
//		List<Node> lst = JsonPath.parse(jsonResult).read("$", List.class);
//		assertTrue(lst.size() == 1000000);
	}


}
