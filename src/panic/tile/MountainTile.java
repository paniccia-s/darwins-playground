package panic.tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class MountainTile extends BoardTile { 
  private static final long serialVersionUID = -220645233514411936L;

  /**
   * Snow color
   */
  private final Color COLOR_SNOW = new Color(0xfefefe);
  
  /**
   * Peak color
   */
  private final Color COLOR_PEAK = new Color(0x0a0a0a);

  public MountainTile(int col, int row) {
    super(col, row);
  }

  @Override
  protected Color assignColorFill() {
    return new Color(0xA0522D);
  }

  @Override
  protected Color assignColorNumOrgs() {
    return new Color(0xffffff);
  }
  
  @Override
  public Color assignColorGrave() {
    return new Color(0xeeeeee);
  }
  
  // Add a mountain peak
  @Override
  public void draw(Graphics2D g, int offX, int offY) {
    super.draw(g, offX, offY);
    g.setColor(this.COLOR_SNOW); 
    
    Polygon p = new Polygon(this.getPeakX(offX), this.getPeakY(offY), 8);
    g.setColor(this.COLOR_SNOW);
    g.fillPolygon(p);
    
    int dia = BoardTile.size / 10; 
    int rad = dia / 2;
    
    g.setColor(this.COLOR_PEAK);
    g.fillArc((this.x * BoardTile.size) + (BoardTile.size / 2) + offX - rad, 
        (this.y * BoardTile.size) + (BoardTile.size / 2) + offY - rad, 
        dia, dia, 0, 360); 
    
  }
  
  private int[] getPeakX(int offX) {
    int centerX = (this.x * BoardTile.size) + (BoardTile.size / 2) + offX;
    return new int[] {
        centerX, centerX + (size / 6) + 3,
        centerX + (size / 4), 
        centerX + (size / 6) + 3, centerX, 
        centerX - (size / 6) - 3, 
        centerX - (size / 4), 
        centerX - (size / 6) - 3
    };
  }
  
  private int[] getPeakY(int offY) {
    int centerY = (this.y * BoardTile.size) + (BoardTile.size / 2) + offY;
    return new int[] { 
        centerY - (size / 4), 
        centerY - (size / 6) - 3, centerY, 
        centerY + (size / 6) + 3, 
        centerY + (size / 4),
        centerY + (size / 6) + 3,
        centerY,
        centerY - (size / 6) - 3
    };
  } 

  @Override
  protected String setTileName() {
    return "Mountain";
  }

}




