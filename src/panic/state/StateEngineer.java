package panic.state;

import java.awt.Graphics2D;
import java.util.Optional;

import panic.game.GameController;
import panic.gene.Allele;
import panic.gene.Trait;
import panic.org.Organism;

public class StateEngineer extends GameState {

  private Organism toEngineer;
  
  private Trait traitToEngineer;
  
  public StateEngineer(GameController gc) {
    super(gc); 
  }

  @Override
  protected String setName() {
    return "Engineer";
  }

  @Override
  protected void initStateStructures() {
    this.stateQueue.add(new AnonState(this.gc, "engineer-intro") {
      
      @Override
      public void onLoad() {
        this.gc.setText("\nLet's engineer one of your Organisms. Which would you like to engineer?");
        this.gc.repaint();
      }
      
      @Override
      public void onTextInput(String in) {
        Optional<Organism> maybeEngineer = this.gc.getOrgFromName(in);
        if (!maybeEngineer.isPresent()) {
          this.gc.setText("\nNo Organism named " + in + " was found! Try again, or press the "
              + "'Back' button to return.");
        }
        else {
          Organism engineer = maybeEngineer.get();
          if (engineer.isPlacedOnBoard()) {
            this.gc.setText(
                "\nOrganism " + in + " has been placed on the board and cannot be engineered! "
                    + "Try again, or press the 'Back' button to return.");
          }
          else {
            toEngineer = engineer;
            this.gc.nextSubstate();

          }
        }
      }
      
    });
    
    this.stateQueue.add(new AnonState(this.gc, "engineer-choose-trait") {

      @Override
      public void onLoad() {
        this.gc.setText("\nAnd which Trait would you like to engineer?");
      }
      
      @Override
      public void onTextInput(String in) {
        Optional<Trait> maybeTrait = Trait.getTraitFromName(in); 
        if (maybeTrait.isPresent()) {
          Trait trait = maybeTrait.get(); 
          traitToEngineer = trait; 
          this.gc.nextSubstate();
        } else {
          this.gc.setText("\n" + in + " is not a valid Trait! Try again, or press the "
            + "'Back' button to return.");
        }
      }
      
    });
    
    this.stateQueue.add(new AnonState(this.gc, "engineer-pick-allele") {
      
      @Override
      public void onLoad() {
        this.gc.setText("\nChoose from one of the following discovered alleles:");
        String[] alleles = this.gc.getAllelesAsStrings(traitToEngineer);
        for (String allele : alleles) {
          this.gc.addText("\n" + allele);
        }
      }
      
      @Override
      public void onTextInput(String in) {
        Optional<Allele> maybeAllele = this.gc.getAlleleFromName(in);
        if (maybeAllele.isPresent()) {
          Allele allele = maybeAllele.get();
          toEngineer.setGene(traitToEngineer, allele);
          this.gc.setText("\nEngineering complete! View your engineered Organism. Press the 'Back' button to "
              + "return!");
          this.gc.repaint();
        } else {
          this.gc.setText("\n" + in + " is not a valid Allele! Try again, or press the 'Back' "
              + "button to return.");
        } 
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
  public void drawPlayPanel(Graphics2D g) {
    this.gc.drawOrganisms(g);
  }
  
  @Override
  public void nextSubstate() {
    this.stateQueue.remove(); 
  }
  
}
