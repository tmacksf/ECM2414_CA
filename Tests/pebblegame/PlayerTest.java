package pebblegame;

import com.sun.source.tree.AssertTree;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {

    PebbleGame pebbleGame = new PebbleGame();
    PebbleGame.Bags bags = pebbleGame.new Bags();
    int tempPlayerId = 1;
    PebbleGame.Player player = pebbleGame.new Player(bags, tempPlayerId);

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
        player.start();
        ArrayList playerBag = player.playerBag;
        Assert.assertEquals(10, playerBag.size());
    }

    @Test
    public void checkerTest(){
        player.playerBag.clear();
        for (int i = 0; i < 10; i ++){
            player.playerBag.add(10);
        }
        assertEquals(true, player.checker());

        player.playerBag.clear();
        for (int i = 0; i < 10; i ++){
            player.playerBag.add(1);
        }
        assertEquals(false, player.checker());

    }

    @Test
    public void pickTest(){
        for (int i = 0; i < 10; i ++) {
            player.playerBag.add(i+1);
        }
        player.pick();
        assertEquals(10, player.playerBag.size());
    }


}
