package panic.org;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import panic.game.Father;
import panic.gene.Allele;
import panic.gene.MapKeyGenerator;
import panic.gene.Sex;
import panic.gene.SurvivalTrait;
import panic.gene.Trait;
import panic.io.DataIO;
import panic.tile.BoardTile;

public abstract class Organism implements Serializable {
  private static final long serialVersionUID = -4844966823966336914L;

  /**
   * This Organism's pair of Chromosomes
   */
  protected Chromosome chromosome; 
  
  /**
   * The name of this Organism
   */
  private String name; 
  
  /**
   * The String representation of the type of Organism this is
   */
  private String species;
  
  /**
   * The sex of this Organism. Sex is simplified to either male or female
   */
  protected Sex sex;
  
  /**
   * The number of Genes in *each* of this Organism's Chromosomes
   */
  protected int numGenes;
  
  /**
   * The tile on which this Organism has been placed, or null if it is not
   * on the board
   */
  protected BoardTile tile;
  
  /**
   * Whether this Organism has been placed on the board. Should align with 
   * (this.tile != null)
   */
  protected boolean hasBeenPlaced;
  
  /**
   * The current life state of this Organism
   */
  protected OrganismLifeState lifeState; 
   
  public static final Font FONT_ORGANISM_TEXT = new Font("Arial", Font.PLAIN, 30);
  private final int DRAW_Y_OFFSET = 30; 
   
  
  protected static final HashMap<String, Double> orgValues = 
      DataIO.loadOrgValues();
  
  /**
   * Each subclass of Organism must take a list of Traits, which
   * represent the types of Genes in the Organism. It also takes a name and a sex.
   */
  protected Organism(String name, Sex sex) {
    this.name = name;
    this.species = this.setSpecies(); 
    Trait[] traits = this.setTraits();
    this.numGenes = traits.length;
    if (numGenes == 0) {
      throw new IllegalArgumentException("At least one trait must be supplied to an Organism.");
    }
    this.chromosome = new Chromosome(traits); 
    this.lifeState = OrganismLifeState.alive;
    this.sex = sex;
  }  
  
  protected abstract String setSpecies();
  
  protected abstract Trait[] setTraits();
  
  /**
   * To get this Organism's name
   */
  public String getName() {
    return this.name;
  }

  /**
   * To mutate the given Organism according to the given mutation chance
   * @param toMutate
   * @param chance The mutation chance, between 0 and 1
   * @return
   */
  public Optional<Allele> mutate(double scenarioChance) { 
    return this.chromosome.mutate(scenarioChance);
  }

  /**
   * To determine if this Organism can breed with the given Organism
   * @param other
   * @return
   */
  public boolean canBreed(Organism other) {
    // neither may be placed, both must be of the same species, both must be different sexes, 
    // and they must pass their specific species standards
    return !this.isPlacedOnBoard() 
        && !other.isPlacedOnBoard() 
        && this.sameSpecies(other)
        && this.sex != other.sex
        && this.speciesBreedable(other);
  }

  /**
   * To determine species-specific breeding standards that may not apply to other Organisms.<br>
   * Casting is okay here because the species have already been confirmed to be the same through the
   * sameSpecies() call in canBreed().
   * @param other
   * @return
   */
  protected abstract boolean speciesBreedable(Organism other);
  
  private boolean sameSpecies(Organism other) {
    return this.species.equals(other.species);
  }
  
  /**
   * To create a new Organism of this Organism's species and of a random sex
   * @param name
   * @return
   */
  public abstract Organism createOffspring(String name);
  
