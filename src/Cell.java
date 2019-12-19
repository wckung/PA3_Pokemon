
/**
 * Defines a grid of map
 * @author Chun
 *
 */
public class Cell {
	/**
	 * Defines the Celll Type
	 * @author Chun
	 *
	 */
	public enum CellType {
		WALL, EMPTY, PLAYER, DESTINATION, STATION, POKEMON
	}
	
	/**
	 * the row value of the cell
	 */
	public int row;
	
	/**
	 * the column value of the cell
	 */
	public int col;
	
	/**
	 * type of the cell
	 */
	public CellType cell_type;
	
	/**
	 * Constructor of the Cell class
	 * @param row row value of the cell
	 * @param col column value of the cell
	 * @param type type of the cell
	 */
	public Cell(int row, int col, CellType type) {
		this.row = row;
		this.col = col;
		this.cell_type = type;
	}
	
	/**
	 * Constructor of the Cell class
	 */
	public Cell() {
		this.cell_type = CellType.EMPTY;
	}
}
