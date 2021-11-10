package pebblegame;

import com.sun.source.tree.AssertTree;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {

    PebbleGame pebbleGame = new PebbleGame();
    PebbleGame.Bags bags = pebbleGame.new Bags();
    int tempPlayerId = 1;
    PebbleGame.Player player = pebbleGame.new Player(tempPlayerId);

    ArrayList<Integer> blackPebbleBag = new ArrayList<>();

    private int sizeOfBags = 50;

    @Before
    public void setUp() {
        for (int i = 0; i < sizeOfBags; i ++){
            blackPebbleBag.add(i+1);
        }
        bags.addBags(blackPebbleBag, blackPebbleBag, blackPebbleBag);
    }

    @Test
    public void startTest() {

        for (int i = 0; i < sizeOfBags; i ++){
            blackPebbleBag.add(i+1);
        }
        bags.addBags(blackPebbleBag, blackPebbleBag, blackPebbleBag);

        player.start();
        ArrayList playerBag = player.playerBag;
        Assert.assertEquals(10, playerBag.size());
    }

    @Test
    public void checkerTest(){
        for (int i = 0; i < 10; i ++){
            player.playerBag.add(10);
        }
        assertEquals(true, player.checker());
    }

    @Test
    public void pickTest(){
        player.pick();
        assertEquals(1, player.playerBag.size());
    }


}
