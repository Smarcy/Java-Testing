package honkytonky;

import honkytonky.objects.*;
import java.util.Scanner;

public class Main
{

    private static Player player;

    public static void main(String[] args)
    {
        showIntro();

    }

    private static void createPlayer()
    {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = reader.next();
        player = new Player(name);
        reader.close();
    }

    private static void showIntro()
    {
        System.out.print(
          "Welcome to HonkyTonky!\n" +
          "Please choose an option:\n" +
          "1) Create Player\n" +
          "2) Exit\n"
        );

        Scanner reader = new Scanner(System.in);
        int option = reader.nextInt();

        switch (option)
        {
            case 1:
                createPlayer();
                break;
        }
    }

}