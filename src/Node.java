
public class Node {
	
	String className;
	double prob;
	Node left;
	Node right;
	
	public Node(String className, double prob, Node left, Node right) {
		
		this.className = className;
		this.prob = prob;
		this.left = left;
		this.right = right;
		
		
	}

}
