package panic.state;

import java.awt.Graphics2D;
import java.util.Optional;

import panic.game.GameController;
import panic.org.Organism;
import panic.tile.BoardTile;

public class StatePlace extends GameState {
  
  /**
   * The Organism to place
   */
  Organism toPlace;

  public StatePlace(GameController gc) {
    super(gc); 
  }

  @Override
  protected String setName() {
    return "Place";
  }

  @Override
  protected void initStateStructures() {
    this.stateQueue.add(new AnonState(this.gc, "place-intro") {
      
      @Override
      public void onLoad() {
        this.gc.setText("\nLet's place an Organism into the world! Which Organism "
            + "would you like to place?");
      }
      
      @Override
      public void onTextInput(String in) {
        Optional<Organism> maybePlace = this.gc.getOrgFromName(in);
        if (!maybePlace.isPresent()) {
          this.gc.setText("\nNo Organism named " + in + " was found! Try again, or press the "
              + "'Back' button to return.");
        }
        else {
          Organism place = maybePlace.get();
          if (place.isPlacedOnBoard()) {
            this.gc
                .setText("\nOrganism " + in + " has already been placed on the board! Try again, "
                    + "or press the 'Back' button to return.");
          }
          else {
            toPlace = place;
            this.gc.nextSubstate();
          }
        }
      }
      
      @Override
      public void onButtonBack() {
        this.gc.lastState();
      }
      
    });
    
    this.stateQueue.add(new AnonState(this.gc, "place-select") {
      
      @Override
      public void onLoad() {
        this.gc.setText("\nSelect onto which tile you want to place " + toPlace.getName() + ".");
      }
      
      @Override
      public void onMouseClick(int x, int y) {
        BoardTile clicked = this.gc.determineTileFromClick(x, y); 
        if (clicked != null) {
          if (toPlace.placeOnTile(clicked)) {
            clicked.addOrganism(toPlace);
            this.gc.nextSubstate();
            this.gc.repaint();
          } else {
            this.gc.setText("\nThis Organism cannot exist on the selected terrain! "
                + "Try again, or press the 'Back' button to return.");
          }
        } else {
          this.gc.setText("\nInvalid click! Try again, or press the 'Back' button to return.");
        }
      }
      
      @Override
      public void onButtonBack() {
        this.gc.lastState();
      }
      
    });
    
    this.stateQueue.add(new AnonState(this.gc, "place-confirm") {
      
      @Override
      public void onLoad() {
        this.gc.setText("\nPlacement successful! Game saved. Press the 'Back' button to return!");
        this.gc.saveGame();
      }
      
      @Override
      public void onButtonBack() {
        this.gc.nextTurn();
      }
    });
  }

  @Override
  public void onLoad() {
    this.stateQueue.peek().onLoad();
  }
  
  @Override
  public void onTextInput(String in) {
    this.stateQueue.peek().onTextInput(in);
  }
  
  @Override
  public void onButtonBack() { 
    this.stateQueue.peek().onButtonBack();
  }
  
  @Override
  public void nextSubstate() {
    this.stateQueue.remove();
  }
  
  @Override
  public void drawPlayPanel(Graphics2D g) {
    this.gc.drawTiles(g);
  }
  
  @Override
  public void onMouseClick(int x, int y) { 
    this.stateQueue.peek().onMouseClick(x, y);
  }
  
}
