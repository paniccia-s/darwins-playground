package panic.scenario;

import panic.game.GameController;

public class ScenarioTutorial extends Scenario { 
  private static final long serialVersionUID = 1;

  public ScenarioTutorial(GameController gc) {
    super(gc, "tutorial"); 
  }

  @Override
  protected double setMutateFactor() {
    return 1;
  }

  @Override
  protected int initTurnLimit() {
    return 50;
  }

  @Override
  protected int initXOffset() {
    return 100;
  }

  @Override
  protected int initYOffset() {
    return 100;
  } 

}
