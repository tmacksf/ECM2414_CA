import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.*;

public class PebbleGame{

    ArrayList<ArrayList> players = new ArrayList<>();
    Bags bags = new Bags();

    public static void main(String[] args) {
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
        while (numPlayers < 1) {
            System.out.println("Please enter the number of players:");
            Scanner sc = new Scanner(System.in);
            players = sc.nextLine();
            try {
                numPlayers = Integer.parseInt(players);
            } catch (Exception e) {
                if (players.equals("E")) System.exit(0);
            }
        }

        //Input for files
        ArrayList<ArrayList> fileInfo = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            ArrayList<Integer> temp = new ArrayList<>();
            boolean fileFlag = false;
            while (!fileFlag) {
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
                } catch (Exception e) {
                    if (fileName.equals("E")) System.exit(0);
                }
            }

        }

        ArrayList<Integer> bagA = fileInfo.get(0);
        ArrayList<Integer> bagB = fileInfo.get(1);
        ArrayList<Integer> bagC = fileInfo.get(2);

        //Player player = new Player(bagA, bagB, bagC);
        //Bags bag = new pebbleGame.Bags(bagA, bagB, bagC);


        pg.bags.addBags(bagA, bagB, bagC);


        Thread player = new Thread(new Player());
        player.run();
    }


     class Bags{

        ArrayList<Integer> bagA;
        ArrayList<Integer> bagB;
        ArrayList<Integer> bagC;

        public void addBags(ArrayList bagA, ArrayList bagB, ArrayList bagC){
            this.bagA = bagA;
            this.bagB = bagB;
            this.bagC = bagC;
        }

        /*public Bags(ArrayList bagA, ArrayList bagB, ArrayList bagC){
            this.bagA = bagA;
            this.bagB = bagB;
            this.bagC = bagC;
        }*/

        ArrayList<Integer> bagX = new ArrayList<>();
        ArrayList<Integer> bagY = new ArrayList<>();
        ArrayList<Integer> bagZ = new ArrayList<>();

        public int getBagSize(int blackBag){
            int bagSize = 0;
            if (blackBag == 0){
                bagSize += bagX.size();
            }
            else if (blackBag == 1){
                bagSize += bagY.size();
            }
            else {
                bagSize += bagZ.size();
            }
            return bagSize;
        }

        //gets the pebble with a given index and bag number then removes it from bag
        public int getPebble(int blackBag, int pebbleIndex){
            int pebble = 0;

            if (blackBag == 0){
                pebble = bagA.get(pebbleIndex);
                bagA.remove(pebbleIndex);
            }
            else if (blackBag == 1){
                pebble = bagB.get(pebbleIndex);
                bagB.remove(pebbleIndex);
            }
            else {
                pebble = bagC.get(pebbleIndex);
                bagC.remove(pebbleIndex);
            }
            return pebble;
        }

        public ArrayList getWhiteBag(int whiteBag){
            if (whiteBag == 0){
                return bagX;
            }
            else if (whiteBag == 1){
                return bagY;
            }
            else {
                return bagZ;
            }
        }

        //puts the white bag's contents into the corresponding black bag
        public synchronized void emptyWhiteBag(int bagIndex){
            if (bagIndex == 0){
                bagA = bagX;
                bagX.clear();
            }
            else if (bagIndex == 1){
                bagB = bagY;
                bagY.clear();
            }
            else if (bagIndex == 2){
                bagC = bagZ;
                bagZ.clear();
            }
        }

        public synchronized void addToWhiteBag(int marble, int bagIndex){
            if (bagIndex == 0){
                bagX.add(marble);
            }
            else if (bagIndex == 1){
                bagY.add(marble);
            }
            else if (bagIndex == 2){
                bagZ.add(marble);
            }
        }
    }

    class Player implements Runnable{

        ArrayList<Integer> bagA;
        ArrayList<Integer> bagB;
        ArrayList<Integer> bagC;

        public Player(ArrayList bagA, ArrayList bagB, ArrayList bagC){
            this.bagA = bagA;
            this.bagB = bagB;
            this.bagC = bagC;
        }

        ArrayList<Integer> playerBag = new ArrayList<>();
        //Bags bags = new Bags(bagA, bagB, bagC);

        public synchronized int pick() throws InterruptedException{
            //uses thread safe random number to decide what bag
            int bagNum = ThreadLocalRandom.current().nextInt(0,3);

            //gets the bag size, then marble index, then marble
            int bagsize = bags.getBagSize(bagNum);
            int marbleIndex = ThreadLocalRandom.current().nextInt(0,bagsize);
            int marble = bags.getPebble(bagNum,marbleIndex);

            //puts marble into players bag
            playerBag.add(marble);
            return bagNum;
        }

        /**
         *
         * @param playerMarbleIndex for the index of the marble to be removed in the player bag
         * @param whiteBagIndex the corresponding white bag which the marble is moved to
         */
        public synchronized void deposit(int playerMarbleIndex, int whiteBagIndex){
            int marble = playerBag.get(playerMarbleIndex);
            playerBag.remove(playerMarbleIndex);
            bags.addToWhiteBag(marble, whiteBagIndex);
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

        public synchronized void start(){
            try {
                for (int i = 0; i < 10; i++) {
                    int bagNum = ThreadLocalRandom.current().nextInt(0, 3);
                    int bagsize = PebbleGame.Bags.getBagSize(bagNum);
                    System.out.println(bagsize);
                    int marbleIndex = ThreadLocalRandom.current().nextInt(0, bagsize);
                    int marble = bags.getPebble(bagNum, marbleIndex);

                    playerBag.add(marble);
                }
                if (checker(playerBag)) {
                    System.out.println("Player " + Thread.currentThread().getName() + " has won!");
                    Thread.sleep(10000);
                } else {
                    ;
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void run(){
            synchronized (this){
                //might only need the while or try not sure if both are needed
                try{
                    start();
                    System.out.println(playerBag);
                    /*int bagTotal = 0;
                    while (bagTotal != 100){
                        int correspondingWhiteBag = pick();
                        int discardIndex = ThreadLocalRandom.current().nextInt(0,playerBag.size());
                        deposit(discardIndex, correspondingWhiteBag);
                        checker(playerBag);
                    }*/
                }
                catch(Exception e) {
                    System.out.println("Exception in the run");
                }
            }
        }

    }
}
