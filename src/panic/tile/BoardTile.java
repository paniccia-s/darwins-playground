package panic.tile;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import panic.game.Father;
import panic.gene.MapKeyGenerator;
import panic.gene.SurvivalTrait;
import panic.io.DataIO;
import panic.org.Organism;

public abstract class BoardTile implements Serializable { 
  private static final long serialVersionUID = 636611276565246451L;

  /**
   * Board index in the x dimension 
   */
  protected int x;
  
  /**
   * Board index in the y dimension
   */
  protected int y;
  
  /**
   * Tile size, in pixels
   */
  protected static final int size = 48;
  
  /**
   * Color of tile border
   */
  protected final Color COLOR_BORDER = new Color(0xffffff);

  /**
   * Color of tile 
   */
  protected Color COLOR_FILL;
  
  /**
   * Color of number indication of how many Organisms are on this tile
   */
  protected Color COLOR_NUM_ORGS;
  
  /**
   * Color of grave, to show that an Organism died here
   */
  protected Color COLOR_GRAVE;
  
  /**
   * A list of all Organisms on this BoardTile
   */
  protected ArrayList<Organism> orgs;
  
  /**
   * The BoardTile to this BoardTile's corresponding direction on the board
   */
  protected BoardTile above, below, left, right;
  
  /**
   * A list of all neighbors
   */
  protected ArrayList<BoardTile> neighbors;
  
  /**
   * A String representation of this Tile
   */
  protected String tileName; 
  
  protected static final HashMap<String, Double> traitValues = 
      DataIO.loadTileValues(); 
  
  public BoardTile(int col, int row) { 
    this.x = row;
    this.y = col;
    this.COLOR_FILL = this.assignColorFill();
    this.COLOR_NUM_ORGS = this.assignColorNumOrgs();
    this.COLOR_GRAVE = this.assignColorGrave();
    this.neighbors = new ArrayList<BoardTile>();
    this.orgs = new ArrayList<Organism>();
    this.tileName = this.setTileName(); 
  } 
  
  /**
   * To set the name for this type of BoardTile
   * @return
   */
  protected abstract String setTileName(); 
  
  /**
   * To get the Organism-Tile-Trait weight for this Tile, the given Organism species, and the given
   * SurvivalTrait
   * @param species
   * @param trait
   * @return
   */
  public double getSurvivalTraitForOrganism(String species, SurvivalTrait trait) {
    String key = MapKeyGenerator.tileAndSpeciesToKey(this, species);
    return trait.getTileOrganismValue(key);
  }
  
  public double getTraitValue(SurvivalTrait which) {
    //String key = which + this.tileName; 
    String key = MapKeyGenerator.tileAndTraitToKey(this, which);
    return BoardTile.traitValues.get(key);
  }

  /**
   * To render this Tile on the PlayPanel
   * @param g
   */
  public void draw(Graphics2D g) {
    this.draw(g, 0, 0);
  }
  
  /**
   * To render this Tile on the PlayPanel with the given x- and y-offset
   */
  public void draw(Graphics2D g, int offX, int offY) {
    int x = this.x * BoardTile.size + offX;
    int y = this.y * BoardTile.size + offY;
    g.setColor(this.COLOR_FILL);
    g.fillRect(x, y, BoardTile.size, BoardTile.size);
    g.setColor(this.COLOR_BORDER);
    g.drawRect(x, y, BoardTile.size, BoardTile.size);
     
    this.drawOrganisms(g, x, y);
    
  }
  
  /**
   * To render the number of Organisms on this Tile
   */
  public void drawOrganisms(Graphics2D g, int x, int y) { 
    int textX = x + 5;
    int textY = y + 15;
    
    int numOrgs = (int) this.orgs.stream() 
        .filter(o -> o.isAlive())
        .count();
        
    
    if (numOrgs > 0) {
      String num = String.valueOf(numOrgs); 
      g.setColor(this.COLOR_NUM_ORGS);
      g.drawString(num, textX, textY);
    } 
    
    if (numOrgs != this.orgs.size()) {
      textY -= 10;
      textX += BoardTile.size - (textX / BoardTile.size) - 6;
      g.setColor(this.COLOR_GRAVE);
      g.fillArc(textX, textY, 6, 6, 0, 180);
      textY += 3;
      g.fillRect(textX, textY, 6, 6);
    }
  }
  
  /**
   * To return a key for this BoardTile type that may be used in HashMaps
   * @return
   */
  public String toKey() {
    return this.tileName;
  }
  
  @Override
  public String toString() {
    return "(" + this.x + "," + this.y + ")";
  } 
  
  /**
   * To assign the inner color of this Tile
   * @return
   */
  protected abstract Color assignColorFill();
  
  /**
   * To assign the color of the text representing the number of Organisms on this Tile
   * @return
   */
  protected abstract Color assignColorNumOrgs();
  
  /**
   * To assign the color of the grave symbol
   */
  protected abstract Color assignColorGrave();

  /**
   * To determine which tile was clicked, given the coordinates of the click
   * @param x
   * @param y
   * @param rows 
   * @param cols 
   * @return
   */
  public static int determineTileFromClick(int x, int y,
      int rows, int cols) {
    
    // check that click was within board domain
    if (x < 0 || y < 0) { 
      return -1;
    }
    
    if (x > (rows * BoardTile.size) || y > (cols * BoardTile.size)) { 
      return -1;
    }
    
    //in valid space
    int whichX = x / BoardTile.size;
    int whichY = y / BoardTile.size; 
    int which = (whichY * rows) + whichX;  
    return which;
  }
  
  /**
   * To add an Organism to this BoardTile
   */
  public void addOrganism(Organism toAdd) {
    this.orgs.add(toAdd);
  }

  public String[] getOrgNamesOnTile() {
    return this.orgs.stream()
        .filter(o -> o.isAlive())
        .map(o -> o.getName())
        .toArray(s -> new String[s]);
  }

  public void setRight(BoardTile right) {
    this.right = right;
    this.neighbors.add(right);
    right.left = this;
    right.neighbors.add(this);
  }
  
  public void setBelow(BoardTile below) {
    this.below = below;
    this.neighbors.add(below);
    below.above = this;
    below.neighbors.add(this);
  }
  
  public BoardTile moveOrganism(Organism toMove, String species) {
    Father.log("\tOrganism is currently on Tile " + this.toString() + ".");
    BoardTile destination = null;
    double mobility = -2;
    // continue while it is impossible to do so
    while (mobility < -1) {
      int rand = Father.randInt(0, this.neighbors.size()); 
      destination = this.neighbors.get(rand);
      mobility = destination.getSurvivalTraitForOrganism(species, SurvivalTrait.mobility); 
    }
    
    destination.addOrganism(toMove);
    this.orgs.remove(toMove);
    Father.log("\tOrganism moved to " + destination.toString());
    return destination;
  }
}






