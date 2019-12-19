import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Defines the game class
 * @author Chun
 *
 */
public class Game {
	
	/**
	 * defines map of the game
	 */
	public Map map = new Map();
	
	/**
	 * 
	 * @param inputFile
	 * @throws Exception
	 */
	public void initialize(File inputFile) throws Exception{
		if (inputFile.getPath().equals("random")) {
			map.genRandomMap();
			return;
		}
		
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		
		// Read the first of the input file
		String line = br.readLine();
		int M = Integer.parseInt(line.split(" ")[0]);
		int N = Integer.parseInt(line.split(" ")[1]);
		
		// TODO: define a map
		map.setSize(M, N);
		
		// Read the following M lines of the Map
		for (int i = 0; i < M; i++) {
			line = br.readLine();
			
			// TODO: Read the map line by line
			map.readRow(i, line);
		}
		
		// TODO:
		// Find the number of stations and pokemons in the map 
		// Continue read the information of all the stations and pokemons by using br.readLine();
		int pokemonCnt = map.getPokemonCount();
		for (int i = 0; i < pokemonCnt; ++i) {
			line = br.readLine();
			
			map.readPokemon(line);
		}
		
		int stationCount = map.getStationCount();
		for (int i = 0; i < stationCount; ++i) {
			line = br.readLine();
			
			map.readStation(line);
		}
		
		br.close();
	}
	
	/**
	 * The entry function of the text-based game
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		File inputFile = new File("./sampleInput.txt");
		File outputFile = new File("./sampleOutput.txt");
		
		if (args.length > 0) {
			inputFile = new File(args[0]);
		} 

		if (args.length > 1) {
			outputFile = new File(args[1]);
		}
		
		// TODO:
		// Read the configures of the map and pokemons from the file inputFile
		// and output the results to the file outputFile
		Game game = new Game();
		
		game.initialize(inputFile);
		game.map.writePath(outputFile);
	}
	
	/**
	 * draw path
	 */
	public void updateMap(Canvas canvas, int ticksElapsed) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		List<Cell> steps = map.player.optimPath;
		gc.save();
		gc.setFill(Color.GREEN);
		for (int i = 0; i < ticksElapsed; ++i) {
			int posX = steps.get(i).col * 32;
			int posY = steps.get(i).row * 32;
			
			gc.fillRect(posX, posY, 32, 32);
		}
		gc.restore();
		
		int posX = steps.get(ticksElapsed).col * 32;
		int posY = steps.get(ticksElapsed).row * 32;
		gc.drawImage(new Image("file:///../resources/images/player.png"), posX, posY, 32, 32);
		
		int rows = map.rows;
		int cols = map.cols;
		
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				Cell curCell = map.cell[i][j];
				posX = j * 32;
				posY = i * 32;
				switch (curCell.cell_type) {
				case WALL:
			        gc.drawImage(new Image("file:///../resources/images/wall.png"), posX, posY, 32, 32);
					break;
				case STATION:
					gc.drawImage(new Image("file:///../resources/images/ball.png"), posX, posY, 32, 32);
					break;
				case POKEMON:
					Pokemon curPokemon = (Pokemon) curCell;
					Image pokemonImg = new Image("file:///../resources/images/pokemon/" + curPokemon.getPokemonName().trim() + ".png");
					if (pokemonImg == null) {
						pokemonImg = new Image("file:///../resources/images/pokemon/pokemon.png");
					}
					gc.drawImage(pokemonImg, posX, posY, 32, 32);
					break;
				case EMPTY:
					//gc.drawImage(new Image("file:///../resources/images/wall.png"), posX, posY, 32, 32);
					break;
				case DESTINATION:
					gc.drawImage(new Image("file:///../resources/images/victory.png"), posX, posY, 32, 32);
					break;
				case PLAYER:
					gc.drawImage(new Image("file:///../resources/images/player.png"), posX, posY, 32, 32);
					break;
				}
			}
		}
	}
}
