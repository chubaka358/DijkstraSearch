import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DijkstraSearch {
	private Map<Node, List<Node>> graph;
	private Map<String, Integer> costs;
	private Map<String, String> parents;

	public void addNode(String name, String parent, int cost){
		Node node = new Node(name, parent, cost);
	}

	public DijkstraSearch() {
		graph = new HashMap<Node, List<Node>>();
	}

	private class Node{
		private String name;
		private Map<Node, Integer> children;

		public Node(String name, Map<Node, Integer> children) {
			this.name = name;
			this.children = children;
		}
	}
}
