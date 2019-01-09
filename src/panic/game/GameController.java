package panic.game;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Stack;

import javax.swing.SwingUtilities;

import panic.gene.Allele;
import panic.gene.Trait;
import panic.gui.GUIHandler;
import panic.io.DataIO;
import panic.org.Organism;
import panic.state.GameState;
import panic.state.StateIntro;
import panic.state.StateViewDeaths;
import panic.tile.BoardTile;

/**
 * A Singleton to control all aspects of gameplay
 * @author Sam Paniccia
 *
 */
public class GameController {
  
  /**
   * The GameController singleton 
   */
  private static final GameController instance = new GameController();

  /**
   * A Stack of GameStates representing the current state and
   * any relevant states that came before it
   */
  private Stack<GameState> stateStack;
  
  /**
   * A global GUIHandler for all GUI manipulation 
   */
  private GUIHandler guiHandler;  
  
  /**
   * The Player in play
   */
  private Player player;
  
  private GameController() {}
  
  /**
   * To get the singleton instance of this class 
   */
  public static GameController getInstance() {
    Father.log("GameController instance retrieved.");
    return GameController.instance;
  }
  
  // initializing
  
  /**
   * To initialize this.stateStack and to load in all game data
   */
  void init() { 
    this.stateStack = new Stack<GameState>();
    this.stateStack.push(new StateIntro(this));
    Father.log("Intro state pushed, but NOT loaded!");
    DataIO.loadOrgTileTraitVals(); 
    DataIO.loadAlleleValues();
    guiHandler = GUIHandler.getInstance(this);
  }
  
  /**
   * To initialize all GUI components (through this.guiHandler)
   */
  void initGUI() { 
    Father.log("Initializing GUI components...");
    guiHandler.initGUI();
    Father.log("GUI initialization complete.");
  }
  
  
  /**
   * To display to the user that it's time to start
   */
  void showIntro() {
    this.stateStack.peek().onLoad();
    Father.log("Intro state loaded.");
  } 
  
  // drawing

  /**
   * To refresh the PlayPanel's image
   */
  public void repaint() { 
    Father.log("repainting the PlayPanel.");
    this.guiHandler.repaint();  
  }
  
  /**
   * To draw the PlayPanel based on the current state
   * @param g The PlayPanel's Graphics2D instance
   */
  public void drawPlayPanel(Graphics2D g) { 
    this.stateStack.peek().drawPlayPanel(g);
  }
  
  /**
   * To draw all BoardTiles onto the PlayPanel
   * @param g The PlayPanel's Graphics2D instance
   */
  public void drawTiles(Graphics2D g) { 
      this.player.drawTiles(g);  
  } 
  
  /**
   * To render all Organisms (textually) onto the PlayPanel
   * @param g
   */
  public void drawOrganisms(Graphics2D g) { 
      this.player.drawOrganisms(g); 
  }
  
  /**
   * To render text on the PlayPanel
   * @param g
   * @param toSay
   */
  public void renderTextOnPlayPanel(Graphics2D g, String[] toSay) {
    this.guiHandler.renderTextOnPlayPanel(g, toSay);
  }
  
  // user input
  
  /**
   * To react to user text input
   * @param input What the user has entered
   */
  public void userInput(String input) { 
    this.stateStack.peek().onTextInput(input);
  }
  
  /**
   * To react to the user pressing a button
   * @param which The name of the pressed button (eqivalent to 
   * the text on the button)
   */
  public void buttonPressed(String which) {
    Father.log("Button pressed: " + which);
    switch(which) {
    case "New":
      this.stateStack.peek().onButtonNew();
      break;
    case "Load": 
      this.stateStack.peek().onButtonLoad(); 
      break;
    case "Save": 
      this.stateStack.peek().onButtonSave(); 
      break;
    case "Start": 
      this.stateStack.peek().onButtonStart(); 
      break;
    case "Back":  
      this.stateStack.peek().onButtonBack(); 
      break;
    case "Exit": 
      this.stateStack.peek().onButtonExit();
      break;
    case "Mutate":
      this.stateStack.peek().onButtonMutate();
      break;
    case "Breed":
      this.stateStack.peek().onButtonBreed();
      break;
    case "Place":
      this.stateStack.peek().onButtonPlace();
      break;
    case "Observe":
      this.stateStack.peek().onButtonObserve();
      break;
    case "Engineer":
      this.stateStack.peek().onButtonEngineer();
      break;
    }
  }

