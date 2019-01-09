package panic.state;

import java.awt.Graphics2D;

import panic.game.GameController;
import panic.tile.BoardTile;

public class StateGameLoop extends GameState {

  public StateGameLoop(GameController gc) {
    super(gc); 
  }

  @Override
  protected String setName() {
    return "Game Loop";
  }

  @Override
  protected void initStateStructures() {
    
  }
  
  @Override
  public void onButtonBack() {
     this.onLoad();
  }

  @Override
  public void onLoad() {
    this.gc.showGameDataOnTurnStart();
    this.gc.addText("\nClick on a Tile to view the Organisms on it, if there are any.");
    this.gc.repaint();
  }
  
  @Override
  public void drawPlayPanel(Graphics2D g) {
    this.gc.drawTiles(g);
  }
  
  @Override
  public void onButtonMutate() {
    this.gc.addState(new StateMutate(this.gc));
  }
  
  @Override
  public void onButtonBreed() {
    this.gc.addState(new StateBreed(this.gc));
  }
  
  @Override
  public void onButtonPlace() {
    this.gc.addState(new StatePlace(this.gc));
  }
  
  @Override
  public void onButtonObserve() {
    this.gc.addState(new StateObserve(this.gc));
  }
  
  @Override
  public void onButtonEngineer() {
    this.gc.addState(new StateEngineer(this.gc));
  } 
  
  @Override
  public void onMouseClick(int x, int y) {
    BoardTile clicked = this.gc.determineTileFromClick(x, y);
    if (clicked != null) {
      String[] onTile = clicked.getOrgNamesOnTile();
      if (onTile.length != 0) { 
        this.gc.setText("\nAll Organisms on the clicked tile:");
        for (String o : onTile) {
          this.gc.addText("\n" + o);  
        }
      } else {
        this.gc.setText("\nNo Organisms on this tile.");
      }
    }
  }
  
}
