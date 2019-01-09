  package panic.gene;

import java.util.HashMap;
import java.util.Optional;

public enum SurvivalTrait { 
  
  predation, 
  appetite, 
  adaptability, 
  mobility; 
  
  protected HashMap<String, Double> organismTileTraitValues = 
      new HashMap<String, Double>();

  protected HashMap<String, Double> alleleMap; 
  
  public static Optional<SurvivalTrait> getTraitFromName(String name) {
    for (SurvivalTrait t : SurvivalTrait.values()) {
      if (t.name().equalsIgnoreCase(name)) { 
        return Optional.of(t); 
      }
    }
    return Optional.empty();
  }
  
  public void setAlleleMap(HashMap<String, Double> map) {
    this.alleleMap = map;
  }
  
  public void putTileOrganismValue(String key, double value) {
     this.organismTileTraitValues.put(key, value);
  }
  
  public void setTileOrganismMap(HashMap<String, Double> map) {
    this.organismTileTraitValues = map;
  }
  
  public double getTileOrganismValue(String key) {
    Double d = this.organismTileTraitValues.get(key);
    
    if (d == null) {
      throw new IllegalArgumentException("Illegal species or tile name: " + key);
    }
    
    return d;
  } 
  
  public double getAlleleValue(String key) { 
    if (alleleMap == null) {
      throw new IllegalArgumentException("Allele map is null!");
    }
    return this.alleleMap.get(key);
  }
}


