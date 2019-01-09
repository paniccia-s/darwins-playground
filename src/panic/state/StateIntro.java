package panic.state;

import panic.game.Father;
import panic.game.GameController;

public class StateIntro extends GameState {
 
  public StateIntro(GameController gc) {
    super(gc); 
  }
  
  @Override
  protected String setName() {
    return "Intro";
  }
  
  @Override
  protected void initStateStructures() {
    // do nothing
  }
  
  // Important button clicks: 
  @Override
  public void onButtonNew() {
    Father.log("State transition: Intro to New");
    this.gc.addState(new StateNew(this.gc)); 
  }  
  
  @Override
  public void onButtonLoad() {
    Father.log("State transition: Intro to Load");
    this.gc.addState(new StateLoad(this.gc));
  }
  
  @Override
  public void onLoad() {
    this.gc.setText("\nWelcome to Darwin's Playground!\n"
        + "Press a button below to start your \ngame!\n\n"
        + "If you have a saved game, press 'Load'.\n"
        + "Otherwise, press 'New' to start.");
    this.gc.repaint(); 
  }
  
  @Override
  public void onButtonBack() {
    // do nothing
  } 
}







