package uni.miskolc.ips.ilona.positioning.service.impl.knn;

import uni.miskolc.ips.ilona.measurement.model.measurement.MeasurementDistanceCalculator;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.service.MeasurementService;

import java.util.ArrayList;

/**
 * 
 * @author ilona
 *
 */
public class KNNSimplePositioning extends KNNPositioning {

	/**
	 * The constructor of the KNNSimplePositioning class.
	 * 
	 * @param distanceCalculator
	 *            The distance function used in k-NN.
	 * @param measurementservice
	 *            The measurementservice provide access to the measurements in
	 *            the database
	 * @param k
	 *            is the k parameter of the k Nearest Neighbour algorithm.
	 */
	public KNNSimplePositioning(final MeasurementDistanceCalculator distanceCalculator,
			final MeasurementService measurementservice, final int k) {
		super(distanceCalculator, measurementservice, k);
	}

	@Override
	protected final Position getMajorVote(final ArrayList<Neighbour> nearestneighbours) {
		ArrayList<Zone> zones = new ArrayList<Zone>();
		int maxsize = nearestneighbours.size();
		double[] votes = new double[maxsize];
		Position result = null;
		for (Neighbour n : nearestneighbours) {
			Zone zone = n.getMeasurement().getPosition().getZone();
			if (!zones.contains(zone)) {
				zones.add(zone);
				votes[zones.indexOf(zone)] = 1;
			} else {
				votes[zones.indexOf(zone)]++;
			}
		}
		int maxindex = getIndexOfMaxValue(votes);
		result = new Position(zones.get(maxindex));
		return result;
	}

}