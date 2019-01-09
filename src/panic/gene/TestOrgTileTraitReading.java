package panic.gene;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import panic.io.DataIO;

public class TestOrgTileTraitReading {

  @BeforeClass
  public static void init() {
    DataIO.loadOrgTileTraitVals();
  }
  
  private HashMap<String, Double> map;

  private void initPredation() { 
    this.initWith(SurvivalTrait.predation);
  }
  
  private void initAppetite() {
    this.initWith(SurvivalTrait.appetite);
  }
  
  private void initAdaptability() {
    this.initWith(SurvivalTrait.adaptability);
  }
  
  private void initMobility() {
    this.initWith(SurvivalTrait.mobility);
  }
  
  private void initWith(SurvivalTrait toTest) {
    map = toTest.organismTileTraitValues;
  } 
  
  private List<Double> getOrgTileVals() {
    List<Double> vals = new ArrayList<Double>();
    
    String[] allOrgs = new String[] {
        "CyanoBacterium"
    };
    
    String[] allTiles = new String[] {
      "Forest", "Mountain", "Ocean", "Deep Ocean"  
    };
    
    for (String org : allOrgs) {
      for (String tile : allTiles) {
        String key = MapKeyGenerator.tileAndSpeciesToKey(tile, org);
        vals.add(map.get(key));
      }
    }
    
    return vals;
  }
  
  public void testValues(Double[] expected) { 
    List<Double> vals = this.getOrgTileVals();
    
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i], vals.get(i));
    }
    
  } 
  
  @Test
  public void testPredationValues() {
    this.initPredation();
    this.testValues(new Double[] {
        0.5 ,0. ,0. ,0.
    });
  }
  
  @Test
  public void testAppetiteValues() {
    this.initAppetite();
    this.testValues(new Double[] {
        0. ,0. ,0. ,0.
    });
  }
  
  @Test
  public void testAdaptabilityValues() {
    this.initAdaptability();
    this.testValues(new Double[] {
        0., 0., 0., 0.
    });
  }
  
  @Test 
  public void testMobility() {
    this.initMobility();
    this.testValues(new Double[] {
        0., 0., 0., 0.
    });
  }
}
