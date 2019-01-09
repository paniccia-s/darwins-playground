package panic.state;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Queue;

import panic.game.GameController;

/**
 * An abstract frame for game states.<br>
 * Each state needs to handle all types of user input.<br>
 * Each explicit GameState subclass overrides all useful methods to 
 * provide functionality. Additionally, each subclass may have states of its
 * own, which are stored in the stateQueue field. Each of these substates is 
 * initialized as an anonymous AnonState instance, and all necessary fields
 * are anonymously overridden as appropriate.
 * @author Sam Paniccia
 *
 */
public abstract class GameState {
  
  /**
   * The user-friendly name of this state (for toString)
   */
  protected String stateName;
  
  /**
   * GameController reference
   */
  protected GameController gc;
  
  /**
   * Queue of states
   */
  protected Queue<GameState> stateQueue; 
  
  public GameState(GameController gc) { 
    this.gc = gc;
    this.stateName = this.setName();
    this.stateQueue = new LinkedList<GameState>();
    this.initStateStructures();
  }
  
  /**
   * To run when this State becomes the active state.
   */
  public void onLoad() {
    
  }
  
  /**
   * To run when this State is exited
   */
  public void onExit() {
    
  } 
  
  /**
   * To set the user-friendly name of this state
   */
  protected abstract String setName();
  
  /**
   * To add the proper states to this state's state stack
   */
  protected abstract void initStateStructures(); 

  /**
   * To react to text input
   * @param text What the user sent
   */
  public void onTextInput(String text) { 
    
  }
  
  @Override
  public String toString() {
    return this.stateName;
  }
  
  /**
   * To change to the next substate
   */
  public void nextSubstate() {
    
  }
  
  /**
   * To reset this State to its initial substate and to add back all passed substates
   */
  public void reset() {
    this.initStateStructures();
  }
  
  /**
   * To draw appropriate information onto the PlayPanel
   * @param g The PlayPanel's Graphics2D reference
   */
  public void drawPlayPanel(Graphics2D g) {
    
  }
  
  // need one method per button press...
  
  /**
   * To react to pressing the New button
   */
  public void onButtonNew() {
    System.out.println(this.stateName + " New");
  }

  /**
   * To react to pressing the Load button
   */
  public void onButtonLoad() {
    System.out.println(this.stateName + " Load");
    
  }

  /**
   * To react to pressing the Save button
   */
  public void onButtonSave() {
    this.gc.saveGame();
  }

  /**
   * To react to pressing the Start button
   */
  public void onButtonStart() { 
    
  }
  
  /**
   * To react to pressing the Back button<br>
   * <b>NOTE:</b> this method defaults to restoring the last state in this.gc. 
   * This is DIFFERENT from most other functions, which default to doing nothing.
   */
  public void onButtonBack() {
    this.gc.lastState();
  } 

  /**
   * To react to pressing the Exit button<br>
   * <b>NOTE:</b> this method defaults to running GameController#exitGame()!
   */
  public void onButtonExit() { 
    this.gc.exitGame();
  }
  
  /**
   * To react to pressing the Mutate button
   */
  public void onButtonMutate() {
    
  }

  /**
   * To react to pressing the Breed button
   */
  public void onButtonBreed() {
    
  } 
  
  /**
   * To react to pressing the Place button
   */
  public void onButtonPlace() {
    
  }

  /**
   * To react to a mouse click on the PlayPanel
   * @param x
   * @param y
   */
  public void onMouseClick(int x, int y) {
    //System.out.println(x + ", " + y);
  }

  /**
   * 
   */
  public void onButtonObserve() {
    
  }

  public void onButtonEngineer() {
    
  }
  
}
