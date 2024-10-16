public class NodeForDouble {
    private Object data;
    private String name;


	private NodeForDouble prev;
    private NodeForDouble next;

    public NodeForDouble(Object data, String name) {
        this.data = data;
        this.name = name;
        prev=null;
        next=null;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public NodeForDouble getPrev() {
        return prev;
    }

    public void setPrev(NodeForDouble prev) {
        this.prev = prev;
    }

    public NodeForDouble getNext() {
        return next;
    }

    public void setNext(NodeForDouble next) {
        this.next = next;
    }
}
