/**
 * This package domain contains the measurement model class hierarchy.
 */
package uni.miskolc.ips.ilona.navigation.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import uni.miskolc.ips.ilona.measurement.model.position.Zone;

/**
 * The ZoneMap class defines graph related methods.
 * 
 * @author kun
 */

public class ZoneMap {

	/**
	 * Logger.
	 */
	private static final Logger LOG = LogManager.getLogger(ZoneMap.class);

	/**
	 * 
	 */
	private DirectedGraph<UUID, DefaultEdge> zoneGraph;

	public ZoneMap(Collection<UUID> iDs,Collection<Gateway> edges) {

		this.zoneGraph = new DefaultDirectedGraph<UUID, DefaultEdge>(DefaultEdge.class);

		for (UUID uUID : iDs) {

			this.zoneGraph.addVertex(uUID);

			//LOG.info(zone.getName() + " added to the graph");

		}
		
		for (Gateway edge : edges){
			this.zoneGraph.addEdge(edge.getFrom(), edge.getTo());
		}

	}
	
	/**
	 * find the path for a singular destination inside the graph
	 * @param start the uuid of the user's current destination
	 * @param destination the UUID of the zone to which the user wants to travel
	 * @return a list of UUIDs which collects the shortest path.
	 */
	public List<UUID> findPath(UUID start, UUID destination){
		List<UUID> result = new ArrayList<UUID>();
		//System.out.println(start.toString()+ destination.toString());
		DijkstraShortestPath<UUID, DefaultEdge> path= new DijkstraShortestPath<UUID, DefaultEdge>(zoneGraph, start, destination);
		result=intoUUID(start,path.getPathEdgeList());
		//System.out.println(result.toString());
		return result;
	}

	/**
	 * find the shortest path when there are multiple availible destinations
	 * @param start the current position of the user
	 * @param destinations the possible destinations
	 * @return the shortest path to the closest destination
	 */
	public List<UUID> findPath(UUID start, Collection<UUID> destinations){
		List<UUID> result=new ArrayList<UUID>();
		DijkstraShortestPath<UUID, DefaultEdge> best = new DijkstraShortestPath<UUID, DefaultEdge>(zoneGraph, start, destinations.iterator().next());
		for(UUID destination : destinations){
			DijkstraShortestPath<UUID, DefaultEdge> actual = new DijkstraShortestPath<UUID, DefaultEdge>(zoneGraph, start, destination);
			if(actual.getPathLength()<best.getPathLength()){
				best=actual;
			}
		}
		result=intoUUID(start, best.getPathEdgeList());
		return result;
		
	}


	/**
	 * Transform the list of edges into a list of UUIDs
	 * @param start the UUID of the starting zone
	 * @param edges the list of edges to the destination
	 * @return a list of UUIDs
	 */
	private List<UUID> intoUUID (UUID start, List<DefaultEdge> edges){
		List<UUID> result = new ArrayList<UUID>();
		result.add(start);
		for(DefaultEdge edge : edges){
			UUID next = zoneGraph.getEdgeTarget(edge);
			result.add(next);
		}
		return result;
	}
}