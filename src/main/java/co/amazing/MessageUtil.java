package co.amazing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {
	
	// validation messages
	@Value("${message.validation.root_already_have_both_childs}") 
	public String PARENT_ALREADY_HAVE_BOTH_CHILDS;
	@Value("${message.validation.root_already_assigned_to_this_parent}")
	public String NODE_ALREADY_ASSIGNED_TO_THIS_PARENT;
	@Value("${message.validation.root_node_cant_change_parent}")
	public String ROOT_NODE_CANT_CHANGE_PARENT;
	@Value("${message.validation.node_and_parent_are_the_same}")
	public String NODE_AND_PARENT_ARE_THE_SAME;
	@Value("${message.validation.parent_already_have_a_right_child_node}")
	public String PARENT_ALREADY_HAVE_A_RIGHT_CHILD_NODE = null;
	@Value("${message.validation.parent_already_have_a_left_child_node}")
	public String PARENT_ALREADY_HAVE_A_LEFT_CHILD_NODE;

	
	// error messages
	@Value("${message.error.parent_must_be_not_null}")
	public String PARENT_NODE_MUST_NOT_BE_NULL;
	@Value("${message.error.id_must_be_not_null}")
	public String ID_MUST_NOT_BE_NULL;
	@Value("${message.error.child_without_position}")
	public String CHILD_NODE_WITHOUT_POSITION;
	@Value("${message.error.unable_to_save_null}")
	public String UNABLE_TO_SAVE_NULL;
	
}
