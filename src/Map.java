import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;




/**
 * Defines map where wall, player, station, pokemon exists
 * @author Chun
 *
 */
public class Map {
	
	/**
	 * Row count of the Map
	 */
	public int rows;
	
	/**
	 * Col count of the Map
	 */
	public int cols;
	
	/**
	 * number of pokemon in the map
	 */
	private int pokemon;
	
	/**
	 * number of station in the map
	 */
	private int station;
	
	/**
	 * player of the game
	 */
	public Player player = new Player();

	/**
	 * 2D array of Cell
	 */
	public Cell cell[][];
		
	/**
	 * Resize the map according to the given row and col value
	 * @param M defines row value
	 * @param N defines col value
	 */
	public void setSize(int M, int N) {
		cell = new Cell[M][N];
		rows = M;
		cols = N;
	}
	
	/**
	 * Create cells with the given string
	 * @param line String presentation of the current row
	 */
	public void readRow(int row, String line) {
		for (int j = 0; j < cols; ++j) {
			switch (line.charAt(j)) {
			case '#':
				cell[row][j] = new Wall(row, j);
				break;
			case ' ':
				cell[row][j] = new Cell(row, j, Cell.CellType.EMPTY);
				break;
			case 'B':
				cell[row][j] = new Cell(row, j, Cell.CellType.PLAYER);
				player.setPosition(row, j);
				break;
			case 'D':
				cell[row][j] = new Cell(row, j, Cell.CellType.DESTINATION);
				break;
			case 'S':
				this.station += 1;
				break;
			case 'P':
				this.pokemon += 1;
				break;
			}
		}
	}
	
	/**
	 * reads a pokemon from string
	 */
	public void readPokemon(String line) {
		String str_pos = line.substring(line.indexOf("<") + 1, line.indexOf(">"));
		int row = Integer.parseInt(str_pos.split(",")[0]);
		int col = Integer.parseInt(str_pos.split(",")[1]);
		
		String pokemon_name = line.split(",")[2];
		String pokemon_type = line.split(",")[3];
		int combat_power = Integer.parseInt(line.split(",")[4].trim());
		int ball = Integer.parseInt(line.split(",")[5].trim());
		
		cell[row][col] = new Pokemon(row, col, pokemon_name, pokemon_type, combat_power, ball);
	}
	
	/**
	 * reads a station from string
	 */
	public void readStation(String line) {
		String str_pos = line.substring(line.indexOf("<") + 1, line.indexOf(">"));
		int row = Integer.parseInt(str_pos.split(",")[0]);
		int col = Integer.parseInt(str_pos.split(",")[1]);
		
		int ball = Integer.parseInt(line.split(",")[2].trim());
		
		cell[row][col] = new Station(row, col, ball);
	}
	
	/**
	 * write optimized path to the specified File
	 * @param outputFile
	 */
	public void writePath(File outputFile) {
		player.findPath(cell, outputFile);
	}
	
	/***
	 * Generate a random map which is valid to the requirements
	 */
	public void genRandomMap() {
		// generate row, col, pokemon count, station count
		int M = (int) (Math.random() * 6) + 5;
		int N = (int) (Math.random() * 21) + 10;
		int Np = (int) (Math.random() * 11);
		int Ns = (int) (Math.random() * 6);
		
		rows = M;
		cols = N;
		
		// define pokemon name array, type array
		String pokemonNames[] = { "fearow", "kakuna", "mankey", "nidorino", "ninetales", "pidgey", "pokemon", "rattata", "sandshrew", "Spearow"};
		String pokemonTypes[] = { "Water", "Bug", "Flying", "Ground", "Poison" };
		
		// generate map
		do {
			cell = new Cell[M][N];
						
			//generate wall
			int Nw = (int) ( (Math.random() * 0.3 + 0.7) * (M * N - Np - Ns));
			while (Nw > 0) {
				int row = (int) (Math.random() * M);
				int col = (int) (Math.random() * N);
				if (cell[row][col] == null || cell[row][col].cell_type == Cell.CellType.EMPTY) {
					cell[row][col] = new Wall(row, col);
					--Nw;
				}
			}
			
			// generate pokemon
			while (Np > 0) {
				int row = (int) (Math.random() * M);
				int col = (int) (Math.random() * N);
				if (cell[row][col] == null || cell[row][col].cell_type == Cell.CellType.EMPTY) {
					String name = pokemonNames[(int) (Math.random() * pokemonNames.length)];
					String type = pokemonTypes[(int) (Math.random() * pokemonTypes.length)];
					int power = (int) (Math.random() * 201);
					int ball = (int) (Math.random() * 11);
					cell[row][col] = new Pokemon(row, col, name, type, power, ball);
					Np--;
				} 
			}
			
			// generate station
			while (Ns > 0) {
				int row = (int) (Math.random() * M);
				int col = (int) (Math.random() * N);
				if (cell[row][col] == null || cell[row][col].cell_type == Cell.CellType.EMPTY) {
					int ball = (int) (Math.random() * 21);
					cell[row][col] = new Station(row, col, ball);
					Ns--;
				}
			}
		} while (!isValid());
		
		int wall_count = 0;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("random.txt"));
			bw.write("" + M + " " + N + "\n");
			for (int i = 0; i < M; ++i) {
				for (int j = 0; j < N; ++j) {
					switch (cell[i][j].cell_type) {
					case WALL:
						bw.write("#");
						wall_count++;
						break;
					case EMPTY:
						bw.write(" ");
						break;
					case PLAYER:
						bw.write("B");
						break;
					case DESTINATION:
						bw.write("D");
						break;
					case STATION:
						bw.write("S");
						break;
					case POKEMON:
						bw.write("P");
						break;
					}
				}
				bw.write("\n");
			}
			for (int i = 0; i < M; ++i) {
				for (int j = 0; j < N; ++j) {
					if (cell[i][j].cell_type == Cell.CellType.POKEMON) {
						Pokemon curPokemon = (Pokemon) cell[i][j];
						bw.write("<" + curPokemon.row + "," + curPokemon.col + ">, " + 
								curPokemon.getPokemonName() + ", " + curPokemon.getPokemonType() + ", " + 
								curPokemon.getCombatPower() + ", " + curPokemon.getBall() + "\n");
						
					}
				}
			}
			for (int i = 0; i < M; ++i) {
				for (int j = 0; j < N; ++j) {
					if (cell[i][j].cell_type == Cell.CellType.STATION) {
						Station curStation = (Station) cell[i][j];
						bw.write("<" + curStation.row + "," + curStation.col + ">, " +	curStation.getBall() + "\n");
					}
				}
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * checks if the random generated map is valid or not
	 * @return true when it is a valid map, otherwise returns false
	 */
	private boolean isValid() {
		ArrayList<String> pokemonTypeList = new ArrayList<String>();
		int start_row = 0;
		int start_col = 0;
		int dest_row = 0;
		int dest_col = 0;
		
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				if (cell[i][j] == null) {
					cell[i][j] = new Cell(i, j, Cell.CellType.EMPTY);
				} else if (cell[i][j].cell_type == Cell.CellType.POKEMON) {
					String curType = ((Pokemon) cell[i][j]).getPokemonType();
					if (!pokemonTypeList.contains(curType)) {
						pokemonTypeList.add(curType);						
					}
				} else if (cell[i][j].cell_type == Cell.CellType.PLAYER) {
					start_row = i;
					start_col = j;
				} else if (cell[i][j].cell_type == Cell.CellType.DESTINATION) {
					dest_row = i;
					dest_col = j;
				}
			}
		}
		
