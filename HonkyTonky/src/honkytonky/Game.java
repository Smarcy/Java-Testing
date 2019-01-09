package honkytonky;

import honkytonky.factories.RoomFactory;
import honkytonky.objects.Player;
import honkytonky.objects.Room;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Game
{

    private final Scanner scanner = new Scanner(System.in);
    private final ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls").inheritIO();

    private Player player = null;
    private Room[][] roomList = new Room[10][10];

    private RoomFactory roomFactory = new RoomFactory();

    public static void main(String[] args) throws IOException, InterruptedException
    {
        Game game = new Game();
        game.showIntro();
    }

    /**
     * Shows the menu to choose an option
     */
    private void showIntro() throws IOException, InterruptedException
    {
        introLoop:
        while (true)
        {
            clearScreen();

            System.out.print(
              "Welcome to HonkyTonky!\n" +
                "Please choose an option:\n\n" +
                "1) Start Game\n" +
                "2) Create New Player\n"
            );

            int option = scanner.nextInt();

            switch (option)
            {
                case 1:
                    if (player != null)
                    {
                        clearScreen();
                        startGame();
                        break introLoop;
                    } else
                    {
                        System.out.println("Please create a new player first!");
                        TimeUnit.SECONDS.sleep(3);
                    }
                    break;
                case 2:
                    createPlayer();
                    break;
            }
        }
    }

    /**
     * Lets the user create a Player Object
     */
    private void createPlayer() throws IOException, InterruptedException
    {
        clearScreen();

        System.out.println("Enter your name: ");
        player = new Player(scanner.next(), 100, 0, 0);
    }

    private void startGame()
    {
        roomList = roomFactory.createRooms();

        while(true)
        {

            System.out.println("Choose an option:\n\n");

            System.out.println("1) Move");
            System.out.println("2) Where am I?");
            System.out.println("3) Exit Game");

            int option = scanner.nextInt();

            switch (option)
            {
                case 1:
                    clearScreen();
                    move();
                    break;
                case 2:
                    clearScreen();
                    System.out.println(""
                      + "x-Coordinate: " + player.getX()
                      + "\ny-Coordinate: " + player.getY()
                      + "\nCurrent room: " + roomList[player.getX()][player.getY()]
                      + "\n");
                    break;
                case 3:
                    System.exit(0);
            }
        }
    }

    private void move()
    {
            System.out.println("Where would you like to go?\n");
            System.out.println("\nType west, east, south or north\n");

            String direction = (scanner.next().trim().toLowerCase());

            switch (direction)
            {
                case "north":
                    player.setLocation(player.getX(), player.getY() + 1);
                    break;
                case "east":
                    player.setLocation(player.getX() + 1, player.getY());
                    break;
                case "south":
                    player.setLocation(player.getX(), player.getY() - 1);
                    break;
                case "west":
                    player.setLocation(player.getX() - 1, player.getY());
                    break;
                default:
                    System.out.println(direction + " is not a valid command!");
            }
    }

    private void clearScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}