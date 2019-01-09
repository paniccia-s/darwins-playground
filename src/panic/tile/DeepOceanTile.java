package panic.tile;

import java.awt.Color;

public class DeepOceanTile extends BoardTile {  
  private static final long serialVersionUID = -1438260943346091324L;

  public DeepOceanTile(int col, int row) {
    super(col, row); 
  } 

  @Override
  protected Color assignColorFill() {
    return new Color(0x004fce);
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
    return "Deep Ocean";
  }

}