		if (pokemonTypeList.size() > 5) {
			return false;
		}

		ArrayList<Cell> visitedCell = new ArrayList<Cell>();
		
		// add player
		int tbLR = (int) (Math.random() * 4);
		int randRow = (int) (Math.random() * cell.length);
		int randCol = (int) (Math.random() * cell[0].length);
		switch (tbLR) {
		case 0: // add player to top
			cell[0][randCol] = new Cell(0, randCol, Cell.CellType.PLAYER);
			visitedCell.add(cell[0][randCol]);
			visitedCell.add(cell[1][randCol]);
			break;
		case 1: // add player to bottom
			cell[cell.length - 1][randCol] = new Cell(cell.length - 1, randCol, Cell.CellType.PLAYER);
			visitedCell.add(cell[cell.length - 1][randCol]);
			visitedCell.add(cell[cell.length - 2][randCol]);
			break;
		case 2: // add player to left
			cell[randRow][0] = new Cell(randRow, 0, Cell.CellType.PLAYER);
			visitedCell.add(cell[randRow][0]);
			visitedCell.add(cell[randRow][1]);
			break;
		case 3: // add player to right
			cell[randRow][cell[0].length - 1] = new Cell(randRow, cell[0].length - 1, Cell.CellType.PLAYER);
			visitedCell.add(cell[randRow][cell[0].length - 1]);
			visitedCell.add(cell[randRow][cell[0].length - 2]);
			break;
		}
		
		int x[] = { 0, 0, 1, -1 };
		int y[] = { 1, -1, 0, 0 };
		int randDir = 0;
		while (true) {
			Cell lastCell = visitedCell.get(visitedCell.size() - 1);
			if ((lastCell.row == 0 || lastCell.row == cell.length - 1 || lastCell.col == 0 || lastCell.col == cell[0].length - 1)
					&& visitedCell.size() > 15) {
				cell[lastCell.row][lastCell.col] = new Cell(lastCell.row, lastCell.col, Cell.CellType.DESTINATION);
				break;
			}
			cell[lastCell.row][lastCell.col] = new Cell(lastCell.row, lastCell.col, Cell.CellType.EMPTY);
			
			do {
				randDir = (int) (Math.random() * 4);
			} while (lastCell.row + x[randDir] < 0 || lastCell.row + x[randDir] > cell.length - 1 ||
					lastCell.col + y[randDir] < 0 || lastCell.col + y[randDir] > cell[0].length -1 ||
					visitedCell.contains(cell[lastCell.row + x[randDir]][lastCell.col + y[randDir]]));
			visitedCell.add(cell[lastCell.row + x[randDir]][lastCell.col + y[randDir]]);
		}
		
		return true;
	}
	
	
	/**
	 * getter for pokemon
	 * @return number of pokemon in the map
	 */
	public int getPokemonCount() {
		return this.pokemon;
	}
	
	/**
	 * getter for station
	 * @return number of station in the map
	 */
	public int getStationCount() {
		return this.station;
	}
}
