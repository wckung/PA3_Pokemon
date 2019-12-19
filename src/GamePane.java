import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Pokemon GUI scene which shows map and animates optimized path
 * @author Chun
 *
 */
public class GamePane extends BorderPane {

	/**
	 * Header that displays VF, NB, NP, NS, MCP value
	 */
	private HBox topBar = new HBox(20);
	
	/**
	 * Label that displays the VF - scoring function
	 */
	private Label lbl_VF = new Label("");
	
	/**
	 * Label that displays the NB - Number of Poke balls of the player when he arrives at the destination
	 */
	private Label lbl_NB = new Label();
	
	/**
	 * Label that displays the NP - Number of Pokémons that the player has caught
	 */
	private Label lbl_NP = new Label();
	
	/**
	 * Label that displays the NS - Number of distinct types of all Pokémons that the player has caught
	 */
	private Label lbl_NS = new Label();
	
	/**
	 * Label that displays the MCP - The maximum combat power of all Pokémons that the player has caught 
	 */
	private Label lbl_MCP = new Label();
	
	/**
	 * tick used for animate path
	 */
	private int ticksElapsed = 0;
	
	/** 
	 * Canvas which displays map and animated path
	 */
	private Canvas map = new Canvas();
	
	/**
	 * used for animate path
	 */
	Timer timer = new Timer();
	
	/**
	 * constructor
	 */
	GamePane() {
		connectComponents();
		styleComponents();
		setCallbacks();
	}
	
    /**
     * Connects all components into the {@link BorderPane}.
     */
    void connectComponents() {
    	// add labels to the header
    	topBar.getChildren().add(lbl_VF);
    	topBar.getChildren().add(lbl_NB);
    	topBar.getChildren().add(lbl_NP);
    	topBar.getChildren().add(lbl_NS);
    	topBar.getChildren().add(lbl_MCP);
    	
    	// add topbar and map to the scene
    	this.setTop(topBar);
    	this.setCenter(map);
    	
    }

    /**
     * Styles all components as required.
     */
    void styleComponents() {
    	
    }

    /**
     * Set callbacks for all interactive components.
     */
    void setCallbacks() {
    	
    }
    
    /**
     * Set input file and output file
     * @param inFile
     * @param outFile
     */
    public void setIOFiles(String inFile, String outFile)  {
    	Game game = new Game();

		try {
			game.initialize(new File(inFile));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		game.map.writePath(new File(outFile));
		
		// Number of poke balls of the player when he arrives at destination.
		int nb = game.map.player.getNB();
		lbl_NB.setText("NB: " + nb);

		// Number of pokemon that the player has caught.
		int np = game.map.player.getNP();
		lbl_NP.setText("NP: " + np);

		// Number of distinct types of all Pokemon that the player has caught.
		int ns = game.map.player.getNS();
		lbl_NS.setText("NS: " + ns);

		// Maximum combat power of all pokemons that the player has caught.
		int mcp = game.map.player.getMCP();
		lbl_MCP.setText("MCP: " + mcp);

		// calculate the VF value
		int vf = (nb + 5 * np + 10 * ns + mcp) - game.map.player.optimPath.size();
		lbl_VF.setText("VF: " + vf);
		
		// reset canvas size
		map.setWidth(game.map.cols * 32);
		map.setHeight(game.map.rows * 32);		
		
		int steps = game.map.player.optimPath.size();
		
		// animate path here
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				game.updateMap(map, ticksElapsed);
				ticksElapsed += 1;
				
				if (ticksElapsed >= steps) {
					timer.cancel();
				}
			}
		}, 0, 500);
    }
}
