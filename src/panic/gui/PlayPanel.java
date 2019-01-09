package panic.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * The JPanel on which all actual game rendering occurs
 * @author Sam Paniccia
 *
 */
public class PlayPanel extends JPanel { 
  private static final long serialVersionUID = 1L;
  
  /**
   * The GUIHandler that controls this
   */
  private GUIHandler handler;
  
  /**
   * The width of this panel
   */
  private final int width = GUIHandler.RIGHT_WIDTH;
  
  /**
   * The height of this panel
   */
  private final int height = GUIHandler.RIGHT_HEIGHT;
  
  /**
   * The size of this panel
   */
  private final Dimension size = new Dimension(
      this.width, this.height);
  
  /**
   * The background color of this panel
   */
  private final Color COLOR_BACKGROUND = new Color(0x8376ff);
  
  public PlayPanel(GUIHandler g) {
    this.handler = g;
    //this.addMouseListener(this);
  }
  
  /**
   * To initialize this panel by:<br>
   * - setting its size
   */
  void init() {
    this.setPreferredSize(this.size);
  } 
  
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D G = (Graphics2D) g; 
    // draw background
    G.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    G.setColor(this.COLOR_BACKGROUND);
    G.fillRect(0, 0, this.width, this.height); 
    this.handler.drawPlayPanel(G);
  }
 
}