  /**
   * To create offspring from two given Organisms with randomly assigned
   * traits from its parents
   * @param one A parent Organism
   * @param two The other parent Organism
   * @param name The name of this new Organism
   * @return The offspring
   */
  public static Organism breed(Organism one, Organism two, String name) {
    Organism offspring = one.createOffspring(name); 
    
    Trait[] ids = one.chromosome.getAllTraitsInChromosome();
    
    for (int i = 0; i < one.numGenes; i++) {
      Trait currentTrait = ids[i]; 
      Allele oneAllele = one.getAlleleAt(currentTrait);
      Allele twoAllele = two.getAlleleAt(currentTrait); 
      
      boolean shouldTakeFromFirst = new Random().nextBoolean();
      
      if (shouldTakeFromFirst) {
        offspring.setGene(currentTrait, oneAllele); 
      } else {
        offspring.setGene(currentTrait, twoAllele); 
      }
    } 
    
    return offspring; 
  }
  
  /**
   * To set the given Trait to the given Gene
   */
  public void setGene(Trait which, Allele allele) {
    this.chromosome.setGene(which, allele);
  } 

  /**
   * To get the Allele at the given index on each Chromosome
   * @param index
   * @return
   */
  private Allele getAlleleAt(Trait which) { 
    return this.chromosome.getAlleleAt(which);  
  }

  /**
   * To place this Organism onto the given BoardTile
   * @param tile
   */
  public boolean placeOnTile(BoardTile tile) {
    double canPlace = tile.getSurvivalTraitForOrganism(this.species, SurvivalTrait.mobility);
    if (canPlace > -1) {
      this.tile = tile;
      this.hasBeenPlaced = true; 
      return true;
    }
    return false;
  }
   
  
  /**
   * To determine if this Organism has been placed on the board
   */
  public boolean isPlacedOnBoard() {
    return this.hasBeenPlaced;
  }

  
  @Override
  public String toString() {
    String sex = this.sex.name();
    String s = "Organism " + this.name
        + ": " + sex + " " + this.species + "\n";
    String c1 = this.chromosome.toString(); 
    s += "        " + "Contains: " + c1 + ".\n"; 
    return s;
  }

  /**
   * To return a key for this Organism species that may be used in HashMaps
   * @return
   */
  public String toKey() {
    return this.species;
  }
  
  /**
   * To render this Organism on the PlayPanel
   * @param g
   * @return 
   */
  public int draw(Graphics2D g, int xPos, int yPos, HashMap<Allele, Boolean> discovered) {
    String title = "Organism " + this.name + ": "
        + this.sex + " " + this.species;
    g.setColor(Color.WHITE);
    g.setFont(Organism.FONT_ORGANISM_TEXT);
    g.drawString(title, xPos, yPos); 
    return this.DRAW_Y_OFFSET + this.chromosome.draw(g, xPos, yPos + this.DRAW_Y_OFFSET, discovered);
  }
  
  /**
   * To return the double associated with the given SurvivalTrait in this.orgVals
   */
  private double getTraitValue(SurvivalTrait trait) {
    String key = MapKeyGenerator.orgAndTraitToKey(this, trait);
    return Organism.orgValues.get(key); 
  }
  
  /**
   * To determine whether or not to move this Organism
   */
  private boolean shouldMove() {
    
    // dead Organisms should not move
    if (this.lifeState == OrganismLifeState.dead) {
      return false;
    }
    // [-1, 1]
    double geneticMobility = this.chromosome.getSurvivalTraitValue(SurvivalTrait.mobility);
    // [-1, 1]
    double tileMobility = this.tile.getTraitValue(SurvivalTrait.mobility);
    // [-1, 1]
    double organismTileMobility = this.tile.getSurvivalTraitForOrganism(this.species, SurvivalTrait.mobility);
    // [-1, 1]
    double organismMobility = this.getTraitValue(SurvivalTrait.mobility);
    // centered at 0, 70% of values between [-1, 1]... fine for now
    double randomMobility = new Random().nextGaussian();
    
    double totalMobility = geneticMobility 
        + tileMobility 
        + organismTileMobility
        + organismMobility
        + randomMobility
        ;

    Father.log("Moving...");
    Father.log("Genetic Mobility: " + geneticMobility + ". Tile Mobility: " + tileMobility + ".");
    Father.log("Organism-Tile Mobility: " + organismTileMobility + ". Organism Mobility: " + organismMobility + ".");
    Father.log("Random Mobility: " + randomMobility);
    Father.log("\tSummed Mobility: " + totalMobility + ". "); 
    return totalMobility >= 0;
  }

  
  /**
   * To determine if this Organism has been slain
   */
  private boolean beenSlain() {
    // [-1,1]
    double geneticPredation  = this.chromosome.getSurvivalTraitValue(SurvivalTrait.predation);
    // [-1,1]
    double tilePredation = this.tile.getTraitValue(SurvivalTrait.predation);
    // [-1,1]
    double organismTilePredation = this.tile.getSurvivalTraitForOrganism(this.species, SurvivalTrait.predation);
    // [-1, 1]
    double organismPredation = this.getTraitValue(SurvivalTrait.predation);
    
    double totalPredation = geneticPredation + tilePredation + organismTilePredation + organismPredation; 
    
    
    Father.log("Determining predation...");
    Father.log("Genetic Predation: " + geneticPredation + ". Tile Predation: " + tilePredation + ".");
    Father.log("Organism-Tile Predation: " + organismTilePredation  + ". Organism Predation: " + organismPredation + ".");
    Father.log("\tSummed Predation: " + totalPredation + ". "); 
    
    // should die if less than zero
    return totalPredation < 0;
  }
  
