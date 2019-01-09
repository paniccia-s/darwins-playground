package panic.gene;

import java.util.Random;

public enum Sex {
  male, female;
  
  public static Sex randomSex() {
    if (new Random().nextBoolean()) {
      return Sex.male;
    } else {
      return Sex.female;
    }
  }
}
