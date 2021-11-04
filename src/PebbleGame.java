import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.*;

public class PebbleGame{

    ArrayList<ArrayList> players = new ArrayList<>();

    public synchronized static void main(String[] args){
        PebbleGame pg = new PebbleGame();
        int numPlayers = 0;
        String players;
        //System.out.println is used for every new line to ensure the code is platform-independent
        System.out.println("Welcome to the PebbleGame!!");
        System.out.println("You will be asked to enter the number of players.");
        System.out.println("And then for the location of three files in turn containing comma separated integer values for the pebble weights.");
        System.out.println("The integer values must be strictly positive.");
        System.out.println("The game will then be simulated, and output written to files in this directory");
        System.out.println();

        //Input for number of players
        while (numPlayers < 1){
            System.out.println("Please enter the number of players:");
            Scanner sc = new Scanner(System.in);
            players = sc.nextLine();
            try {
                numPlayers = Integer.parseInt(players);
            }catch(Exception e){
                if (players.equals("E")) System.exit(0);
            }
        }

        //Input for files
        ArrayList<ArrayList> fileInfo = new ArrayList<>();

        for (int i = 0; i < 3; i++){
            ArrayList<Integer> temp = new ArrayList<>();
            boolean fileFlag = false;
            while(!fileFlag){
                System.out.println("Please enter location of bag number " + i + " to load");
                Scanner sc = new Scanner(System.in);
                String fileName = sc.nextLine();
                try {
                    Scanner f = new Scanner(new File(fileName));
                    while (f.hasNextLine()) {
                        String weightValues = f.nextLine();
                        String[] arrOfWeighValues = weightValues.split(",", 0);
                        for (String str : arrOfWeighValues) temp.add(Integer.valueOf(str));
                        fileFlag = true;
                        fileInfo.add(temp);
                    }
                }catch(Exception e){
                    if (fileName.equals("E")) System.exit(0);
                }
            }
        }

        ArrayList<Integer> bagA = fileInfo.get(0);
        ArrayList<Integer> bagB = fileInfo.get(1);
        ArrayList<Integer> bagC = fileInfo.get(2);

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
