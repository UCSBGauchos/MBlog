package MBlogWithPaxos;

public class Message {
	String value;
	String operation;
	public Message (String _operation, String _value){
		this.value = _value;
		this.operation = _operation;
	}
}
