package panic.game;

import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import panic.io.CustomLoggerFormatter;

/**
 * <b>Launcher class</b><br>
 * Gets the game going
 * @author Sam Paniccia
 *
 */
public class Father implements Runnable {

	/**
	 * The GameController instance: controls pretty much everything
	 */
	private GameController gc;

	/**
	 * Logging instance
	 */
	private static final Logger logger =
			Logger.getLogger(Father.class.getName());

	/**
	 * The current version of the game
	 */
	public static final String GAME_VERSION = "0";

	/**
	 * A Random instance for generating random things
	 * @param args
	 */
	private static Random rand = new Random();

	/**
	 * A list of instructions for the game to automatically perform at the beginning of the game
	 * @param args
	 */
	private String[] args;

	/*
	 * DEBUGGING CONSTANTS
	 */

	/**
	 * If true, undiscovered alleles are not hidden from the player
	 */
	public static int SHOW_UNDISCOVERED_ALLELES = 0;

	public static int SHOW_UNDISCOVERED_ALLELES_TRUE = 1;
	public static int SHOW_UNDISCOVERED_ALLELES_FALSE = 0;

	public static void main(String[] args) {
		//DataIO.loadTileValues();
		//DataIO.loadOrgTileTraitVals();
		//DataIO.loadAlleleValues();
		new Father(args).run();
	}

	private Father(String[] args) {
		this.args = args;
	}


	@Override
	public void run() {
		this.initLogger();
		gc = GameController.getInstance();
		gc.init();
		// Initialize GUI on EDT
		SwingUtilities.invokeLater(() -> gc.initGUI());

		gc.showIntro();
		gc.parseArgs(this.args);
	}

	/**
	 * To give the logger custom formatting
	 */
	private void initLogger(){
		try{
			CustomLoggerFormatter format = new CustomLoggerFormatter();
			FileHandler fh = new FileHandler("res/log/log" + 0 + ".log");
			Father.logger.setUseParentHandlers(false);
			fh.setFormatter(format);
			Father.logger.addHandler(fh);

			Father.log("Logger initialized. Let's go!");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Get a new random integer between lo and hi
	 */
	public static int randInt(int lo, int hi) {
		return (Father.rand.nextInt(hi) + lo);
	}

	public static void log(String toLog) {
		Father.logger.info(toLog);
	}

}




