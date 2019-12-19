import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * 
 * @author Chun
 *
 */
public class Player {
	/**
	 * row value of the player
	 */
	private int row;
	
	/**
	 * col value of the player
	 */
	private int col;
	
	/**
	 * row number of the map
	 */
	private int rows;
	
	/**
	 * column number of the map
	 */
	private int cols;
	
	/**
	 * NB status for each cell
	 */
	private String NB_status[][];
	
	/**
	 * NS status for each cell
	 */
	private String NP_status[][];
	
	/**
	 * file handle to the output file
	 */
	private File outFile;
	
	/**
	 * at least 1 path is found
	 */
	private boolean isFound = false;
	
	/**
	 * saves all cell infos of the map
	 */
	private Cell cell[][];
	
	/**
	 * number of the balls player has when he arrived
	 */
	private int NB = 0;
	
	/**
	 * number of pokemon the player has caught
	 */
	private int NP = 0;
	
	/**
	 * number of pokemon type the player has caught
	 */
	private int NS = 0;
	
	/**
	 * maximum combat power of the pokeman the player has caught
	 */
	private int MCP = 0;
	
	/**
	 * optimum score of the found path
	 */
	private int score = -10000;
		
	/**
	 * saves the cell the player has walken
	 */
	private ArrayList<Cell> walken = new ArrayList<Cell>();
	
	/**
	 * 
	 */
	public ArrayList<Cell> optimPath = new ArrayList<Cell>();
	
	/**
	 * 
	 */
	private ArrayList<Pokemon> visitedPokemon = new ArrayList<Pokemon>();
	
	/**
	 * 
	 */
	private ArrayList<Station> visitedStation = new ArrayList<Station>();
	
	/**
	 * saves the species of the poken the player has caught
	 */
	private ArrayList<String> caughtPokemonType = new ArrayList<String>();
	
