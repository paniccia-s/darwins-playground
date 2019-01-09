package panic.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import panic.game.Father;
import panic.game.GameController;
import panic.org.Organism;

/**
 * A Singleton to control all GUI-related activity.
 * @author Sam Paniccia
 *
 */
public class GUIHandler {
  
  
  /**
   * The title of the frame
   */
  private String MAINFRAME_TITLE;

  /**
   * The singleton instance to return
   */
  private static GUIHandler instance;

  /**
   * The GameController instance that controls this
   */
  private GameController gc;
  
  /**
   * Final dimensions for components
   */
    
  public static final int LEFT_WIDTH = 500; 
  public static final int RIGHT_WIDTH = 1200;

  public static final int TEXT_HEIGHT = 500;
  public static final int BUTTON_HEIGHT = 300;
  public static final int RIGHT_HEIGHT = 800;
  
  public static final int TEXT_IN_HEIGHT = 150;
  public static final int TEXT_OUT_HEIGHT = 350;
  
  public static final Dimension INTRO_BUTTON_SIZE = new Dimension(200, 80);
  public static final Dimension GAME_BUTTON_SIZE = new Dimension(200, 56);
  
  
  /**
   * The frame onto which everything is placed
   */
  private JFrame mainframe;
  
  /**
   * The PlayPanel onto which game information is drawn
   */
  private PlayPanel playPanel;

  /**
   * The panel onto which text information is drawn
   */
  private JPanel textPanel;
  
  /**
   * The panel onto which buttons are placed
   */
  private JPanel buttonPanel; 
  
  /**
   * The container for the left side of the screen
   */
  private JPanel leftContainer;
  
  /**
   * The container for buttons
   */
  private JPanel buttonContainer;
  
  /**
   * The text field for user text input
   */
  private final JTextField textInput = new JTextField();
  
  /**
   * The text block for displaying text to the user
   */
  private final JTextArea textOutput = new JTextArea();
  
  /**
   * A list to hold all intro buttons
   */
  private final ArrayList<JButton> introButtons = new ArrayList<JButton>();
  
  /**
   * A list to hold all gameplay buttons
   */
  private final ArrayList<JButton> gameButtons = new ArrayList<JButton>();

  /**
   * The 'new game' button
   */
  private final JButton newButton = new JButton("New");
  
  /**
   * The 'start game' button
   */
  private final JButton startButton = new JButton("Start");
  /**
   * The 'load game' button
   */
  private final JButton loadButton = new JButton("Load");
  
  /**
   * The 'save game' button
   */
  private final JButton saveButton = new JButton("Save");
  
  /**
   * The 'back to previous state' button
   */
  private final JButton backButton = new JButton("Back");
  
  /**
   * The 'exit game' button
   */
  private final JButton exitButton = new JButton("Exit");
  
  private final JButton mutateButton = new JButton("Mutate");
  
  private final JButton breedButton = new JButton("Breed");
 
  private final JButton placeButton = new JButton("Place");
  
  private final JButton engineerButton = new JButton("Engineer");
  
  private final JButton observeButton = new JButton("Observe");
  
  /*private static JButton exitConfirm, exitDeny;
  
  private static JButton[] exitDialogButtons;
  
  static {
    exitConfirm = new JButton("Yes, exit"); 
    exitConfirm.setMnemonic(KeyEvent.VK_ENTER);
    
    exitDeny = new JButton("No, I got here by accident");
    exitDeny.setMnemonic(KeyEvent.VK_ESCAPE); 
    
    exitDialogButtons = new JButton[] {
        exitConfirm, exitDeny
    };
  }*/
  
  /**
   * The background color
   */
  private final Color textPanelBackground = new Color(0x83a5fe);
  
  /**
   * The background color
   */
  private final Color buttonPanelBackground = new Color(0x5b84ff);
  
  /**
   * The background color for the input panel
   */
  private final Color textInputBackground = new Color(0x6b84ff);
  
  /**
   * The default font for textInput
   */
  private final Font textInFont = new Font("Garamond", Font.PLAIN, 32);
  
