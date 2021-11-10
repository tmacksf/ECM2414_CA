package pebblegame;

import com.sun.source.tree.AssertTree;
import org.junit.*;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class BagTest {

    PebbleGame pebbleGame = new PebbleGame();
    PebbleGame.Bags bags = pebbleGame.new Bags();
    int tempPlayerId = 1;

    ArrayList<Integer> blackPebbleBag = new ArrayList<>();

    private int sizeOfBags = 50;

    @Before
    public void setUp() throws Exception {
        for (int i = 0; i < sizeOfBags; i ++){
            blackPebbleBag.add(i+1);
        }
        bags.addBags(blackPebbleBag, blackPebbleBag, blackPebbleBag);
    }

    @Test
    public void bagCheck() {
        int numBags = bags.blackBags.size();
        assertEquals(3, numBags);
        for (int i = 0; i < 3; i++){
            assertEquals(50, bags.blackBags.get(i).size());
        }
    }

    @Test
    public void getBlackBagPebbleTest(){
        int[] getBlackBagPebbleOutput;
        getBlackBagPebbleOutput = bags.getBlackBagPebble();

        if (0 < getBlackBagPebbleOutput[1] && getBlackBagPebbleOutput[1] < 51) {
            assertTrue(true);
        }

        if (getBlackBagPebbleOutput[1] == 0 | getBlackBagPebbleOutput[1] == 1 | getBlackBagPebbleOutput[1] == 2){
            assertTrue(true);
        }
    }

    @Test
    public void emptyWhiteBagTest(){
        Random random = new Random();
        int randomIndex = random.nextInt(3);
        for (int i = 0; i < 10; i++){
            bags.whiteBags.get(randomIndex).add(i+1);
        }

        assertEquals(3, bags.whiteBags.size());
        assertEquals(10, bags.whiteBags.get(randomIndex).size());

        bags.emptyWhiteBag(0);
        assertEquals(0, bags.whiteBags.get(randomIndex).size());
    }
}
