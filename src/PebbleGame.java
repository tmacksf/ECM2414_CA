import java.io.File;
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

        ArrayList<Integer> bagA;
        ArrayList<Integer> bagB;
        ArrayList<Integer> bagC;

        public void addBags(ArrayList bagA, ArrayList bagB, ArrayList bagC){
            this.bagA = bagA;
            this.bagB = bagB;
            this.bagC = bagC;
        }

        ArrayList<Integer> bagX = new ArrayList<>();
        ArrayList<Integer> bagY = new ArrayList<>();
        ArrayList<Integer> bagZ = new ArrayList<>();

        //gets the pebble with a given index and bag number and then removes it from bag
        /*public int getPebble(int blackBag, int pebbleIndex){
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
        }*/

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
                for (int i = 0; i < bagX.size(); i++){
                    bagA.add(bagX.get(i));
                }

                bagX.clear();
            }
            else if (bagIndex == 1){
                for (int i = 0; i < bagY.size(); i++){
                    bagB.add(bagY.get(i));
                }
                bagY.clear();
            }
            else if (bagIndex == 2){
                for (int i = 0; i < bagZ.size(); i++){
                    bagC.add(bagZ.get(i));
                }
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

        public Player(){
        }

        ArrayList<Integer> playerBag = new ArrayList<>();
        //Bags bags = new Bags(bagA, bagB, bagC);

        public synchronized int pick() throws InterruptedException{
            //uses thread safe way to determine random number to determine from which black bag a marble will be removed from
            int bagNum = ThreadLocalRandom.current().nextInt(0,3);
            //initializing variables for use inside switch
            int marbleIndex;
            int marble = 0;
            int removeIndex;
            int marbleRemove = 0;

            //System.out.println("Picking called");
            //System.out.println("Player bag: " + playerBag);


            switch (bagNum) {
                case 0:
                    //Get marble from player by index
                    removeIndex = ThreadLocalRandom.current().nextInt(0,playerBag.size());
                    marbleRemove = playerBag.get(removeIndex);
                    //Remove marble from player bag and add to white bag
                    playerBag.remove(removeIndex);
                    bags.addToWhiteBag(marbleRemove, 0);

                    //System.out.println(bags.bagX);

                    //Random marble to be added to player bag from black bag
                    marbleIndex = ThreadLocalRandom.current().nextInt(0, bags.bagA.size());
                    marble = bags.bagA.get(marbleIndex);

                    //Remove marble from black bag
                    bags.bagA.remove(marbleIndex);

                    if (bags.bagA.size() < 1){
                        bags.emptyWhiteBag(0);

                        //System.out.println("Bag X was emptied");
                    }
                    break;
                case 1:
                    //Get marble from player by index
                    removeIndex = ThreadLocalRandom.current().nextInt(0,playerBag.size());
                    marbleRemove = playerBag.get(removeIndex);
                    //Remove marble from player bag and add to white bag
                    playerBag.remove(removeIndex);
                    bags.addToWhiteBag(marbleRemove, 1);

                    //System.out.println(bags.bagY);

                    //Random marble to be added to player bag from black bag
                    marbleIndex = ThreadLocalRandom.current().nextInt(0, bags.bagB.size());
                    marble = bags.bagB.get(marbleIndex);
                    bags.bagB.remove(marbleIndex);

                    if (bags.bagB.size() < 1){
                        bags.emptyWhiteBag(1);

                        //System.out.println("Bag Y was emptied");
                    }
                    break;
                case 2:
                    //Get marble from player by index
                    removeIndex = ThreadLocalRandom.current().nextInt(0,playerBag.size());
                    marbleRemove = playerBag.get(removeIndex);
                    //Remove marble from player bag and add to white bag
                    playerBag.remove(removeIndex);
                    bags.addToWhiteBag(marbleRemove, 2);

                    //System.out.println(bags.bagZ);

                    //Random marble to be added to player bag from black bag
                    marbleIndex = ThreadLocalRandom.current().nextInt(0, bags.bagC.size());
                    marble = bags.bagC.get(marbleIndex);
                    bags.bagC.remove(marbleIndex);

                    if (bags.bagC.size() < 1){
                        bags.emptyWhiteBag(2);

                        //System.out.println("Bag Z was emptied");
                    }
                    break;
            }

            //puts marble into players bag
            //System.out.println("Marble to be added to player bag: " + marble);
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

            return result;
        }

        /**
         * used to start the pebble game and
         */
        public synchronized void start(){
            try {
                for (int i = 0; i < 10; i++) {
                    //choose random black bag
                    int bagNum = ThreadLocalRandom.current().nextInt(0, 3);
                    int marbleIndex;
                    int marble = 0;
                    switch (bagNum){
                        case 0: marbleIndex = ThreadLocalRandom.current().nextInt(0, bags.bagA.size());
                                marble = bags.bagA.get(marbleIndex);
                                bags.bagA.remove(marbleIndex);
                            break;
                        case 1: marbleIndex = ThreadLocalRandom.current().nextInt(0, bags.bagB.size());
                                marble = bags.bagB.get(marbleIndex);
                                bags.bagB.remove(marbleIndex);
                            break;
                        case 2: marbleIndex = ThreadLocalRandom.current().nextInt(0, bags.bagC.size());
                                marble = bags.bagC.get(marbleIndex);
                                bags.bagC.remove(marbleIndex);
                            break;
                    }
                    playerBag.add(marble);
                }
                boolean checking = checker();
                System.out.println("winner check: " + checking);
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
                //might only need the while or try not sure if both are needed
                try{
                    //Start is responsible for initiating the player bags

                    start();
                    boolean checking = false;
                    int correspondingWhiteBag =0;

                    while (!checking){

                        try{
                            pick();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        //int discardIndex = ThreadLocalRandom.current().nextInt(0,playerBag.size());
                        //deposit(discardIndex, correspondingWhiteBag);
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
