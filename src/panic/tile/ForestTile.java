package panic.tile;

import java.awt.Color;

public class ForestTile extends BoardTile { 
  private static final long serialVersionUID = -8761094519834792038L;
   

  public ForestTile(int x, int y) {
    super(x, y);
  } 

  @Override
  protected Color assignColorFill() {
    return new Color(0x149100);
  }

  @Override
  protected Color assignColorNumOrgs() {
    return new Color(0xefeeff);
  }
 
  @Override
  public Color assignColorGrave() {
    return new Color(0xcccccc);
  }
  
  @Override
  protected String setTileName() {
    return "Forest";
  }  

}
