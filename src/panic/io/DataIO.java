package panic.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import panic.game.Father;
import panic.game.Player;
import panic.gene.MapKeyGenerator;
import panic.gene.SurvivalTrait;
import panic.gene.Trait;
import panic.tile.BoardTile;
import panic.tile.DeepOceanTile;
import panic.tile.ForestTile;
import panic.tile.MountainTile;
import panic.tile.OceanTile;

public class DataIO {

  /**
   * The character used to separate entries in all data files
   */
  private static final String DELIMITER = "\t";
  
  /**
   * To parse a Scenario file into a list of BoardTiles
   * @return the number of columns on the board
   */
  public static int readTiles(String which, ArrayList<BoardTile> board) { 
    
    String root = "res/scenario/" + which + ".sno";
    Father.log("Attempting to read in board data: " + root);
    int h = 0;
    try {
      FileReader fin = new FileReader(root);
      BufferedReader b = new BufferedReader(fin);
      
      //first line is width, second is height
      
      int w = Integer.valueOf(b.readLine());
      h = Integer.valueOf(b.readLine());
      
      // or do mod/div on one int for row/col but this is fine
      int colNum = 0;
      int rowNum = 0;
      
      while(b.ready()) {
        int next = b.read(); 
        if ('!' <= next && next <= '~') {
          switch (next) {
          case 'o':
            // Ocean tile
            board.add(new OceanTile(rowNum, colNum));
            break;
          case 'f':
            // Forest tile
            board.add(new ForestTile(rowNum, colNum));
            break;
          case 'm':
            // Mountain tile
            board.add(new MountainTile(rowNum, colNum));
            break;
          case 'd':
            // Deep Ocean tile
            board.add(new DeepOceanTile(rowNum, colNum));
            break;
          default:
              throw new IllegalArgumentException("Illegal character: " + next); 
          } 
          colNum++;
          if (colNum == w) {
            colNum = 0;
            rowNum++;
          } 
        }
      }
      
      Father.log("Reading succeeded!");
      b.close();  
    }
    catch (Exception e) {
      Father.log("Reading FAILED! " + e.getMessage());
      e.printStackTrace();
      System.exit(-1); 
    }
    return h;
  }
   
  
  /**
   * To load in a player file
   */
  public static Player loadPlayer(String name) {
    String root = "res/playerdata/" + name + ".plr";
    Father.log("Attempting to read player data: " + root);
    Player p = null;
    try {
      File f = new File(root);
      if (!f.exists()) {
        return null;
      }
      FileInputStream fin = new FileInputStream(f); 
      ObjectInputStream in = new ObjectInputStream(fin); 
      p = (Player) in.readObject();
      Father.log("Reading succeeded!");
      in.close();
    } catch (Exception e) {
      Father.log("Reading FAILED! " + e.getMessage());
      e.printStackTrace();
    }
    return p;
  }
  
  /**
   * To write to a player file
   */
  public static void writePlayer(Player p) {
    try {
      String name = p.getName();
      String root = "res/playerdata/" + name + ".plr"; 
      Father.log("Attempting to write player data: " + root);
      FileOutputStream fout = new FileOutputStream(root);
      ObjectOutputStream out = new ObjectOutputStream(fout);
      out.writeObject(p);
      out.close();
      Father.log("Write succeeded!");
    } catch (Exception e) { 
      Father.log("Write FAILED!" + e.getMessage());
      e.printStackTrace();
    }
  }

   
  /**
   * To retrieve the appropriate SurvivalTrait, given its name, or to 
   * throw an Exception if no SurvivalTrait of that name exists
   * @param name
   * @return
   * @throws Exception
   */
  private static SurvivalTrait getTraitFromName(String name) throws Exception {
    Optional<SurvivalTrait> maybeTrait = SurvivalTrait.getTraitFromName(name); 
    return maybeTrait.orElseThrow(
        () -> new IllegalArgumentException("Illegal Trait: " + name));
    
  }

