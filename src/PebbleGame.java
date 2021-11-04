import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.*;

public class PebbleGame{

    ArrayList<ArrayList> players = new ArrayList<>();

    public synchronized static void main(String[] args) {
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
    }
    static class Bags{

        //change to other input method
        ArrayList<Integer> bagA = new ArrayList<>();
        ArrayList<Integer> bagB = new ArrayList<>();
        ArrayList<Integer> bagC = new ArrayList<>();

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

    static class Player implements Runnable{

        ArrayList<Integer> playerBag = new ArrayList<>();
        Bags bags = new Bags();

        public void run(){
            Player player = new Player();
            //Bags bags = new Bags();
            try{
                int playerBag = 0;
                Thread thread = new Thread();
                thread.start();
                thread.join();
                while (playerBag != 100){

                }
            }
            catch(Exception e){

            }
        }


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

        public synchronized void deposit(int marble, int whiteBagIndex){
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

    }
}
