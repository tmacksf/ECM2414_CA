import java.util.Scanner;
import java.util.ArrayList;

public class PebbleGame {

    ArrayList<ArrayList> players = new ArrayList<>();

    //idk whats going on here
    public void threads(int pNum){
        for (int i = 0; i < pNum; i ++){
            ArrayList<String> playerinfo = new ArrayList<>();
            playerinfo.add("Player_" + String.valueOf(i));
            players.add(playerinfo);
        }
    }

    public static void main(String[] args){
        PebbleGame pg = new PebbleGame();
        String players;
        System.out.println("How many players?");
        Scanner sc = new Scanner(System.in);
        players = sc.nextLine();
        int playInt = Integer.valueOf(players);

        pg.threads(playInt);

        Read read = new Read();
        Write write = new Write(3);
    }
}
