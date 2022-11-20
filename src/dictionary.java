import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class dictionary
{
    private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
        String choice;

        while(true){
			System.out.println();
			System.out.println("1. Search Slang Word");
			System.out.println("2. Search Slang Word follow definition");
			System.out.println("3. Search History");
			System.out.println("4. Add new Slang Word");
			System.out.println("5. Edit Slang Word");
			System.out.println("6. Delete Slang Word");
			System.out.println("7. Reset Slang Word Dictionary");
            System.out.println("8. On this day Slang Word");
            System.out.println("9. Slang Word Quiz");
            System.out.println("10. Slang Word Quiz follow definition");
			System.out.println("Chose difference chosen to exit");
			System.out.print("Chose action: ");
			choice = sc.nextLine();

            if (choice.equals("1")){
                System.out.println(choice);
            }
            else if (choice.equals("2")){
                System.out.println(choice);
            }
            else if (choice.equals("3")){
                System.out.println(choice);
            }
            else if (choice.equals("4")){
                System.out.println(choice);
            }
            else if (choice.equals("5")){
                System.out.println(choice);
            }
            else if (choice.equals("6")){
                System.out.println(choice);
            }
            else if (choice.equals("7")){
                System.out.println(choice);
            }
            else if (choice.equals("8")){
                System.out.println(choice);
            }
            else if (choice.equals("9")){
                System.out.println(choice);
            }
            else if (choice.equals("10")){
                System.out.println(choice);
            }
            else {
                break;
            }
        }
    }
}
