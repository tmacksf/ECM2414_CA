import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class Read{

    private int playerNum;
    /*public Read(int playerNum){
        this.playerNum = playerNum;
    }*/

    ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<ArrayList> fileInfo = new ArrayList<>();

    /*
    Todo: Add file not found etc
     */
    public void readIn(){
        Scanner sc = new Scanner(System.in);

        for (int i = 0; i < 3; i ++){
            String line = sc.nextLine();
            fileNames.add(line);
        }
    }

    /*
    todo have to add messages for incorrect stuff, have to add error handling, etc
    */
    public void readFiles(){
        /*filenames.add("example_file_2.txt");
        filenames.add("example_file_2.txt");
        filenames.add("example_file_1.csv");*/

        for (int i = 0; i < 3; i++){
            System.out.println("Please enter name for file number " + i);
            ArrayList<String> list = new ArrayList<>();
            String file = fileNames.get(i);

            int counter = 0;

            try {
                File fn = new File(file);
                Scanner f = new Scanner(fn);
                while (f.hasNextLine()) {
                    counter ++;
                    list.add(f.nextLine());
                }
            }
            catch(FileNotFoundException e){
                e.printStackTrace();
            }

            if (counter / 3 < playerNum * 11){
                System.out.println("Too few marbles in bag " + String.valueOf(i));
                System.exit(1);
            }
            fileInfo.add(list);
        }
    }

    public ArrayList<ArrayList> getFileInfo() {
        return fileInfo;
    }


    /*public static void main(String[] args){
        Read rw = new Read();
        //rw.readIn();
        rw.readFiles();
        System.out.println(rw.getFileInfo());
        System.out.println(rw.fileInfo);
    }*/
}
