package panic.state;

import panic.game.GameController;

/**
 * Represents a substate within a GameState instance. Meant to be created 
 * anonymously.
 * @author Sam Paniccia
 *
 */
public class AnonState extends GameState {

  public AnonState(GameController gc, String name) {
    super(gc); 
    this.stateName = name;
  }

  @Override
  protected String setName() {
    return "ANON: " + this.stateName;
  }

  @Override
  protected void initStateStructures() {
  }

}