	/**
	 * set position of the player
	 * @param row row value of the player
	 * @param col col vlaue of the player
	 */
	public void setPosition(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	/**
	 * 
	 * @param cell map where player starts to find Path
	 * @param outFile file handler to write optim Path
	 * @return
	 */
	public boolean findPath(Cell[][] cell, File outFile) {
		rows = cell.length;
		cols = cell[0].length;
		NB_status = new String[rows][cols];
		NP_status = new String[rows][cols];
		this.cell = new Cell[rows][cols];
		int start_row = 0;
		int start_col = 0;
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				if (cell[i][j].cell_type == Cell.CellType.POKEMON) {
					Pokemon curCell = (Pokemon) cell[i][j];
					this.cell[i][j] = new Pokemon(i, j, curCell.getPokemonName(),curCell.getPokemonType(), curCell.getCombatPower(), curCell.getBall());
				} else if (cell[i][j].cell_type == Cell.CellType.STATION) {
					Station curCell = (Station) cell[i][j];
					this.cell[i][j] = new Station(i, j, curCell.getBall());
				} else {
					this.cell[i][j] = new Cell(i, j, cell[i][j].cell_type);
				}
				this.NB_status[i][j] = "";
				this.NP_status[i][j] = "";
				if (cell[i][j].cell_type == Cell.CellType.PLAYER) {
					start_row = i;
					start_col = j;
				}
			}
		}
		this.outFile = outFile;
		return search(start_row, start_col);
	}
	
	/**
	 * visit the current cell and check if to continue search or not
	 * and write the optim path to the outputFile	 * 
	 * @return if at least one path is found
	 */
	private boolean search(int row, int col) {
		walken.add(cell[row][col]);
		switch (cell[row][col].cell_type) {
		case DESTINATION:
			isFound = true;
			break;
		case PLAYER:
			break;
		case WALL:
			walken.remove(walken.size() - 1);
			return false;
		case EMPTY:
			break;
		case POKEMON:
			Pokemon curPokemon = (Pokemon) cell[row][col];
			if (NB > curPokemon.getBall() && !visitedPokemon.contains(curPokemon)) {
				NB -= curPokemon.getBall();
				NP += 1;
				visitedPokemon.add(curPokemon);
			}
			break;
		case STATION:
			if (!visitedStation.contains((Station) cell[row][col])) {
				NB += ((Station) cell[row][col]).getBall();
				visitedStation.add((Station) cell[row][col]);
			}
			break;
		}
		
		if (cell[row][col].cell_type == Cell.CellType.DESTINATION) {
			NS = 0;
			MCP = 0;
			
			Iterator<Cell> it = walken.iterator();
			ArrayList<String> pokemonTypeList = new ArrayList<String>();
			while (it.hasNext()) {
				Cell curCell = it.next();
				if (curCell.cell_type == Cell.CellType.POKEMON) {
					Pokemon curPokemon = (Pokemon) curCell;
					if (curPokemon.getCombatPower() > MCP) {
						MCP = curPokemon.getCombatPower();
					}
					if (!pokemonTypeList.contains(curPokemon.getPokemonType())) {
						pokemonTypeList.add(curPokemon.getPokemonType());
						NS++;
					}
				}
			}
			
			if (getScoring() > score) {
				score = getScoring();
				
				try {
					optimPath = new ArrayList<Cell>();
					BufferedWriter bw = new BufferedWriter(new FileWriter(outFile.getPath()));
					
					bw.write("" + score + "\n");
					bw.write("" + NB + ":" + NP + ":" + NS + ":" + MCP + "\n");
					it = walken.iterator();
					Cell curCell = it.next();
					bw.write("<" + curCell.row + "," + curCell.col + ">");
					while (it.hasNext()) {
						curCell = it.next();
						optimPath.add(curCell);
						bw.write("-><" + curCell.row + "," + curCell.col + ">");
					}
					if (!NB_status[row][col].contains("" + NB)) {
						NB_status[row][col] += "," + NB;
					}
					if (!NP_status[row][col].contains("" + NP)) {
						NP_status[row][col] += "," + NP;
					}
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			walken.remove(walken.size() - 1);
			return true;
		}
		
		boolean rtn = false;
		if (!NB_status[row][col].contains("" + NB) || !NP_status[row][col].contains("" + NP)) {
			if (!NB_status[row][col].contains("" + NB)) {
				NB_status[row][col] += "," + NB;
			}
			if (!NP_status[row][col].contains("" + NP)) {
				NP_status[row][col] += "," + NP;
			}
			
			if (row > 0) { // up
				rtn = search(row - 1, col) || rtn;
			}
			if (row < rows - 1) { // down
				if (row + 1 == 5 && col == 4) {
					int i = 1;
				}
				rtn = search(row + 1, col) || rtn;
			}
			if (col > 1) { // left
				rtn = search(row, col - 1) || rtn;
			}
			if (col < cols - 1) { // right
				if (row == 8 && col + 1 == 15) {
					int i = 1;
				}
				rtn = search(row, col + 1) || rtn;
			}
		}
		Cell removedCell = walken.remove(walken.size() - 1);
		switch (removedCell.cell_type) {
		case STATION:
			if (visitedStation.contains((Station) removedCell) && !walken.contains(removedCell)) {
				NB -= ((Station) removedCell).getBall();
				visitedStation.remove((Station) removedCell);
			}
			break;
		case POKEMON:
			if (visitedPokemon.contains((Pokemon) removedCell) && !walken.contains(removedCell)) {
				NP -= 1;
				NB += ((Pokemon) removedCell).getBall();
				visitedPokemon.remove((Pokemon) removedCell);
			}

			break;
		}
		return rtn;
	}
	
	/**
	 * returns the scoring function value according to NB, Ns, NP, MCP, steps
	 */
	private int getScoring() {
		return NB + 5 * NP + 10 * NS + MCP - walken.size() + 1;
	}
	
	/**
	 * return NB value
	 */
	public int getNB() {
		return NB;
	}
	
	/**
	 * return NP value
	 */
	public int getNP() {
		return NB;
	}
	
	/**
	 * return NS value
	 */
	public int getNS() {
		return NS;
	}
	
	/**
	 * return MCP value
	 * 
	 */
	public int getMCP() {
		return MCP;
	}
}
