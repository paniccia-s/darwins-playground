package panic.tile;

import java.awt.Color;

public class OceanTile extends BoardTile { 
  private static final long serialVersionUID = -245107645569137310L;

  public OceanTile(int x, int y) {
    super(x, y); 
  } 

  @Override
  protected Color assignColorFill() {
    return new Color(0x3366ff);
  }

  @Override
  protected Color assignColorNumOrgs() {
    return new Color(0xeeeeee);
  } 
  
  @Override
  public Color assignColorGrave() {
    return new Color(0xeeeeee);
  }

  @Override
  protected String setTileName() {
    return "Ocean";
  }

}
