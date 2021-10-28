import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class Write {

    private int playerNumber;
    ArrayList<String> playerFiles = new ArrayList<>();

    public Write(int playerNumber){
        this.playerNumber = playerNumber;
    }

    public void generator(){
        for (int i = 0; i < playerNumber; i ++){
            playerFiles.add("player" + String.valueOf(i+1) + "_output.txt");
        }
    }

    public void fileWriter(int index, String input){
        try{
            String playernum = playerFiles.get(index);
            File f = new File(playernum);
            if (f.createNewFile()){
                FileWriter writer = new FileWriter(playernum, true);
                writer.write(input);
                writer.close();
            }
            else{
                FileWriter writer = new FileWriter(playernum, true);
                writer.write(input);
                writer.close();
            }
        }
        catch (Exception e){

        }
    }

    public static void main(String[] args){
        Write w = new Write(3);
        w.generator();
        System.out.println(w.playerFiles);
        w.fileWriter(0, "This is a test for index 0 aka player 1\n");
    }

}