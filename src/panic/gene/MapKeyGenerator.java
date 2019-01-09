package panic.gene;

import panic.org.Organism;
import panic.tile.BoardTile;

public class MapKeyGenerator {

  
  public static String toKey(Allele allele) {
    return allele.getStringRepresentation();
  }
  
  public static String toKey(Trait trait) {
    return trait.name();
  } 

  public static String toKey(BoardTile boardTile) {
    return boardTile.toKey();
  }
  
  public static String toKey(SurvivalTrait trait) {
    return trait.name();
  }
  
  private static String toKey(Organism org) {
    return org.toKey();
  }
  
  public static String tileAndSpeciesToKey(String tile, String species) {
    return species + tile;
  }
  
  public static String tileAndSpeciesToKey(BoardTile tile, String species) {
    return species + MapKeyGenerator.toKey(tile);
  }
  
  public static String alleleAndTraitToKey(Allele allele, Trait trait) {
    return MapKeyGenerator.toKey(trait) + MapKeyGenerator.toKey(allele);
  }
  
  public static String alleleAndTraitToKey(String allele, String trait) {
    return trait + allele;
  }

  public static String alleleAndTraitToKey(String allele, Trait currentTrait) {
    return toKey(currentTrait) + allele;
  }
  
  public static String tileAndTraitToKey(String tile, String trait) {
    return trait + tile;
  }

  public static String tileAndTraitToKey(BoardTile boardTile, SurvivalTrait trait) {
    return MapKeyGenerator.toKey(trait) + MapKeyGenerator.toKey(boardTile);
  }

  public static String tileAndTraitToKey(String tile, SurvivalTrait trait) {
    return MapKeyGenerator.tileAndTraitToKey(tile, MapKeyGenerator.toKey(trait));
  }

  public static String orgAndTraitToKey(String org, SurvivalTrait currentTrait) {
    return MapKeyGenerator.orgAndTraitToKey(org, MapKeyGenerator.toKey(currentTrait));
  }

  private static String orgAndTraitToKey(String org, String trait) {
    return trait + org;
  }

  public static String orgAndTraitToKey(Organism org, SurvivalTrait trait) {
    return MapKeyGenerator.orgAndTraitToKey(MapKeyGenerator.toKey(org), MapKeyGenerator.toKey(trait));
  }

  
}
