package pebblegame;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.Random;

public class PebbleGame{

    //creates an instance of the bags class which is universally accessible
    //Bags bags = new Bags();

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

        //Input for number of players which covers cases if there are fewer than 1 player or if input is negative
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

        //Input for files is stored in an arraylist for easy access
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
                    }
                    if (temp.size() > 11*numPlayers) fileFlag = true;
                    for (int j = 0; j < temp.size(); j++){
                        if (temp.get(j) < 0) {
                            fileFlag = false;
                            break;
                        }
                    }
                    if (fileFlag) fileInfo.add(temp);
                } catch (Exception e) {
                    if (fileName.equals("E")) System.exit(0);
                }
            }

        }

        //creating the bags which are then added to the bags class
        ArrayList<Integer> bagA = fileInfo.get(0);
        ArrayList<Integer> bagB = fileInfo.get(1);
        ArrayList<Integer> bagC = fileInfo.get(2);

        PebbleGame.Bags bags = pg.new Bags();

        bags.addBags(bagA, bagB, bagC);

        //creates a threadpool for execution
        ExecutorService threadPool = Executors.newFixedThreadPool(numPlayers);

        //adds players to the threadpool based on the number of players then executes
        for (int i = 0; i < numPlayers; i ++) {
            System.out.println("Player " + i + " has started");
            Runnable player = pg.new Player(bags, i+1);
            threadPool.execute(player);
        }

    }

     class Bags{
         Random random = new Random();

         ArrayList<ArrayList> blackBags = new ArrayList<>();
         ArrayList<ArrayList> whiteBags = new ArrayList<>();

         /**
          * This method is to add bags to the bags class and to the black bags arraylist
          * @param bagA The first bag
          * @param bagB The second bag
          * @param bagC The third bag
          */
         public void addBags(ArrayList bagA, ArrayList bagB, ArrayList bagC) {
             this.blackBags.add(bagA);
             this.blackBags.add(bagB);
             this.blackBags.add(bagC);
             for (int i = 0; i < 3; i++) {
                 this.whiteBags.add(new ArrayList<>());
             }
         }

         /**
          * This method chooses a random pebble from a random black bag and removes it from said black bag while storing the pebble in a final variable
          * @return an array containing the pebble value and the black bag that it was pulled from
          */
         public synchronized int[] getBlackBagPebble(){
             int[] pebbleAndBag = new int[2];
             //gets a random number between 0 and 2 to decide what bag to pick from
             int bagNum = random.nextInt(3);

             //the pebble is selected here and then removed from the black bag
             int pebbleIndex = random.nextInt(getBlackBag(bagNum).size());
             final int blackBagPebble = (int) getBlackBag(bagNum).get(pebbleIndex);
             getBlackBag(bagNum).remove(pebbleIndex);
             //checks if the black bag is empty then refills its
             if (getBlackBag(bagNum).size() < 1){
                 emptyWhiteBag(bagNum);
             }
             //adding the value of the pebble and the bag number to the array which is returned so both can be accessed by other methods
             pebbleAndBag[0] = blackBagPebble;
             pebbleAndBag[1] = bagNum;

             return pebbleAndBag;
         }

         /**
          * method to get a black bag using the index of the bag
          * @param bagIndex the index of the black bag within the blackBags arraylist
          * @return the Arraylist of the black bag specified
          */
         public ArrayList getBlackBag(int bagIndex){
             return this.blackBags.get(bagIndex);
         }

         /**
          * method to get a white bag using the index of the bag
          * @param bagIndex the index of the white bag within the whiteBags arraylist
          * @return the arraylist of the white bag specified
          */
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

         /**
          * method to add a pebble to a white bag after it has been discarded from the players hand
          * @param pebble the value of the pebble
          * @param bagIndex the corresponding which bag to the black bag which has been accessed
          */
         public synchronized void addToWhiteBag(int pebble, int bagIndex){
            this.getWhiteBag(bagIndex).add(pebble);
        }
     }

    class Player implements Runnable{

        //a player id which is input when the players are run for ease of output
        int playerId;
        Bags bags;

        public Player(Bags bags, int playerId){
            this.playerId = playerId;
            this.bags = bags;
        }

        //the player's bag of pebbles
        ArrayList<Integer> playerBag = new ArrayList<>();

        /**
         * method to pick a random pebble from a random black bag
         */
        public synchronized void pick(){
            //initialize variables for use later
            int playerBagPebbleIndex;
            int playerBagPebble;

            //an array to store the random pebble and its black bag index
            int[] pebbleAndBag = bags.getBlackBagPebble();

            //disposing of random pebble in the playerbag
            synchronized (this){
                //Threadsafe random is used here to avoid multithreading issues
                playerBagPebbleIndex = ThreadLocalRandom.current().nextInt(0, 10);
                playerBagPebble = playerBag.get(playerBagPebbleIndex);
                playerBag.remove(playerBagPebbleIndex);
                bags.addToWhiteBag(playerBagPebble, pebbleAndBag[1]);
            }

            //Picks pebble from black bag, stores it in final variable pebble and checks if black bag is empty
            final int addToPlayerBagPebble = pebbleAndBag[0];
            playerBag.add(addToPlayerBagPebble);

            String outputMessage = "player " + playerId + " has picked " + pebbleAndBag[0] + " from bag " + pebbleAndBag[1] + "\n";
            String hand = "player" + playerId + " hand is " + playerBag + "\n";

            String playerOutputFile = "player_" + playerId + "_output.txt";

            //writes to the output file
            try{
                FileWriter writer = new FileWriter(playerOutputFile, true);
                writer.write(outputMessage);
                writer.write(hand);
                writer.close();
            }
            catch (Exception e){

            }
        }

        /**
         * method to check the sum of the player bag
         * @return true if it is 100 and false otherwise
         */
        public boolean checker(){
            boolean checking = false;

            int bagSize = playerBag.size();
            int total = 0;
            for (int i = 0; i < playerBag.size(); i++){
                total += playerBag.get(i);
            }

            if (total == 100){
                checking = true;
            }

            return checking;
        }

        /**
         * method to start the game and check if the player has won on the starting hand
         */
        public synchronized void start(){
            String playerOutputFile = "player_" + playerId + "_output.txt";
            try {
                //draws 10 pebbles from the black bags
                for (int i = 0; i < 10; i++) {
                    int startPebble;
                    //uses the getBlackBagPebble to avoid threading issues
                    int[] startPebbleArr = bags.getBlackBagPebble();
                    startPebble = startPebbleArr[0];
                    playerBag.add(startPebble);

                    //the output message for the player
                    String outputMessage = "player" + playerId + " has picked " + startPebble + " from bag " + startPebbleArr[1] + "\n";
                    String hand = "player " + playerId + " hand is " + playerBag + "\n";
                    //creates the player output file if this is the first pebble pulled
                    if (i == 0){
                        try{
                            System.out.println("New file created with name: " + playerOutputFile);
                            FileWriter writer = new FileWriter(playerOutputFile);
                            writer.write(outputMessage);
                            writer.write(hand);
                            writer.close();
                        }catch (Exception e){

                        }
                    }
                    //else appends to the player output file
                    else{
                        try{
                            FileWriter writer = new FileWriter(playerOutputFile, true);
                            writer.write(outputMessage);
                            writer.write(hand);
                            writer.close();
                        }
                        catch (Exception e){

                        }
                    }
                }
                //calls the checking method to see if the player has won
                boolean checking = checker();
                if (checking) {
                    System.out.println("Player " + playerId + " has won!");
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
        public void run(){
            synchronized (this){
                try{
                    //Start is responsible for initiating the player bags
                    start();
                    //sets checking to false if start did not get a win
                    boolean checking = false;
                    //while the player has not won this runs
                    while (!checking){
                        try{
                            //calls the pick method to get pebbles
                            pick();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        //sets checking to the output of checker
                        checking = checker();
                    }
                    System.out.println("Player " + playerId + " has won!");
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