  /**
   * To handle a mouse click on the PlayPanel
   * @param x X coordinate of the click, in pixels
   * @param y Y coordinate of the click, in pixels
   */
  public void onMouseClick(int x, int y) {
    this.stateStack.peek().onMouseClick(x, y);
  }
  
  
  // user output
  
  /**
   * To display a text message to the user
   * @param text What to show the user
   */
  public void setText(String text) {
    SwingUtilities.invokeLater(() -> { 
      Father.log("Text set: " + text);
      this.guiHandler.setText(text); 
    });
  }
  
  /**
   * To add text onto whatever is being shown already
   * @param text What to add onto the text panel
   */
  public void addText(String text) {
    SwingUtilities.invokeLater(() -> {
      Father.log("Text added: " + text);
      this.guiHandler.addText(text);
    });
  }

  /**
   * To show the user the current turn and the turn limit
   */
  public void showGameDataOnTurnStart() {
    int turn = this.player.getCurrentTurn();
    int limit = this.player.getTurnLimit();
    
    this.setText("\nIt is turn " + turn + " of " + limit + ".");
  }
  
  
  // state manipulation
  
  /**
   * To change the substate of the current State
   */
  public void nextSubstate() { 
    this.stateStack.peek().nextSubstate();
    this.stateStack.peek().onLoad();
  } 
  
  /**
   * To go to the previous state
   */
  public void lastState() {
    this.stateStack.pop();
    this.stateStack.peek().onLoad();
  }
  
  /**
   * To progress the game to the next turn
   */
  public void nextTurn() {
    // this can only happen during gameplay
    this.stateStack.pop();
    String[] deathMessages = this.player.nextTurn(); 
    this.saveGame();
    
    if (deathMessages.length != 0) {
      this.stateStack.add(new StateViewDeaths(this).acceptDeathMessages(deathMessages)); 
      this.stateStack.peek().onLoad();
      this.repaint();
      //System.out.println(deathMessages[0]);
    } else {
      this.stateStack.peek().onLoad();
    }
    
    //this.stateStack.peek().onLoad();
  }
  
  /**
   * To add the given GameState onto the state stack
   * @param toAdd Which GameState to add
   */
  public void addState(GameState toAdd) {
    this.stateStack.push(toAdd); 
    this.stateStack.peek().onLoad();
  }
  
  /**
   * To clear the state stack
   */
  public void clearStateStack() {
    this.stateStack.clear();
  }
  
  // player creation
  
  /**
   * To create a new Player with the given name
   */
  public void newPlayer(String name) {
    Father.log("Creating new player: " + name);
    this.player = new Player(name, this); 
  }
  
  /**
   * To attempt to load player data of the given name 
   * @return Whether a player file of the given name exists
   */
  public boolean loadPlayer(String name) {
    this.player = DataIO.loadPlayer(name);
    return (this.player != null); 
  }
  
  /**
   * To set this.player to the given Player
   */
  public void setPlayer(Player p) {
    this.player = p; 
  }
  
  // handler delegation
  
  /**
   * To change the button layout from the six intro buttons to the eight gameplay buttons
   */
  public void changeButtons() {
    SwingUtilities.invokeLater(() -> {
      Father.log("Changing button layout.");
      this.guiHandler.changeButtons();
    });
  }
  
  
  
  // player delegation
  
  /**
   * To add an Organism for this Player
   */
  public void addOrganism(Organism toAdd) {
    this.player.addOrganism(toAdd);
    //this.printCurrentPlayer();
  }

  /**
   * To get this.player's Organism of the given name
   */
  public Optional<Organism> getOrgFromName(String name) { 
    return this.player.getOrgFromName(name);
  }
  
  /**
   * To get this.player's Allele of the given name if it exists and has been discovered
   */
  public Optional<Allele> getAlleleFromName(String name) {
    return this.player.getAlleleFromNameIfDiscovered(name);
  }

