package uk.org.landeg.kalah.game;

import java.util.HashMap;

/**
 * Wrapper for board pits with some commonly used utility methods.
 * 
 * @author Andrew Landeg
 *
 */
public class KalahPitDecorator extends HashMap<Integer, Integer>{
	private static final long serialVersionUID = -2810249315342522940L;

	/**
	 * increments the pit with the specified ID by the specified amount.
	 * @param id pit id
	 * @param amount the ammount to increment.
	 */
	public void increment(int id, int amount) {
		int total = this.get(id) + amount;
		this.put(id, total);
	}

	/**
	 * takes stones from the pit with the specified ID
	 * @param id the id of the pit to take from
	 * @return number of stones taken.
	 */
	public int take(int id) {
		int amount = this.get(id);
		this.put(id, 0);
		return amount;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
