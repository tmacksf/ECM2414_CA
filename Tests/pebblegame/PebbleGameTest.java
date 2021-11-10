package pebblegame;

import com.sun.source.tree.AssertTree;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PebbleGameTest {

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
    
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void name() {

    }

    @Test
    public void pickTest() {

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
    public void getBlackPebbleTest() throws Exception{
        int[] blackPebble = bags.getBlackBagPebble();
        int pebbleVal = blackPebble[0];
        int bagIndex = blackPebble[1];

    }

    @Test
    public void startTest() {
        player.start();
        ArrayList playerBag = player.playerBag;
        Assert.assertEquals(10, playerBag.size());
    }

    @Test
    public void main() {
    }
}