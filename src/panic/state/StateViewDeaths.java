package panic.state;

import java.awt.Graphics2D;

import panic.game.GameController;

public class StateViewDeaths extends GameState {

  /**
   * All death messages to render
   * @param gc
   */
  private String[] deathMessages;
  
  public StateViewDeaths(GameController gc) {
    super(gc);
    // TODO Auto-generated constructor stub
  }

  @Override
  protected String setName() {
    return "View Deaths";
  }
  
  public StateViewDeaths acceptDeathMessages(String[] messages) {
    this.deathMessages = messages;
    return this;
  }

  @Override
  protected void initStateStructures() {
  
  }
  
  @Override
  public void onLoad() {
    this.gc.setText("\nDEATH!\n"
        + "One or more of your Organisms on the board has died!\n"
        + "View your dead Organisms and press the 'BACK' button to return. This does not "
        + "take an additional turn.");
  }

  @Override
  public void drawPlayPanel(Graphics2D g) {
    this.gc.renderTextOnPlayPanel(g, this.deathMessages);
  } 
  
}
