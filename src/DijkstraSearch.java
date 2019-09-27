import java.util.*;

public class DijkstraSearch {
	private Map<String, Map<String, Integer>> graph;
	private Map<String, Integer> costs;
	private Map<String, String> parents;
	private List<String> processed;
	private boolean wasStartNode;
	private int shortestPathLength;
	private List<String> shortestPath;
	private String startNode;

	public DijkstraSearch() {
		graph = new HashMap<String, Map<String, Integer>>();
		costs = new HashMap<String, Integer>();
		parents = new HashMap<String, String>();
		processed = new ArrayList<String>();
		wasStartNode = false;
		shortestPath = new ArrayList<String>();
	}

	public void processTheResult(String endNode){
		if (!graph.containsKey(endNode))
			throw new IllegalArgumentException("No end node with this name");
		shortestPath.add(endNode);
		String nodeName = endNode;
		while(!(nodeName = parents.get(nodeName)).equals(startNode)){
			shortestPath.add(nodeName);
		}
		shortestPath.add(startNode);
		Collections.reverse(shortestPath);
		shortestPathLength = costs.get(endNode);
	}

	public int getShortestPathLength() {
		return shortestPathLength;
	}

	public List<String> getShortestPath() {
		return shortestPath;
	}

	public void calculate(String endNode){
		if (startNode == null)
			throw new IllegalArgumentException("No start node has been added.");
		String node = findLowestCostNode(costs, processed);
		while (node != null){
			int cost = (int)costs.get(node);
			Map<String, Integer> neighbors = graph.get(node);
			if (neighbors != null) {
				for (Map.Entry<String, Integer> entry : neighbors.entrySet()) {
					if (entry.getValue() + cost < (int) costs.get(entry.getKey()) || costs.get(entry.getKey()) == -1) {
						costs.put(entry.getKey(), entry.getValue() + cost);
						parents.put(entry.getKey(), node);
					}
				}
			}
			processed.add(node);
			node = findLowestCostNode(costs, processed);
		}
		processTheResult(endNode);
	}

	private static String findLowestCostNode(Map<String, Integer> costs, List<String> processed){
		int min = -1;
		String node = null;
		for (Map.Entry<String, Integer> entry : costs.entrySet()){
			if (((double)entry.getValue() < (double)min || min == -1) && !processed.contains(entry.getKey())){
				min = entry.getValue();
				node = entry.getKey();
			}
		}
		return node;
	}

	public void addNode(String name, Map<String, Integer> children, boolean isStartNode){
		graph.put(name, children);
		if (isStartNode){
			startNode = name;
			if (wasStartNode)
				throw new IllegalArgumentException("Start node has been already added.");
			wasStartNode = true;
			if (children != null) {
				for (Map.Entry<String, Integer> entry : children.entrySet()) {
					costs.put(entry.getKey(), entry.getValue());
					parents.put(entry.getKey(), name);
				}
			}
		} else {
			if (children != null) {
				for (Map.Entry<String, Integer> entry : children.entrySet()) {
					if (!costs.containsKey(entry.getKey()))
						costs.put(entry.getKey(), -1);
					if (!parents.containsKey(entry.getKey()))
						parents.put(entry.getKey(), null);
				}
			}
		}
	}

	public void addNode(String name, Map<String, Integer> children){
		graph.put(name, children);
		if (children != null) {
			for (Map.Entry<String, Integer> entry : children.entrySet()) {
				if (costs.get(entry.getKey()) == null)
					costs.put(entry.getKey(), -1);
				if (parents.get(entry.getKey()) == null)
					parents.put(entry.getKey(), null);
			}
		}
	}

	/*
	first arg - child name;
	second arg - child cost;
	*/
	public static Map<String, Integer> createChildren(String ... args){
		Map<String, Integer> children = new HashMap<String, Integer>();
		for (int i = 0; i < args.length; i += 2){
			String name = args[i];
			if (i + 1 >= args.length)
				throw new IllegalArgumentException("Odd number of arguments");
			int cost = Integer.parseInt(args[i + 1]);
			children.put(name, cost);
		}
		return children;
	}

	public static void main(String[] args) {
		DijkstraSearch dijkstraSearch = new DijkstraSearch();
		dijkstraSearch.addNode("start", DijkstraSearch.createChildren("a", "6", "b", "2"), true);
		dijkstraSearch.addNode("a", DijkstraSearch.createChildren("end", "1"));
		dijkstraSearch.addNode("b", DijkstraSearch.createChildren("a", "3", "end", "5"));
		dijkstraSearch.addNode("end", null);
		dijkstraSearch.calculate("end");
		System.out.println(dijkstraSearch.getShortestPath());
		System.out.println(dijkstraSearch.getShortestPathLength());
	}
}