  /**
   * To retrieve the factor of successful mutation from the player's Scenario
   */
  public double getMutationFactor() {
    return this.player.getMutationFactor();
  } 

  /**
   * To determine which tile was clicked, given the coordinates of the click
   * @param x
   * @param y
   * @return
   */
  public BoardTile determineTileFromClick(int x, int y) {
    return this.player.determineTileFromClick(x, y);
  }

  // Organism delegation
  
  /**
   * To mutate the given Organism
   */
  public boolean mutateOrganism(Organism toMutate, double chance) { 
    return this.player.mutateOrganism(toMutate, chance); 
  }
  
  
  // state initialization
  
  /**
   * To transition into the game loop after loading/creating player data.
   * <br><b>Clears</b> the state stack of all intro/loading states- the player should
   * not go back to the intro section of the game once gameplay has begun!
   */
  /*public void startGame() {
    this.stateStack = new Stack<GameState>();
    this.stateStack.push(new StateGameLoop(this));
    this.stateStack.peek().onLoad();
    
    this.guiHandler.changeButtons();
  }*/
  
  /**
   * To save the current game
   */
  public void saveGame() {
    DataIO.writePlayer(this.player); 
  } 

  /**
   * To prompt the user to save or cancel before exiting the game
   */
  public void exitGame() {
    // commented out for convenience 
    /*boolean confirm = this.guiHandler.confirmExit();
    if (confirm) {
      System.exit(0);
    }*/
    System.exit(0);
  }

  // debugging
  
  /**
   * To run through a sequence of mocked user inputs supplied by method main
   * <br>textIn, button, mouse
   * <br>method-name, button-name, coordinates
   * @param args The inputs
   */
  public void parseArgs(String[] args) {

    try {
      Thread.sleep(250);
    }
    catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    Father.log("Parsing input arguments...");
    for (String arg : args) { 
      String[] parts = arg.split("_");
      Father.log(" - " + parts[0]);
      switch(parts[0]) {
      case "flag":
        String which = "";
        for (int i = 1; i < parts.length - 1; i++) {
          which += "_" + parts[i] ;
        }
        
        which = which.substring(1);
        String val = parts[parts.length - 1];
        
        int value = Integer.valueOf(val);
        
        try {
          Field f = Father.class.getDeclaredField(which);
          f.set(null, value);
        }
        catch (NoSuchFieldException | SecurityException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        catch (IllegalAccessException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } 
        break;
      case "stop":
        Father.log("Argument parsing complete: stop request received.");
        return;
      case "load": 
        this.updateGameData();
        break;
      case "textIn":
        Father.log("   - " + parts[1]);
        this.userInput(parts[1]);
        break;
      case "button":
        Father.log("   - " + parts[1]);
        this.buttonPressed(parts[1]);
        break;
      case "mouse":
        int x = Integer.valueOf(parts[1]) * 48 + 100;
        int y = Integer.valueOf(parts[2]) * 48 + 100;
        Father.log("   - Clicked tile: " + parts[1] + ", " + parts[2]);
        this.onMouseClick(x, y);
        break;
      default: 
        Father.log("Argument parsing FAILED! " + arg);
        throw new IllegalArgumentException("I did something wrong: " + arg);
      } 
    }
    Father.log("Argument parsing complete: all requests handled.");
  }

  /**
   * To reload all gamedata files through exceltotext.py
   */
  private void updateGameData() {
    try {
      Father.log("Attempting to reload gamedata files: " );
      Father.log("\tallele_trait_vals.txt");
      Father.log("\torg_tile_trait_vals.txt");
      Father.log("\ttile_trait_vals.txt");
      ProcessBuilder pb = new ProcessBuilder("python", "res\\exceltotext.py");
      Process p = pb.start(); 
      
      InputStreamReader is = new InputStreamReader(p.getInputStream());
      BufferedReader in = new BufferedReader(is); 
      
      in.close();
      Father.log("Read complete!");
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-2);
    }
  } 
  
  public String[] getAllelesAsStrings(Trait which) {
    return this.player.getStringRepOfDiscoveredAlleles(which);
  }
  
}


