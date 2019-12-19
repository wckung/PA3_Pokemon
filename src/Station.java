
/**
 * Defines Station Class
 * @author Chun
 *
 */
public class Station extends Cell {
	
	/**
	 * balls gets from this station
	 */
	private int ball;

	/**
	 * Constructor of the Station class
	 * @param row row value of the cell
	 * @param col col value of the cell
	 * @param ball balls gets from the station
	 */
	public Station(int row, int col, int ball) {
		super(row, col, CellType.STATION);
		// TODO Auto-generated constructor stub
		this.ball = ball;
	}

	/**
	 * 
	 * @return ball value can get from this station
	 */
	public int getBall() {
		return this.ball;
	}
}
