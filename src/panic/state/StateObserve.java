package panic.state;

import java.awt.Graphics2D;
import java.util.Optional;

import panic.game.GameController;
import panic.gene.Trait;
import panic.org.Organism;

public class StateObserve extends GameState {

  public StateObserve(GameController gc) {
    super(gc); 
  }

  @Override
  protected String setName() {
    return "Observe";
  }

  @Override
  protected void initStateStructures() {
    
  }
  
  @Override
  public void onLoad() {
    this.gc.setText("\nHere, you may observe any of your Organisms. Type an Organism name to view "
        + "its genes, or type a Trait name to see your discovery process. No actions "
        + "here cost a turn. Press the 'Back' button to return.");
    this.gc.repaint();
  } 
  
  @Override
  public void onTextInput(String in) {
    Optional<Organism> maybeObserve = this.gc.getOrgFromName(in);
    if (maybeObserve.isPresent()) {
      this.gc.setText("\nHere is Organism " + in + ":");
      System.out.println(maybeObserve.get());
    } else {
      Optional<Trait> maybeTrait = Trait.getTraitFromName(in); 
      if (maybeTrait.isPresent()) {
        Trait trait = maybeTrait.get();
        this.gc.setText("\nHere is your progress on Trait " + trait.getUserFriendlyName() + ":");
        
        String[] alleleRepresentations = this.gc.getAllelesAsStrings(trait);
        for (String s : alleleRepresentations) {
          this.gc.addText("\n" + s);
        }
      } else {
        this.gc.setText("\n" + in + " is not a valid Organism or Trait! Try again, or press the "
          + "'Back' button to return.");
      }
    }
  }
  
  @Override
  public void drawPlayPanel(Graphics2D g) {
    this.gc.drawOrganisms(g); 
  }

}
