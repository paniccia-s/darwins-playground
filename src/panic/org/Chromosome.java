package panic.org;

import java.awt.Font;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Random;

import panic.gene.Allele;
import panic.gene.Gene;
import panic.gene.MapKeyGenerator;
import panic.gene.SurvivalTrait;
import panic.gene.Trait;

public class Chromosome implements Serializable {  
  private static final long serialVersionUID = 1L;
  
  /**
   * The map of Genes and their Trait keys for this Chromosome
   */ 
  
  private EnumMap<Trait, Gene> genes;
  
  /**
   * A list of all Traits used by this Chromosome
   */
  private Trait[] allTraits;
  
  private final int DRAW_Y_OFFSET = 30;
  private final int DRAW_BOTTOM_OFFSET = 10;
  private final int DRAW_CHARS_PER_LINE = 80;
  private final Font FONT_CHROMOSOME = new Font("Arial", Font.PLAIN, 24);
  
  
  public Chromosome(Trait[] allTraits) {
    this.allTraits = allTraits;
    this.addGenes();
  }
  
  private void addGenes() {
    this.genes = new EnumMap<Trait, Gene>(Trait.class);
    for (Trait t : this.allTraits) {
      Gene toAdd = new Gene(t);
      this.genes.put(t, toAdd);
    }
  } 
  
  
  public String printGenes() {
    String s = "";
    for (Gene g : this.genes.values()) {
      s += ", " + g.toString();
    }
    return s.substring(2);
  }
  
  public int draw(Graphics2D g, int xPos, int yPos, HashMap<Allele, Boolean> discovered) {
    g.setFont(this.FONT_CHROMOSOME);
    String toDraw = "";
    int numLines = 1;
    int numCharsInThisLine = 0;
    
    for (Gene gene : this.genes.values()) {
      String thisGene = gene.getStringRepOfCurrentAllele(discovered) + " ";
      numCharsInThisLine += thisGene.length();
      if (numCharsInThisLine >= this.DRAW_CHARS_PER_LINE) {
        numLines++;
        numCharsInThisLine = 0;
        toDraw += "\n";
      }
      toDraw += thisGene;
    }
    
    String[] lines = toDraw.split("\n");
    
    for (int i = 0; i < lines.length; i++) {
      String line = lines[i];
      g.drawString(line, xPos, yPos + (i * this.DRAW_Y_OFFSET));
      
    }
    return (this.DRAW_Y_OFFSET * numLines) + this.DRAW_BOTTOM_OFFSET;
  }
  
  @Override
  public String toString() {
    return this.printGenes();
  }

  /**
   * To mutate one Gene in this Chromosome
   */
  public Optional<Allele> mutate(double scenarioChance) {  
    double randAdaptability = new Random().nextGaussian() * 0.5;
    double geneticAdaptability = this.getSurvivalTraitValue(SurvivalTrait.adaptability);
    
    double totalAdaptability = geneticAdaptability + randAdaptability + scenarioChance;
    
    if (totalAdaptability >= 0) { 
      List<Trait> thisTraits = new ArrayList<Trait>(this.genes.keySet());
      int rand = new Random().nextInt(thisTraits.size());
      Trait which = thisTraits.get(rand);
      Gene toMutate = this.genes.get(which);
      return Optional.of(toMutate.mutate()); 
    } 
    return Optional.empty();
  }
  
  /**
   * To return the Allele at the given index in this Chromosome
   */
  public Allele getAlleleAt(Trait which) {
    Gene toReturn = this.genes.get(which);
    return toReturn.getAllele();
  }
  
  /**
   * To set the Allele of the given Trait 
   */
  public void setGene(Trait which, Allele allele) {
    Gene toChange = this.genes.get(which);
    toChange.setAllele(allele); 
  } 
  
  public Trait[] getAllTraitsInChromosome() {
    return this.allTraits;
  }
  
  public double getSurvivalTraitValue(SurvivalTrait which) { 
    OptionalDouble average;
    
    average = this.genes.values().stream()
    .map(gene -> gene.getAllele())
    .map(allele -> which.getAlleleValue(
        MapKeyGenerator.alleleAndTraitToKey(allele, allele.whichTrait())))
    .mapToDouble(d -> d) 
    .filter(d -> d > -1) 
    .average();

    if (average.isPresent()) {
      return average.getAsDouble();
    } else {
      throw new UnsupportedOperationException("what");
    }
  }
} 



