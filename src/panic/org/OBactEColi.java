package panic.org;

import panic.gene.Sex;
import panic.gene.Trait;

public class OBactEColi extends Organism { 
  private static final long serialVersionUID = 1L;

  protected OBactEColi(String name, Sex sex) {
    super(name, sex); 
  }

  @Override
  protected String setSpecies() {
    return "E.Coli";
  }

  @Override
  protected Trait[] setTraits() {
    return new Trait[] {
        Trait.size, Trait.bacterialglidespeed, Trait.cellwallthickness
    };
  }

  @Override
  protected boolean speciesBreedable(Organism other) {
    // no special checking for e.coli
    return true;
  }

  @Override
  public Organism createOffspring(String name) {
    return new OBactEColi(name, Sex.randomSex());
  }

}
