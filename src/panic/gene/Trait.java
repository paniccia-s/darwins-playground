package panic.gene;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public enum Trait {

  size(Allele.AlleleSize.values(), "Size"), 
  bacterialglidespeed(Allele.AlleleBacterialGlideSpeed.values(), "Glide Speed"), 
  cellwallthickness(Allele.AlleleCellWallThickness.values(), "Cell Wall Thickness"), 
  color(Allele.AlleleColor.values(), "Color");
  
  /**
   * All of the possible Alleles for this Trait
   */
  private Allele[] allAlleles; 
  
  /**
   * A user-friendly name for this Trait
   */
  private String userFriendlyName;
  
  private Trait(Allele[] allAlleles, String userFriendlyName) {
    this.allAlleles = allAlleles; 
    this.userFriendlyName = userFriendlyName;
  } 
  
  /**
   * To get the desired Trait from its name
   * @param name
   * @return
   */
  public static Optional<Trait> getTraitFromName(String name) {
    return Stream.of(Trait.values())
        .filter(t -> t.name().equalsIgnoreCase(name)
            || t.userFriendlyName.equalsIgnoreCase(name)) 
        .findFirst(); 
  }

  /**
   * To get a random Allele of this Trait
   * @return
   */
  public Allele getRandomAllele() {
    int rand = new Random().nextInt(this.allAlleles.length);
    return this.allAlleles[rand];
  } 
  
  /**
   * To get all Alleles for this Trait
   */
  public Allele[] getAlleles() {
    return this.allAlleles;
  }
  
  /**
   * To get the user-friendly name for this Trait
   */
  public String getUserFriendlyName() {
    return this.userFriendlyName;
  }

  public int getAlleleNumber() {
    return this.allAlleles.length;
  }
  
}