  /**
   * To determine if this Organism has starved to death
   */
  private boolean starved() {
    // [-1,1]
    double geneticAppetite  = this.chromosome.getSurvivalTraitValue(SurvivalTrait.appetite);
    // [-1,1]
    double tileAppetite = this.tile.getTraitValue(SurvivalTrait.appetite);
    // [-1,1]
    double organismTileAppetite = 
        this.tile.getSurvivalTraitForOrganism(this.species, SurvivalTrait.appetite);
    // [-1, 1]
    double organismAppetite = this.getTraitValue(SurvivalTrait.appetite);
    
    double totalAppetite = geneticAppetite + tileAppetite + organismTileAppetite + organismAppetite; 
    
    Father.log("Determining appetite...");
    Father.log("Genetic Appetite: " + geneticAppetite + ". Tile Appetite: " + tileAppetite + ".");
    Father.log("Organism-Tile Appetite: " + organismTileAppetite + ". Organism Appetite: " + organismAppetite + ".");
    Father.log("\tSummed Appetite: " + totalAppetite + ". "); 
    
    return totalAppetite < 0;
  }
  
  /**
   * To move this Organism to a bordering Tile
   */
  private void maybeMove() { 
    if (this.shouldMove()) {
      Father.log("Moving succeeded! Will now move to valid adjacent Tile.");
      this.tile = this.tile.moveOrganism(this, this.species);
    } else {
      Father.log("Moving failed!");
    }
  }
  
  /**
   * To kill this Organism if it has been slain or if it starved
   */
  private Optional<String> maybeKill() {
    if (this.beenSlain()) {
      return this.die(SurvivalTrait.predation); 
    } else if (this.starved()) {
      return this.die(SurvivalTrait.appetite); 
    }
    
    return Optional.empty();
  } 

  private Optional<String> die(SurvivalTrait cause) {
    this.lifeState = OrganismLifeState.dead;
    String message = "\nOrganism " + this.name + ", a " + this.sex + " " + this.species + ",";
    switch (cause) { 
    case predation:
      message += " has been SLAIN!";
      break;
    case appetite:
      message += " has STARVED to death!"; 
      break;
    default:
      throw new IllegalArgumentException("Illegal cause of death: " + cause.toString());
    }  
    message += "\nIt was on Tile " + this.tile.toString() + 
        " (a " + this.tile.toKey() + " tile).\n";
    return Optional.of(message);
  }
  
  /**
   * To move this Organism and to kill it, if it should die
   */
  public Optional<String> nextTurn() {
    if (this.hasBeenPlaced) {
      Optional<String> result = this.maybeKill();
      this.maybeMove(); 
      return result;
    } else {
      return Optional.empty();
    }
  }


  public boolean isAlive() {
    return this.lifeState == OrganismLifeState.alive;
  }


}
