package panic.state;

import java.awt.Graphics2D;
import java.util.Optional;

import panic.game.GameController;
import panic.org.Organism;

public class StateMutate extends GameState {
  
  /**
   * The Organism to mutate
   */
  private Organism toMutate;
  
  /**
   * Mutation chance
   */
  double chance;
  
  public StateMutate(GameController gc) {
    super(gc); 
  }

  @Override
  protected String setName() {
    return "Mutate";
  }

  @Override
  protected void initStateStructures() {
    this.stateQueue.add(new AnonState(this.gc, "mutate-intro") {

      @Override
      public void onLoad() {
        this.gc.setText("\nLet's mutate. Enter the name of the organism you'd like to mutate:");
        this.gc.repaint();
      }

      @Override
      public void onTextInput(String in) {
        Optional<Organism> maybeMutate = this.gc.getOrgFromName(in);
        if (!maybeMutate.isPresent()) {
          this.gc.setText("\nNo Organism named " + in + " was found! Try again, or press the "
              + "'Back' button to return.");
        }
        else {
          Organism mutate = maybeMutate.get();
          if (mutate.isPlacedOnBoard()) {
            this.gc.setText(
                "\nOrganism " + in + " has been placed on the board and cannot be mutated! "
                    + "Try again, or press the 'Back' button to return.");
          }
          else {
            toMutate = mutate;
            this.gc.nextSubstate();

          }
        }
      }

      @Override
      public void onButtonBack() {
        this.gc.lastState();
      }
      
    });
    
    this.stateQueue.add(new AnonState(this.gc, "mutate-select") { 
      @Override
      public void onLoad() {
        chance = this.gc.getMutationFactor();  
        this.gc.setText("\nYou have chosen to mutate " + toMutate.getName() + "."
            + "\nFor this level, the successful mutation factor is " + chance
            + ". Are you sure you'd like to continue?"
            + "\nType 'y' for yes and anything else for no.");
      }
      
      @Override
      public void onTextInput(String in) {
        if (in.equalsIgnoreCase("y")) {
          this.gc.nextSubstate();
        } else {
          this.gc.setText("\nMutation cancelled. Press the 'Back' button to return.");
        }
      } 
      
      @Override
      public void onButtonBack() {
        this.gc.lastState();
      }
      
    });
    
    this.stateQueue.add(new AnonState(this.gc, "mutate-perform") {
      
      @Override
      public void onLoad() { 
        if (this.gc.mutateOrganism(toMutate, chance)) {
          this.gc.setText("\nMutation successful! Game saved. Press the 'Back' button to continue!");
          //this.gc.printCurrentPlayer();
          this.gc.repaint();
          this.gc.saveGame();
        } else {
          this.gc.setText("\nMutation failed! Game saved. Press the 'Back' button to continue!");
          this.gc.saveGame();
        }
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
