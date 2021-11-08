import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.Random;

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

        pg.bags.addBags(bagA, bagB, bagC);

        //creates
        ExecutorService threadPool = Executors.newFixedThreadPool(numPlayers);

        for (int i = 0; i < numPlayers; i ++) {
            System.out.println("Player " + i + " has started");
            Runnable player = pg.new Player();
            //need to learn about threadpool
            threadPool.execute(player);
        }

    }

     class Bags{
        Random random = new Random();

        ArrayList<ArrayList> blackBags = new ArrayList<>();
        ArrayList<ArrayList> whiteBags = new ArrayList<>();

        public void addBags(ArrayList bagA, ArrayList bagB, ArrayList bagC) {
            this.blackBags.add(bagA);
            this.blackBags.add(bagB);
            this.blackBags.add(bagC);
            for (int i = 0; i < 3; i++) {
                this.whiteBags.add(new ArrayList<>());
            }
        }

         /**
          * This method chooses a random marble from a random black bag and removes it from said black bag while storing the marble in a final variable
          * @return
          */
         public synchronized int[] getBlackBagPebble(){
             int[] pebbleAndBag = new int[2];
             //change to regular random no longer multithreaded
             int bagNum = random.nextInt(3);
             //initializing variables for use inside switch
             int pebbleIndex = random.nextInt(bags.getBlackBag(bagNum).size());
             final int blackBagPebble = (int) bags.getBlackBag(bagNum).get(pebbleIndex);
             bags.getBlackBag(bagNum).remove(pebbleIndex);
             if (bags.getBlackBag(bagNum).size() < 1){
                 emptyWhiteBag(bagNum);
             }
             pebbleAndBag[0] = blackBagPebble;
             pebbleAndBag[1] = bagNum;

             return pebbleAndBag;
         }

         public ArrayList getBlackBag(int bagIndex){
             return this.blackBags.get(bagIndex);
         }

        public ArrayList getWhiteBag(int bagIndex){
            return this.whiteBags.get(bagIndex);
        }

        //puts the white bag's contents into the corresponding black bag
        public synchronized void emptyWhiteBag(int bagIndex){
            for (int i = 0; i < this.getWhiteBag(bagIndex).size(); i++){
                this.getBlackBag(bagIndex).add(this.getWhiteBag(bagIndex).get(i));
            }
            this.getWhiteBag(bagIndex).clear();
        }

        public synchronized void addToWhiteBag(int marble, int bagIndex){
            this.getWhiteBag(bagIndex).add(marble);
        }
    }

    class Player implements Runnable{

        public Player(){
        }

        ArrayList<Integer> playerBag = new ArrayList<>();

        public void pick(){
            //uses thread safe way to determine random number to determine from which black bag a marble will be removed from
            //initializing variables for use inside switch
            int marbleIndex;
            int playerBagPebbleIndex;
            int playerBagPebble;

            int[] pebbleAndBag = bags.getBlackBagPebble();


            //System.out.println("Picking called");
            synchronized (this){
                playerBagPebbleIndex = ThreadLocalRandom.current().nextInt(0, 10);
                playerBagPebble = playerBag.get(playerBagPebbleIndex);
                playerBag.remove(playerBagPebbleIndex);
                bags.addToWhiteBag(playerBagPebble, pebbleAndBag[1]);
            }

            //Picks marble from black bag, stores it in final variable marble and checks if black bag is empty
            final int addToPlayerBagPebble = pebbleAndBag[0];
            playerBag.add(addToPlayerBagPebble);
        }

        public boolean checker(){
            boolean checking = false;

            int bagSize = playerBag.size();
            int total = 0;
            for (int i = 0; i < playerBag.size(); i++){
                total += playerBag.get(i);
            }
            //System.out.println("This is the total of the current player bag " + total);

            if (total == 100){
                checking = true;
            }

            return checking;
        }

        /**
         * used to start the pebble game and
         */
        public synchronized void start(){
            try {
                for (int i = 0; i < 10; i++) {
                    int startPebble;
                    startPebble = bags.getBlackBagPebble()[0];
                    playerBag.add(startPebble);
                }
                boolean checking = checker();
                System.out.println("Winner check: " + checking);
                if (checking) {
                    System.out.println("Player " + Thread.currentThread().getName() + " has won!");
                    System.exit(0);
                } else {
                    System.out.println("game starts");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public synchronized void run(){
            synchronized (this){

                try{
                    //Start is responsible for initiating the player bags

                    start();
                    boolean checking = false;

                    while (!checking){
                        try{
                            pick();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        checking = checker();
                    }
                    System.out.println("Player " + Thread.currentThread().getName() + " has won!");
                    System.out.println(playerBag);
                    System.exit(0);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
