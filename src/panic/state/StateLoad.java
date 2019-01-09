package panic.state;

import java.awt.Graphics2D;

import panic.game.GameController;

public class StateLoad extends GameState {

  private String playerName;
  
  public StateLoad(GameController gc) {
    super(gc); 
  }

  @Override
  protected String setName() {
    return "Load";
  }

  @Override
  protected void initStateStructures() {  
    stateQueue.add(new AnonState(gc, "load-prompt") {
      
      @Override
      public void onLoad() { 
        this.gc.setText("\nEnter below your player name.");
      }
      
      @Override
      public void onTextInput(String name) { 
        if (this.gc.loadPlayer(name)) {
          playerName = name;
          gc.nextSubstate();
        } else { 
          this.gc.setText("\nThere is no saved file for "
              + name + ".");
        }
      }
    });
    
    stateQueue.add(new AnonState(gc, "load-show-player") { 
      
      @Override
      public void onLoad() {
        this.gc.setText("\nWelcome back, " + playerName + "!");
        this.gc.repaint(); 
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
  public void nextSubstate() {
    this.stateQueue.remove();
  }
  
  @Override
  public void drawPlayPanel(Graphics2D g) {
    this.stateQueue.peek().drawPlayPanel(g);
  }
  
  @Override
  public void onTextInput(String in) {
    this.stateQueue.peek().onTextInput(in);
  }
  
  @Override
  public void onLoad() {
    this.stateQueue.peek().onLoad();
  }

  @Override
  public void onButtonBack() {
    this.gc.lastState();
  }
  
  @Override
  public void onButtonStart() {
    this.stateQueue.peek().onButtonStart();
  }
  
}