  /**
   * To return a singleton GUIHandler
   * @param gc The GameController that controls this GUIHandler
   * @return The singleton instance
   */
  public static GUIHandler getInstance(GameController gc) {
    if (instance == null) {
      GUIHandler.instance = new GUIHandler(gc);
    }
    Father.log("Retrieving GUIHandler instance.");
    return GUIHandler.instance;
  }
  
  private GUIHandler(GameController gc) {
    this.gc = gc;
  }
  
  /**
   * To assemble all of the necessary GUI components for the game
   */
  public void initGUI() {
    this.initComponents();
    this.initLayout();
    this.addComponents(); 
    this.finalizeComponents();
  }

  /**
   * To initialize all components in working order
   */
  private void initComponents() {
    this.MAINFRAME_TITLE = "Darwin's Playground | Version " + Father.GAME_VERSION;
    this.mainframe = new JFrame(this.MAINFRAME_TITLE);
    
    // Closing the window should prompt the user to save
    this.mainframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.mainframe.setResizable(false);  
    this.mainframe.addWindowListener(new WindowAdapter() {
      
      @Override
      public void windowClosing(WindowEvent e) {
        gc.exitGame();
      }
    });
    
    this.initPanels();
    this.initPanelComponents();
    this.initButtons();
  }

  /**
   * To initialize all panels: 
   */
  private void initPanels() {
    this.playPanel = new PlayPanel(this);
    this.playPanel.init();
    this.playPanel.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {
        gc.onMouseClick(e.getX(), e.getY());
      }

    });
    
    this.leftContainer = new JPanel();
    
    this.textPanel = new JPanel();
    this.textPanel.setPreferredSize(
        new Dimension(GUIHandler.LEFT_WIDTH, GUIHandler.TEXT_HEIGHT));
    this.textPanel.setBackground(textPanelBackground);
    
    this.buttonPanel = new JPanel();
    this.buttonPanel.setPreferredSize(
        new Dimension(GUIHandler.LEFT_WIDTH, GUIHandler.BUTTON_HEIGHT));
    this.buttonPanel.setBackground(buttonPanelBackground);
    
    this.buttonContainer = new JPanel();
    this.buttonContainer.setBackground(this.buttonPanelBackground);
  }
  
  /**
   * To initialize all GUI components that go inside of panels
   */
  private void initPanelComponents() {
    // textInput 
    this.textInput.setPreferredSize(
        new Dimension(GUIHandler.LEFT_WIDTH, GUIHandler.TEXT_IN_HEIGHT));
    this.textInput.setBackground(Color.WHITE); 
    
    ActionListener a = (e -> {
      String txt = e.getActionCommand();
      if (!txt.equals("")) {
        enterPressed(txt);
      }
    });
    
    this.textInput.addActionListener(a);
    this.textInput.setFont(textInFont);
    this.textInput.setBackground(this.textInputBackground);
    this.textInput.setBorder(null);
    
    // textOutput 
    this.textOutput.setPreferredSize(
        new Dimension(GUIHandler.LEFT_WIDTH, GUIHandler.TEXT_OUT_HEIGHT));
    this.textOutput.setBackground(Color.GREEN);
    this.textOutput.setEditable(false);
    this.textOutput.setFont(textInFont);
    this.textOutput.setBackground(this.textPanelBackground);
    this.textOutput.setLineWrap(true);
    this.textOutput.setWrapStyleWord(true);
  }
  
  /**
   * To initialize all buttons
   */
  private void initButtons() { 
    
    introButtons.add(newButton); 
    introButtons.add(loadButton); 
    introButtons.add(saveButton); 
    introButtons.add(startButton); 
    introButtons.add(backButton); 
    introButtons.add(exitButton);

    gameButtons.add(breedButton); 
    gameButtons.add(mutateButton); 
    gameButtons.add(placeButton); 
    gameButtons.add(engineerButton);
    gameButtons.add(observeButton);
    gameButtons.add(saveButton);
    gameButtons.add(exitButton);
    
    ActionListener a = (e -> {
      gc.buttonPressed(e.getActionCommand());
    });
    
    for (JButton b : introButtons) {
      b.setPreferredSize(GUIHandler.INTRO_BUTTON_SIZE);
      b.addActionListener(a);
    }
    
    for (JButton b : gameButtons) {
      b.setPreferredSize(GUIHandler.GAME_BUTTON_SIZE);
      b.addActionListener(a); 
    } 
    gameButtons.add(backButton);
    
  }
  
  /**
   * To initialize the layout of this.mainframe
   */
  private void initLayout() {
    this.mainframe.setLayout(new BorderLayout());
    this.leftContainer.setLayout(new BorderLayout());
    this.textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.PAGE_AXIS));
  
    this.buttonContainer.setLayout(new GridLayout(0, 2, 20, 20)); 
  }
  
  /**
   * To add all components to their containers
   */
  private void addComponents() {
    for (JButton b : this.introButtons) {
      this.buttonContainer.add(b);
    }
    
    this.textPanel.add(this.textOutput);
    this.textPanel.add(this.textInput);
    
    this.buttonPanel.add(this.buttonContainer);
    
    this.leftContainer.add(this.textPanel, BorderLayout.NORTH);
    this.leftContainer.add(this.buttonPanel, BorderLayout.SOUTH);
    
    this.mainframe.add(this.leftContainer, BorderLayout.WEST);
    this.mainframe.add(this.playPanel, BorderLayout.EAST);
  }
  
  
  /**
   * To pack and show the mainframe
   */
  private void finalizeComponents() {
    this.mainframe.pack();
    this.mainframe.setVisible(true);
    Father.log("Frame packed and visible.");
  } 
  
  /**
   * To change the buttons on the screen from the set of intro buttons (new, load, etc)
   * to the set of gameplay buttons (breed, mutate, etc)
   */
  public void changeButtons() {
    for (JButton b : this.introButtons) {
      this.buttonContainer.remove(b);
    } 
    
    this.backButton.setPreferredSize(GUIHandler.GAME_BUTTON_SIZE);

    for (JButton b : this.gameButtons) {
      this.buttonContainer.add(b);
    }
    this.buttonContainer.add(this.backButton);
    
  }
  
  /**
   * To change the text on textOutput
   * @param input The text to show
   */
  public void setText(String input) {
    this.textOutput.setText(input);
  }
  
  /**
   * To add text to textOutput
   */
  public void addText(String text) {
    String current = this.textOutput.getText();
    this.textOutput.setText(current + text);
  }
  
  /** 
   * To respond to the user sending a message through textInput
   * @param input The action command (user input) sent by textInput
   */
  void enterPressed(String input) { 
    this.gc.userInput(input);
    this.textInput.setText(""); 
  } 
  
  /**
   * To render the appropriate graphics onto this.playPanel
   * @param g
   */
  void drawPlayPanel(Graphics2D g) {
    this.gc.drawPlayPanel(g);
  }
  
  /**
   * To repaint the play panel
   */
  public void repaint() {
    SwingUtilities.invokeLater(() -> {
      this.playPanel.repaint(); 
    });
  }
  
  /**
   * To render text onto the PlayPanel
   * @param g
   * @param toSay
   */
  public void renderTextOnPlayPanel(Graphics2D g, String[] toSay) {
    //this.player.renderTextOnPlayPanel(g, toSay);
    int xPos = 50;
    int yPos = 50;
    
    g.setColor(Color.WHITE);
    g.setFont(Organism.FONT_ORGANISM_TEXT);
    
    for (String s : toSay) {
      String[] parts = s.split("\n");
      for (String p : parts) {
        g.drawString(p, xPos, yPos);  
        yPos += 30; 
      }
      yPos += 50;
    }
  }
  
  /**
   * To confirm that the user wants to quit
   */
  public boolean confirmExit() { 
    int confirm = JOptionPane.showOptionDialog(this.mainframe, "Are you sure you want to quit?",
        "Confirm Quit!", JOptionPane.DEFAULT_OPTION, 0, null,
        new String[] {
            "Yes, quit", "No, I got here by accident"
        }, 1); 
    return (confirm == JOptionPane.YES_OPTION);
  }

  /**
   * To handle a mouse click on the screen
   * @param x
   * @param y
   */
  public void onMouseClick(int x, int y) {
    this.gc.onMouseClick(x, y);
  }
  
}






