package panic.state;

import java.awt.Graphics2D;
import java.util.Optional;

import panic.game.GameController;
import panic.org.Organism;

public class StateBreed extends GameState { 
  
  /**
   * The first Organism to breed
   */
  private Organism first;
  
  /**
   * The second Organism to breed
   */
  private Organism second;
  
  /**
   * The name of the new Organism
   */
  private String name;

  public StateBreed(GameController gc) {
    super(gc); 
  }

  @Override
  protected String setName() {
    return "Breed";
  }

  @Override
  protected void initStateStructures() { 
    this.stateQueue.add(new AnonState(this.gc, "breed-intro") {
      
      @Override
      public void onLoad() {
        this.gc.setText("\nLet's breed your Organisms. What is the name of the "
            + "first Organism you'd like to breed?");
        this.gc.repaint();
      }
      
      @Override
      public void onTextInput(String in) { 
        Optional<Organism> maybeBreed = this.gc.getOrgFromName(in);
        if (!maybeBreed.isPresent()) {
          this.gc.setText("\nNo Organism named " + in + " was found! Try again, or press the "
              + "'Back' button to return.");
        } else {
          Organism breed = maybeBreed.get();
          if (breed.isPlacedOnBoard()) {
            this.gc
                .setText("\nOrganism " + in + " has  been placed on the board and cannot be bred! "
                    + "Try again, or press the 'Back' button to return.");
          } else {
            first = breed;
            this.gc.nextSubstate();
          }
        }
      }

      @Override
      public void onButtonBack() {
        this.gc.lastState();
      }
      
    });
    
    this.stateQueue.add(new AnonState(this.gc, "breed-next") {
      
      @Override
      public void onLoad() {
        this.gc.setText("\nAnd the second?");
      }
      
      @Override 
      public void onTextInput(String in) {
        Optional<Organism> maybeBreed = this.gc.getOrgFromName(in);
        if (!maybeBreed.isPresent()) {
          this.gc.setText("\nNo Organism named " + in + " was found! Try again, or press the "
              + "'Back' button to return.");
        }
        else { 
          Organism breed = maybeBreed.get();
          if (breed.isPlacedOnBoard()) {
            this.gc
                .setText("\nOrganism " + in + " has  been placed on the board and cannot be bred! "
                    + "Try again, or press the 'Back' button to return.");
          }
          else {
            second = breed;
            this.gc.nextSubstate();
          }
        }
      }

      @Override
      public void onButtonBack() {
        this.gc.lastState();
      }
    });
    
    this.stateQueue.add(new AnonState(this.gc, "breed-name") {
      
      @Override
      public void onLoad() {
        if (first.canBreed(second)) {
          this.gc.setText("\nWhat would you like to name your new Organism?");
        } else {
          this.gc.setText("These Organisms cannot breed! Two Organisms may breed only if they are "
              + "of the same species and of opposite sexes. Also, Organisms on the board cannot breed! "
              + "Press the 'Back' button to return.");
        }
      }
      
      @Override
      public void onTextInput(String in) {
        name = in;
        this.gc.nextSubstate();
      }

      @Override
      public void onButtonBack() {
        this.gc.lastState();
      }
      
    });
    
    this.stateQueue.add(new AnonState(this.gc, "breed-perform") {
      
      @Override
      public void onLoad() { 
        Organism offspring = Organism.breed(first, second, name); 
        this.gc.addOrganism(offspring);
        this.gc.setText("\nHere is your new Organism! Game Saved. Press the 'Back' button to return!");
        this.gc.repaint();
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
  public void nextSubstate() {
    this.stateQueue.remove();
  }
  
  @Override
  public void onButtonBack() {
    this.stateQueue.peek().onButtonBack();
  }
  
  @Override
  public void drawPlayPanel(Graphics2D g) {
    this.gc.drawOrganisms(g);
  }

}
