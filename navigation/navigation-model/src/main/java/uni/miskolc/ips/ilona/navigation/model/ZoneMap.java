/**
 * This package domain contains the measurement model class hierarchy.
 */
package uni.miskolc.ips.ilona.navigation.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

/**
 * The ZoneMap class defines graph related methods.
 * 
 * @author kun
 */

public class ZoneMap {

	/**
	 * ZoneMap Constructor
	 */
	private DirectedGraph<UUID, DefaultEdge> zoneGraph;

	public ZoneMap(Collection<UUID> iDs, Collection<Gateway> edges) {

		this.zoneGraph = new DefaultDirectedGraph<UUID, DefaultEdge>(DefaultEdge.class);

		for (UUID uUID : iDs) {

			this.zoneGraph.addVertex(uUID);

		}

		for (Gateway edge : edges) {
			this.zoneGraph.addEdge(edge.getFrom(), edge.getTo());
		}

	}

	/**
	 * find the path for a singular destination inside the graph
	 * 
	 * @param start
	 *            the uuid of the user's current destination
	 * @param destination
	 *            the UUID of the zone to which the user wants to travel
	 * @return a list of UUIDs which collects the shortest path.
	 */
	public List<UUID> findPath(UUID start, UUID destination) throws NoPathAvailableException{
		List<UUID> result = new ArrayList<UUID>();
		DijkstraShortestPath<UUID, DefaultEdge> path = new DijkstraShortestPath<UUID, DefaultEdge>(zoneGraph, start,
				destination);
		if (path.getPath() == null) {
			throw new NoPathAvailableException();
		}
		result = intoUUID(start, path.getPathEdgeList());
		return result;
	}

	/**
	 * find the shortest path when there are multiple available destinations
	 * 
	 * @param start
	 *            the current position of the user
	 * @param destinations
	 *            the possible destinations
	 * @return the shortest path to the closest destination
	 */
	public List<UUID> findPath(UUID start, Collection<UUID> destinations) throws NoPathAvailableException {
		List<UUID> result = new ArrayList<UUID>();
		DijkstraShortestPath<UUID, DefaultEdge> best = new DijkstraShortestPath<UUID, DefaultEdge>(zoneGraph, start,
				destinations.iterator().next());
		for (UUID destination : destinations) {
			DijkstraShortestPath<UUID, DefaultEdge> actual = new DijkstraShortestPath<UUID, DefaultEdge>(zoneGraph,
					start, destination);
			if (actual.getPathLength() < best.getPathLength()) {
				best = actual;
			}
		}
		if (best.getPath()== null) {
			throw new NoPathAvailableException();
		}
		result = intoUUID(start, best.getPathEdgeList());
		return result;

	}

	/**
	 * Transform the list of edges into a list of UUIDs
	 * 
	 * @param start
	 *            the UUID of the starting zone
	 * @param edges
	 *            the list of edges to the destination
	 * @return a list of UUIDs
	 */
	private List<UUID> intoUUID(UUID start, List<DefaultEdge> edges) {
		List<UUID> result = new ArrayList<UUID>();
		result.add(start);
		for (DefaultEdge edge : edges) {
			UUID next = zoneGraph.getEdgeTarget(edge);
			result.add(next);
		}
		return result;
	}
}