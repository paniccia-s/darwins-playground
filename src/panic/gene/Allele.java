package panic.gene;
 
public interface Allele { 
   
  /**
   * To get the trait that this Allele corresponds to
   * @return The Trait enum value to which this Allele belongs 
   */
  public Trait whichTrait(); 

  public enum AlleleColor implements Allele {
    red, yellow, green, blue;

    @Override
    public Trait whichTrait() {
      return Trait.color;
    }
    
  }
  
  public default String getStringRepresentation() {
    return this.toString().replaceAll("_", " ");
  }
  
  public enum AlleleSize implements Allele {
    very_small, small, medium, large, very_large; 

    @Override
    public Trait whichTrait() {
      return Trait.size;
    } 

  }
  

  public enum AlleleBacterialGlideSpeed implements Allele {
    very_slow, slow, medium, fast, very_fast; 
  
    @Override
    public Trait whichTrait() {
      return Trait.bacterialglidespeed;
    }  
  }
  

  public enum AlleleCellWallThickness implements Allele {
    thin, medium, thick;
 
    @Override
    public Trait whichTrait() {
      return Trait.cellwallthickness;
    }  
  } 
}
