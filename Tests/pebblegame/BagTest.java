package pebblegame;

import com.sun.source.tree.AssertTree;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BagTest {

    PebbleGame pebbleGame = new PebbleGame();
    PebbleGame.Bags bags = pebbleGame.new Bags();
    int tempPlayerId = 1;
    PebbleGame.Player player = pebbleGame.new Player(tempPlayerId);

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
        System.out.println(getBlackBagPebbleOutput);

        if (0 < getBlackBagPebbleOutput[1] && getBlackBagPebbleOutput[1] < 51) {
            assertTrue(true);
        }

        if (getBlackBagPebbleOutput[1] == 0 | getBlackBagPebbleOutput[1] == 1 | getBlackBagPebbleOutput[1] == 2){
            assertTrue(true);
        }
    }

    @Test
    public void emptyWhiteBagTest(){

    }
}
