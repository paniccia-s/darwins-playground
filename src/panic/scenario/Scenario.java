package panic.scenario;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

import panic.game.GameController;
import panic.io.DataIO;
import panic.tile.BoardTile;

public abstract class Scenario implements Serializable { 
  private static final long serialVersionUID = 1;

  /**
   * Each Scenario has an independent factor for successful
   * mutation, which is represented by mutateFactor as a
   * float between 0 and 1.
   */
  protected double mutateFactor;
  
  /**
   * The number of turns for this Scenario.
   */
  protected int turnLimit;
  
  /**
   * The x-offset of the board on the PlayPanel, in pixels
   */
  protected int xOffset;
  
  /**
   * The y-offset of the board on the PlayPanel, in pixels
   */
  protected int yOffset;
  
  /**
   * How many rows are on the board
   */
  protected int rows;
  
  /**
   * How many columns are on the board
   */
  protected int cols;
  
  /**
   * The in-game name of this Scenario
   */
  protected String name;
  
  /**
   * The list of tiles for this Scenario's board
   */
  protected ArrayList<BoardTile> board;
  
  /**
   * GameController reference. <b>do i even need this?</b>
   */
  protected transient GameController gc;
  
  protected Scenario(GameController gc, String which) {
    this.gc = gc;
    this.initFields();
    this.initBoard(which);
  }
  
  /**
   * To set all required fields for this Scenario
   */
  private void initFields() {
    this.mutateFactor = this.setMutateFactor();
    this.turnLimit = this.initTurnLimit();
    this.xOffset = this.initXOffset();
    this.yOffset = this.initYOffset();
  }
  
  /**
   * To set the mutation factor for this Scenario 
   */
  protected abstract double setMutateFactor();

  /**
   * To set the turn limit for this Scenario 
   */
  protected abstract int initTurnLimit();

  /**
   * To set the x offset for this Scenario 
   */
  protected abstract int initXOffset();

  /**
   * To set the y offset for this Scenario 
   */
  protected abstract int initYOffset(); 
  
  /**
   * To initialize the board, per Scenario
   */
  private void initBoard(String which) { 
    this.board = new ArrayList<BoardTile>();
    this.cols = DataIO.readTiles(which, this.board);
    this.rows = (this.board.size()) / this.cols;
    
    int numTiles = this.board.size();
    for (int i = 0; i < numTiles; i++) {
      BoardTile toAssign = this.board.get(i);
      int indexD = i + this.cols;
      int indexR = i + 1;
      
      if ((i + 1) % this.cols != 0) {
        BoardTile right = this.board.get(indexR);
        toAssign.setRight(right);
      }
      
      if (indexD < numTiles) {
        BoardTile below = this.board.get(indexD);
        toAssign.setBelow(below);
      }
    }
  }
  
  /**
   * To render all of this Scenario's tiles onto the PlayPanel 
   */
  public void drawTiles(Graphics2D g) {
    for (BoardTile t : this.board) {
      t.draw(g, this.xOffset, this.yOffset);
    }
  }

  public int getTurnLimit() {
    return this.turnLimit;
  }
  
  public double getMutationFactor() {
    return this.mutateFactor;
  }

  /**
   * To determine which tile was clicked, given the coordinates of the click
   * @param x
   * @param y
   * @return
   */
  public BoardTile determineTileFromClick(int x, int y) {
    // first bring from pixels to tile row/col
    x = x - this.xOffset;
    y = y - this.yOffset; 
    
    int which = BoardTile.determineTileFromClick(x, y, this.rows, this.cols);
    if (which >= 0 && which < this.board.size()) {
      return this.board.get(which); 
    } else {
      return null;
    }
  }
}





