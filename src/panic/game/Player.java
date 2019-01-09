package panic.game;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Optional;

import panic.gene.Allele;
import panic.gene.Trait;
import panic.org.Organism;
import panic.scenario.Scenario;
import panic.scenario.ScenarioTutorial;
import panic.tile.BoardTile;

public class Player implements Serializable {  
  private static final long serialVersionUID = 1L;

  /**
   * The player's name
   */
  private final String name;
  
  /**
   * A list of all owned Organisms
   */
  private ArrayList<Organism> orgs; 
  
  /**
   * The turn that the player is on
   */
  private int currentTurn;
  
  /**
   * The currently-used Scenario
   */
  private Scenario currentScenario;
  
  /**
   * All Alleles and whether or not the player has discovered them
   */
  private HashMap<Allele, Boolean> discovered;

  public Player(String name, GameController gc) { 
    this.currentTurn = 1;
    this.name = name;
    this.orgs = new ArrayList<Organism>(); 
    this.currentScenario = new ScenarioTutorial(gc);
    this.discovered = new LinkedHashMap<Allele, Boolean>();
    this.initDiscovered(); 
  } 
  
  /**
   * To initialize this.discovered to contain every Allele paired with false
   */
  private void initDiscovered() {
    for (Trait t : Trait.values()) {
      Allele[] alleles = t.getAlleles();
      if (alleles.length == 1) {
        this.discovered.put(alleles[0], true);
      }
      else {
        for (Allele a : alleles) {
          this.discovered.put(a, false);
        }
      }
    }
  }
  
  // getters and setters
  
  /**
   * To retrieve this Player's name
   */
  public String getName() {
    return this.name;
  }
  
  /**
   * To retrieve this Player's current turn
   */
  public int getCurrentTurn() {
    return this.currentTurn;
  }

  /**
   * To return the Organism of the given name
   */
  public Optional<Organism> getOrgFromName(String name) { 
    return this.orgs.stream()
        .filter(o -> o.getName().equals(name) && o.isAlive())
        .findFirst();
  }
  
  /**
   * To change to the next turn
   */
  public String[] nextTurn() {
    this.currentTurn++;
    return this.orgs.stream()
        .map(o -> o.nextTurn())
        .filter(o -> o.isPresent())
        .map(o -> o.get())
        .toArray(s -> new String[s]);  
  }


  
  /**
   * To retrieve a list of all valid Organism names for this Player
   */
  public String[] getValidOrganismNames() { 
    return this.orgs.stream()
        .map(o -> o.getName()) 
        .toArray(String[]::new);
  }

  /**
   * To add the given Organism to this Player's list of Organisms
   * @param toAdd
   */
  public void addOrganism(Organism toAdd) {
    this.orgs.add(toAdd);
  }
  /**
   * To return the mutation factor of this player's Scenario 
   */
  public double getMutationFactor() {
    return this.currentScenario.getMutationFactor();
  }
  
  // Scenario delegation
  
  /**
   * To retrieve the turn limit for this Player's current Scenario
   */
  public int getTurnLimit() {
    return this.currentScenario.getTurnLimit();
  }
  
  /**
   * To render all game tiles onto the PlayPanel 
   */
  public void drawTiles(Graphics2D g) {
    this.currentScenario.drawTiles(g);
  }
  
  /**
   * To render all Organisms (textually) onto the PlayPanel
   */
  public void drawOrganisms(Graphics2D g) {  
    
    int xPos = 50; 
    int yPos = 50;  
    for (Organism o : this.orgs) {
      if (o.isAlive()) {
        yPos += o.draw(g, xPos, yPos, this.discovered);
        Father.log(o.toString()); 
      }
    }
    
  }
  
  
  /**
   * To determine which tile was clicked, given the coordinates of the click
   * @param x
   * @param y
   * @return
   */
  public BoardTile determineTileFromClick(int x, int y) {
    return this.currentScenario.determineTileFromClick(x, y);
  }
  
  
  // Object overrides
  
  @Override
  public String toString() {
    String s = "PLAYER " + this.name + ":\n";
    for (Organism o : this.orgs) {
      s += "    " + o.toString();
    } 
    return s;
  }

  /**
   * To mutate the given Organism and to record that the user has discovered the 
   * new Allele
   * @param toMutate
   * @param chance
   * @return
   */
  public boolean mutateOrganism(Organism toMutate, double chance) { 
    Optional<Allele> maybeMutated = toMutate.mutate(chance);
    if (maybeMutated.isPresent()) {
      Allele mutated = maybeMutated.get();
      this.discovered.put(mutated, true);
      return true;
    }
    return false;
  } 
  
  /**
   * To return a list of Strings representing the Player's knowledge of each Allele
   * in this.discovered (returns the allele's name if the player has discovered it, or
   * question marks if the player has not.
   * @param which
   * @return
   */
  public String[] getStringRepOfDiscoveredAlleles(Trait which) { 
    return this.discovered.entrySet().stream()
        .filter(e -> e.getKey().whichTrait().equals(which))
        //.sorted((a, b) -> a.getKey().position() - b.getKey().position())
        .map((Entry<Allele, Boolean> e) -> this.getStringRepOfAllele(e.getKey()))  
        .toArray(String[]::new);
  }
  
  /**
   * To retrieve the String representation of the knowledge of the given Allele
   */
  public String getStringRepOfAllele(Allele which) {
    if (Father.SHOW_UNDISCOVERED_ALLELES == Father.SHOW_UNDISCOVERED_ALLELES_TRUE) { 
      return which.getStringRepresentation();
    } else {
      return this.discovered.get(which) ? which.toString() : "???"; 
    }
  } 

  public Optional<Allele> getAlleleFromNameIfDiscovered(String name) {
    return this.discovered.entrySet().stream()
        .filter(e -> e.getKey().getStringRepresentation().equals(name))
        .filter(e -> e.getValue())
        .map(e -> e.getKey())
        .findFirst();
  }
}


