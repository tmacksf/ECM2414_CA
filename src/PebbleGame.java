import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.util.concurrent.*;

public class PebbleGame{

    ArrayList<ArrayList> players = new ArrayList<>();

    public synchronized static void main(String[] args){
        PebbleGame pg = new PebbleGame();
        String players;
        System.out.println("How many players?");
        Scanner sc = new Scanner(System.in);
        players = sc.nextLine();
        int playInt = Integer.valueOf(players);

        //pg.threads(playInt);

        Read read = new Read(playInt);
        Write write = new Write(playInt);

        ArrayList<Integer> bagA = read.getFileInfo().get(0);
        ArrayList<Integer> bagB = read.getFileInfo().get(1);
        ArrayList<Integer> bagC = read.getFileInfo().get(2);

        ArrayList<Integer> bagX = new ArrayList<>();
        ArrayList<Integer> bagY = new ArrayList<>();
        ArrayList<Integer> bagZ = new ArrayList<>();

        Player p = new Player();
        p.run();

    }
}


class Player implements Runnable{
    public void run(){
        Player p = new Player();
        try{
            Thread thread = new Thread();
            thread.start();
            thread.join();
            p.pick();

        }
        catch(Exception e){

        }
    }

    public void pick() throws InterruptedException{
        int bag = ThreadLocalRandom.current().nextInt(0,3);

        while (true) {

        }

    }

    public void deposit(){

    }

    public boolean checker(ArrayList playerBag){
        boolean result = false;

        int bagSize = playerBag.size();
        int total = 0;
        for (int i = 0; i < bagSize; i++){
            total += playerBag.indexOf(i);
        }

        return result;
    }

}
