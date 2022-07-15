//Names: Sophie Pallanck, Alex Schwendeman
//This class implements the dice players will
//roll while acting or when a scene is wrapped
import java.util.*;

public class Dice {

    private static Random r = new Random();

    // Returns a number between 1 and 6 (replicating the roll of a real die).
    public static int roll() {
        return r.nextInt(6) + 1;
    }

    // Rolls the number of dice equal to the budget of the movie so that 
    // players working on a scene card can receive the correct payout
    public static List<Integer> rollPayout(int budget) {
        List<Integer> results = new ArrayList<>();
        for (int i = 0; i < budget; i++) {
            results.add(r.nextInt(6) + 1);
        }
        Collections.sort(results, Collections.reverseOrder());
        return results;
    }

}
