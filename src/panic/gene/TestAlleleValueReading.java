package panic.gene;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import panic.io.DataIO;

public class TestAlleleValueReading {

  @BeforeClass
  public static void init() {
    DataIO.loadAlleleValues();
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
    map = toTest.alleleMap;
  }
  
  private int getNumAllelesTotal() {
    return Arrays.stream(Trait.values())
        .map(t -> t.getAlleles())
        .map(a -> a.length) 
        .mapToInt(i -> i)
        .sum(); 
  }
  
  public List<Double> getAllelTraitKeyValues() {
    Trait[] traits = Trait.values();  
    List<Double> vals = new ArrayList<Double>();
    for (Trait t : traits) {
      for (Allele a : t.getAlleles()) {
        String key = MapKeyGenerator.alleleAndTraitToKey(a, t);
        vals.add(this.map.get(key));
      }
    } 
    assertEquals(vals.size(), this.getNumAllelesTotal());
    return vals;
  }

  public void testValues(Double[] expected) { 
    List<Double> vals = this.getAllelTraitKeyValues(); 
    
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i], vals.get(i));
    }
  } 
  
  @Test
  public void testPredationValues() {
    this.initPredation();
    this.testValues(new Double[] {
        -0.6,-0.3,0., 0.3, 0.6,
        -0.4,  -0.2,  0. ,0.2, 0.4,
        -0.5,  0., 0.5

    });
  }  
  
  @Test
  public void testAppetiteValues() {
    this.initAppetite();
    this.testValues(new Double[] {
        0.75,  0.1, -0.25, -0.5,  -0.85, 
        0.4, 0.2, 0., -0.2,  -0.4,
        0., 0., 0.

    });
  }

  @Test
  public void testAdaptabilityValues() {
    this.initAdaptability();
    this.testValues(new Double[] {
        0.4, 0.2, 0., -0.2,  -0.4,
        -0.4,  -0.2,  0., 0.2, 0.4,
        0.2, 0., -0.2

    });
  }
  
  @Test
  public void testMobilityValues() {
    this.initMobility();
    this.testValues(new Double[] {
        0.8, 0.6, 0., -0.45, -0.85,
        -0.75, -0.3,  0., 0.4, 0.85,
        0.2, 0., -0.2

    });
  }
}





