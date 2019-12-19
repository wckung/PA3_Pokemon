import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The entry point of the game
 * To use text-based game *** --text [inputFile | 'random' [outputFile]]
 * To use gui version *** [input | 'random' [outputFile]]
 * @author Chun
 *
 */
public class PokemonMain extends Application {

	/**
	 * 
	 * @param primaryStage 
	 */
    @Override
    public void start(final Stage primaryStage) {
    	// set the title of the app, set width and height
    	primaryStage.setTitle("Pokemon GUI");
    	primaryStage.setWidth(800);
    	primaryStage.setHeight(600);
    	
    	// add game pane to the stage
    	GamePane gamePane = new GamePane();
    	primaryStage.setScene(new Scene(gamePane, 800, 600));
    	
    	// initialize the gameScene
    	String inputFile = "sampleInput.txt";
    	String outputFile = "sampleOutput.txt";
    	
    	if (getParameters().getUnnamed().size() > 0) {
    		inputFile = getParameters().getUnnamed().get(0);
    	}
    	if (getParameters().getUnnamed().size() > 1) {
    		outputFile = getParameters().getUnnamed().get(1);
    	}
    	gamePane.setIOFiles(inputFile, outputFile);
    	
    	// show the stage
    	primaryStage.show();
    }

    /**
     * reads args and starts gui/text based game
     * @param args
     */
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--text")) { // if the first arg is '--text', launch the text-based game
            final var txtArgs = new ArrayList<>(Arrays.asList(args));
            txtArgs.remove(0);

            final var txtArrayArgs = new String[]{};
            try {
				Game.main(txtArgs.toArray(txtArrayArgs));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            System.exit(0);
        } else { // launch the gui-based game
        	System.out.println("gui");
            PokemonMain.launch(args);
        }
    }
}
