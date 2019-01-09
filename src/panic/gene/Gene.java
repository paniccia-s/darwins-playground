package panic.gene;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import panic.game.Father;

public class Gene implements Serializable {
  protected static final long serialVersionUID = 1L;

  /**
   * The active Allele for this Gene
   */
  protected Allele currentAllele; 
  
  /**
   * The trait that this Gene represents
   */
  protected Trait trait; 

  /**
   * Sets this Gene to a random Allele of the given Trait
   */
  public Gene(Trait trait) { 
    this.trait = trait;
    this.currentAllele = trait.getRandomAllele();
  } 
  
  /**
   * To mutate this Gene to a different allele
   */
  public Allele mutate() {
    if (this.trait.getAlleleNumber() == 1) {
      return this.currentAllele;
    } else {
      Allele newAllele = this.currentAllele;
      // System.out.println(this.currentAllele);

      while (newAllele == this.currentAllele) {
        newAllele = this.trait.getRandomAllele();
      }

      this.currentAllele = newAllele;
      // System.out.println(this.currentAllele);
      return this.currentAllele;
    }
  }

  public Allele getAllele() {
    return this.currentAllele;
  } 
  
  public ArrayList<Allele> getDiscoveredAlleles() { 
    throw new UnsupportedOperationException("Unimplemented, silly goose");
  } 

  public Trait whichTrait() { 
    return this.currentAllele.whichTrait();
  }
  
  @Override
  public String toString() {
    String s = "(";
    s += this.trait.getUserFriendlyName() + ": " + 
    this.currentAllele.getStringRepresentation() + ")";
    return s;
  }
  
  public String getStringRepOfCurrentAllele(HashMap<Allele, Boolean> discovered) {
    String stringRep = "(" + this.trait.getUserFriendlyName() + ": ";
    return stringRep += (discovered.get(this.currentAllele) 
        || Father.SHOW_UNDISCOVERED_ALLELES == Father.SHOW_UNDISCOVERED_ALLELES_TRUE) 
        ? this.currentAllele.getStringRepresentation() + ")" : "???)";
    /*if (discovered.get(this.currentAllele)) {
      return this.toString();
    } else {
      return "(" + this.trait.getUserFriendlyName() + ": " + "???" + ")";
    }*/
  }

  /**
   * To set the expressed Allele
   * @param allele
   */
  public void setAllele(Allele allele) {
    this.currentAllele = allele;
  }
}


