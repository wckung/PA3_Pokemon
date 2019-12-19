/**
 * Defines Pokemon Class
 * @author Chun
 *
 */
public class Pokemon extends Cell {
	/**
	 * name of the pokemon
	 */
	private String pokemon_name;
	
	/**
	 * type of the pokemon
	 */
	private String pokemon_type;
	
	/**
	 * combat power of the pokemon
	 */
	private int combat_power;
	
	/**
	 * balls needed to catch the pokemon
	 */
	private int ball;

	/**
	 * Constructor of the Pokemon Class
	 * @param row row value of the Cell
	 * @param col column value of the cell
	 * @param name name of the pokemon
	 * @param type type of the pokemon
	 * @param power combat power of the pokemon
	 * @param ball balls needed to catch the pokemon
	 */
	public Pokemon(int row, int col, String name, String type, int power, int ball) {
		super(row, col, CellType.POKEMON);
		// TODO Auto-generated constructor stub
		this.pokemon_name = name;
		this.pokemon_type = type;
		this.combat_power = power;
		this.ball = ball;
	}

	/**
	 * @return name of the pokemon
	 */
	public String getPokemonName() {
		return this.pokemon_name;
	}
	
	/**
	 * 
	 * @return type of the pokemon
	 */
	public String getPokemonType() {
		return this.pokemon_type;
	}
	
	/**
	 * 
	 * @return balls needed to catch this pokemon
	 */
	public int getBall() {
		return this.ball;
	}
	
	/**
	 * 
	 * @return combat power of this pokemon
	 */
	public int getCombatPower() {
		return this.combat_power;
	}
}