  /**
   * To load Organism-Survivaltrait weights from org_trait_vals.txt and to
   * return a HashMap of Org-Trait String keys and Double values
   */
  public static HashMap<String, Double> loadTileValues() {
    HashMap<String, Double> orgVals = new HashMap<String, Double>();
    String root = "res/gamedata/tile_trait_vals.txt";
    try {
      FileReader fin = new FileReader(root);
      BufferedReader b = new BufferedReader(fin); 
      
      // first line: gather all possible traits
      String line = b.readLine();
      String[] parts = line.split(DataIO.DELIMITER);
      
      // first part is nothing
      int num = parts.length;
      SurvivalTrait[] traitsInOrder = new SurvivalTrait[num - 1];
      // for each part, add to traitsInOrder the associated SurvivalTrait from its name
      for (int i = 1; i < num; i++) {
        traitsInOrder[i - 1] = DataIO.getTraitFromName(parts[i]);
      } 
      
      // for each tile
      while(b.ready()) {
        // read line, split over delimiter
        line = b.readLine();
        parts = line.split(DataIO.DELIMITER);
        
        // the tile in question 
        String tile = parts[0];
        for (int i = 1; i < parts.length; i++) {
          SurvivalTrait currentTrait = traitsInOrder[i - 1];
          String key = MapKeyGenerator.tileAndTraitToKey(tile, currentTrait);
          double currentValue = Double.valueOf(parts[i]);
          orgVals.put(key, currentValue);
          //System.out.println(key);
        }
      }
      
      b.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return orgVals;
  }
  
  // REDONE
  /**
   * To load Tile-SurvivalTrait weights from tile_trait_vals.txt and to 
   * return a HashMap of Tile-Trait String keys and Double values
   * @return
   */
  public static HashMap<String, Double> loadOrgValues() {
    HashMap<String, Double> traitVals = new HashMap<String, Double>();
    String root = "res/gamedata/org_trait_vals.txt";
    try {
      FileReader fin = new FileReader(root);
      BufferedReader b = new BufferedReader(fin); 
      
      // first line: gather all possible traits
      String line = b.readLine();
      String[] parts = line.split(DataIO.DELIMITER);
      
      // first part is nothing
      int num = parts.length;
      SurvivalTrait[] traitsInOrder = new SurvivalTrait[num - 1];
      // for each part, add to traitsInOrder the associated SurvivalTrait from its name
      for (int i = 1; i < num; i++) {
        traitsInOrder[i - 1] = DataIO.getTraitFromName(parts[i]);
      } 
      
      // for each tile
      while(b.ready()) {
        // read line, split over delimiter
        line = b.readLine();
        parts = line.split(DataIO.DELIMITER);
        
        // the tile in question 
        String org = parts[0];
        for (int i = 1; i < parts.length; i++) {
          SurvivalTrait currentTrait = traitsInOrder[i - 1];
          String key = MapKeyGenerator.orgAndTraitToKey(org, currentTrait);
          double currentValue = Double.valueOf(parts[i]);
          traitVals.put(key, currentValue);
          //System.out.println(key);
        }
      }
      
      b.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return traitVals;
  }

  // REDONE
  /**
   * To load Allele-SurvivalTrait weights from allele_trait_vals.txt and to
   * give each SurvivalTrait a HashMap of Trait-Allele String key to value Double
   */
  public static void loadAlleleValues() {
    String root = "res/gamedata/allele_trait_vals.txt";   
    
    try {
      FileReader fin = new FileReader(root);
      BufferedReader b = new BufferedReader(fin); 
      
      // headers include a Trait followed by its Alleles
      String header = b.readLine().trim();
      String[] headers = header.split(DataIO.DELIMITER);
      //filter out the empty cells
      headers = Stream.of(headers)
          .filter(s -> !s.equals("")) 
          .toArray(s -> new String[s]);
      
      String thisLine = "";
      // for each Survival trait
      while(b.ready()) {
        // read, split over tab, filter out empty cells
        thisLine = b.readLine().trim();
        
        if (thisLine.equals("")) {
          break;
        }
        
        String[] parts = thisLine.split(DataIO.DELIMITER);
        parts = Stream.of(parts)
            .filter(s -> !s.equals(""))
            .toArray(s -> new String[s]);
        HashMap<String, Double> valsForSurvivalTrait = new HashMap<String, Double>(parts.length);
        // recall the current Survival trait
        String survivalTrait = parts[0];
        SurvivalTrait currentSurvivalTrait = DataIO.getTraitFromName(survivalTrait);
        Trait currentTrait = null;
        int currentAllele = 1;
        
        for (int i = 0; i < headers.length; i++) {
          String headerToConsider = headers[i];
          // if the current header is a Trait...
          Optional<Trait> maybeTrait = Trait.getTraitFromName(headerToConsider);
          if (maybeTrait.isPresent()) {
            currentTrait = maybeTrait.get();
          } else { 
            String allele = headerToConsider;
            String key = MapKeyGenerator.alleleAndTraitToKey(allele, currentTrait);
            double alleleValue = Double.valueOf(parts[currentAllele]);
            currentAllele = currentAllele + 1;
            valsForSurvivalTrait.put(key, alleleValue); 
          }
        }
        currentSurvivalTrait.setAlleleMap(valsForSurvivalTrait); 
      }  
      
      b.close();
    } catch (Exception e) {
      e.printStackTrace(); 
    }
  } 
  
  // REDONE
  /**
   * To load Organism-Tile-SurvivalTrait weights from org_tile_trait_vals.txt and to
   * give Organism a HashMap of Organism-Tile String keys and Double values
   */
  public static void loadOrgTileTraitVals() {
    String root = "res/gamedata/org_tile_trait_vals.txt";
    try {
      FileReader fin = new FileReader(root);
      BufferedReader b = new BufferedReader(fin);

      String header = b.readLine().trim();
      String[] tiles = header.split(DataIO.DELIMITER);
      
      String thisLine = ""; 
      String species = "";
      SurvivalTrait currentTrait;
      while(b.ready()) {
        //per organism
        thisLine = b.readLine();
        String[] parts = thisLine.split("\t"); 
        
        /*
         * To determine if strs denotes the start of reading a new Organism
         */
        Function<String[], Boolean> isStartingNewOrg = (strs) -> {
          return strs.length == 1;
        }; 
        
        if (isStartingNewOrg.apply(parts)) {
          //starting new Org
          species = parts[0];
        } else {
          // new tile-trait value
          currentTrait = DataIO.getTraitFromName(parts[0]);
          for (int i = 1; i < parts.length; i++) {
            String tile = tiles[i - 1];
            Double value = Double.valueOf(parts[i]);
            String key = MapKeyGenerator.tileAndSpeciesToKey(tile, species);
            //System.out.println(key + " " + value);
            currentTrait.putTileOrganismValue(key, value);
          }
        } 
         
      }
      
      b.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  
  
  /*
  public static void readOrganismTileSurvivalValues() {
    String root = "res/gamedata/org_tile_vals.dat";
    Father.log("Attempting to read Organism-Tile survival values from " + root);
    try {
      FileReader fin = new FileReader(root);
      BufferedReader b = new BufferedReader(fin);
      
      SurvivalTrait trait = null;
      
      while(b.ready()) {
        String line = b.readLine();
        // tab is a comment
        if (line.startsWith(DataIO.DELIMITER)) {
          continue;
        }
        
        String[] vals = line.split(" ");
        
        //one-word line dictates a new trait
        if (vals.length == 1) {
          trait = DataIO.getTraitFromName(vals[0]);
          continue;
        } 
        
        String species = vals[0];
        String tile = vals[1];
        String key = MapKeyGenerator.tileAndSpeciesToKey(tile, species);
        double valToPut = Double.valueOf(vals[2]);
        trait.putTileOrganismValue(key, valToPut);
        
      }
      Father.log("Reading Succeeded!");
      b.close();
      
    } catch(Exception e) {
      Father.log("Reading FAILED!");
      Father.log(e.getMessage());
      e.printStackTrace();
    }
  }*/
  /*
  public static HashMap<String, Double> readAlleleTraitValues(String name) {
    HashMap<String, Double> traitVals = new HashMap<String, Double>();
    String root = "res/gamedata/allele_trait_vals.dat";
    try {
      FileReader fin = new FileReader(root);
      BufferedReader b = new BufferedReader(fin);  
      while (!b.readLine().equals(name)) {
        //ignore
      }
      
      String trait = b.readLine().substring(1);
      
      while (b.ready()) {
        String line = b.readLine();
        // a tab signifies the end of a trait
        if (line.equals(DataIO.DELIMITER)) {
          break;
        }
        
        // a space is a comment
        if (line.equals(" ")) {
          continue;
        } 
        
        // one space before is a new trait
        if (line.startsWith("-")) {
          trait = line.substring(1);
          continue;
        }
        line = line.trim();
        String[] parts = line.split(" ");
        String allele = parts[0];
        String key = MapKeyGenerator.alleleAndTraitToKey(allele, trait);
        double value = Double.valueOf(parts[1]);
        traitVals.put(key, value); 
      } 
      
      b.close(); 
    } catch(Exception e) {
      e.printStackTrace();
    }
    
    return traitVals;
  }
 
 
  public static HashMap<String, Double> readTileTraitValues() {
    HashMap<String, Double> traitVals = new HashMap<String, Double>();
    String root = "res/gamedata/tile_trait_vals.dat";
    try {
      FileReader fin = new FileReader(root);
      BufferedReader b = new BufferedReader(fin);
      
      String trait = "";
      
      while(b.ready()) {
        String line = b.readLine();
        // tab is a comment
        if (line.startsWith(DataIO.DELIMITER)) {
          continue;
        }
        
        String[] vals = line.split(" ");
        
        //one-word line dictates a new trait
        if (vals.length == 1) {
          trait = vals[0];
          continue;
        } 
        
        String tile = vals[0]; 
        double valToPut = Double.valueOf(vals[1]);
        
        String key = MapKeyGenerator.tileAndTraitToKey(tile, trait);
        traitVals.put(key, valToPut);
        
      }
      
      b.close(); 
    } catch(Exception e) {
      e.printStackTrace();
    }
    return traitVals;
  }

  
 
  */
  
}
