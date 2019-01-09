package panic.org;

import panic.gene.Sex;
import panic.gene.Trait; 

public class OBactCyano extends Organism {   
  private static final long serialVersionUID = 1; 
  
  public OBactCyano(String name, Sex sex) {
    super(name, sex); 
  }
  
  @Override
  protected String setSpecies() {
    return "Cyanobacterium";
  } 

  @Override
  protected Trait[] setTraits() {
    return new Trait[] {
        Trait.size, Trait.bacterialglidespeed, Trait.cellwallthickness, Trait.color
    };
  } 
  
  @Override
  protected boolean speciesBreedable(Organism other) {
    // no special checking for OBactCyano
    return true;
  }  
  
  @Override
  public Organism createOffspring(String name) {
    return new OBactCyano(name, Sex.randomSex());
  }
  
}




