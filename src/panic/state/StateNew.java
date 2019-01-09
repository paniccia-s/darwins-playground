package panic.state;

import java.awt.Color;
import java.awt.Graphics2D;

import panic.game.GameController;
import panic.gene.Sex;
import panic.org.OBactCyano;
import panic.org.Organism;

public class StateNew extends GameState {

  /**
   * The player name
   */
  private String playerName;
  
  public StateNew(GameController gc) {
    super(gc); 
  }
  
  @Override
  public String setName() {
    return "new";
  }

  /**
   * Each substate is an anonymous instance whose relevant
   * methods are overridden
   */
  @Override
  protected void initStateStructures() {  
    this.stateQueue.add(new AnonState(this.gc, "new-intro"){
      
      @Override
      public void onLoad() {
        this.gc.setText("\nLet's start a new game. What is your name?");
      }

      @Override
      public void onTextInput(String name) {
        gc.newPlayer(name);
        playerName = name; 
        gc.nextSubstate();
      }
      
    }); 
    
    this.stateQueue.add(new AnonState(this.gc, "new-name-I") {
      
      @Override
      public void onLoad() {
        gc.setText("\nHello, " + playerName + "!\n\n"
            + "You will start with the tutorial.\n"
            + "For this level, you start with two Organisms.\n"
            + "What would you like to name the first?");
      }
      
      @Override
      public void onTextInput(String firstOrgName) {
        Organism toAdd = new OBactCyano(firstOrgName, Sex.male); 
        gc.addOrganism(toAdd); 
        gc.nextSubstate();
      }
       
    });
    this.stateQueue.add(new AnonState(this.gc, "new-name-II") {
      
      @Override
      public void onLoad() { 
        gc.setText("\nAnd the second?");
      }
      
      @Override
      public void onTextInput(String secondOrgName) {
        Organism toAdd = new OBactCyano(secondOrgName, Sex.female); 
        gc.addOrganism(toAdd); 
        gc.repaint();
        gc.saveGame(); 
        gc.nextSubstate();
      }
      
      /*@Override
      public void drawPlayPanel(Graphics2D g) { 
        g.setColor(Color.RED);
        g.fillRect(100, 100, 100, 100);
      }*/
      
    });
    this.stateQueue.add(new AnonState(this.gc, "new-display") {
      
      @Override
      public void onLoad() { 
        gc.setText("\nView your Organisms: \n"
        + "When you're ready, press the \n"
        + "Start button to begin gameplay!\n"); 
      }
      
      @Override
      public void onButtonSave() {
        this.gc.saveGame();
      }
      
      @Override
      public void onButtonStart() {
        this.gc.clearStateStack();
        this.gc.addState(new StateGameLoop(this.gc));
        this.gc.changeButtons();
      }
      
      @Override
      public void drawPlayPanel(Graphics2D g) {
        this.gc.drawOrganisms(g);
      }
      
    });
  }
   
  
  @Override
  public void onTextInput(String in) {
    this.stateQueue.peek().onTextInput(in);
  }
  
  @Override
  public void nextSubstate() {
    this.stateQueue.remove();
  }
  
  @Override
  public void onButtonStart() {
    this.stateQueue.peek().onButtonStart();
  }
  
  @Override
  public void drawPlayPanel(Graphics2D g) {
    this.stateQueue.peek().drawPlayPanel(g); 
  }
  
  @Override
  public void onButtonSave() {
    this.stateQueue.peek().onButtonSave();
  } 
  
  @Override
  public void onButtonBack() {
    this.stateQueue.peek().onButtonBack();
  }
  
  @Override
  public void onLoad() {
    this.stateQueue.peek().onLoad();
  } 

}


