package version_1;

import version_1.Exceptions.WrongMoveException;

import java.util.Scanner;

public class Main {

    static Scanner scanner;
    static Board board;
    static AI ai;

    static int ai_p = 1;

    public static void main(String []args) {

//        for(int i=0; i<20; i++)
//            System.out.print((int) (Math.random()*5) + "-");
//        System.out.println();

        scanner = new Scanner(System.in);
        board = new Board();
        ai = new AI(board, ai_p);

        System.out.println("Welcome!\n");
        System.out.println("up-left is tile 00, down-right is tile 22, jflyk\n Good Luck!\n\n");

        starting_menu();

        while(game()) {
            System.out.println("\n\nOkay, let's play!");
            ai_p = 3-ai_p;
            System.out.println("You play as " + (3-ai_p));
            board = new Board();
            ai = new AI(board, ai_p);
        }
    }

    public static void starting_menu(){
        System.out.print("\nwho should start?\n\t1) version_1.AI\n\t2) You\nChoose: ");
        String starting_p = scanner.nextLine();

        while(!starting_p.matches("[1,2]")) {
            System.out.println("It is not so hard you jerk, just type '1' or '2'");
            System.out.print("C'mon, choose: ");
            starting_p = scanner.nextLine();
        }

        ai_p = (starting_p.charAt(0) - '0');
    }

    public static boolean game(){
        board.cliPrint();
        while (true){
            p1Move();
            board.cliPrint();
            if(endGame())
                break;
            p2Move();
            board.cliPrint();
            if(endGame())
                break;
        }

        String choice;
        System.out.println("\n\n\ndo you want to play again? [Y/N]");
        choice = scanner.nextLine();
        while(!choice.toLowerCase().matches("[y,n]")) {
            System.out.print("Didn't understand, please repeat [Y/N]: ");
            choice = scanner.nextLine();
        }

        return choice.toLowerCase().charAt(0) == 'y';
    }

    public static boolean endGame(){
        int winner=0;

        switch(board.checkWins()){
            case -1:
                return false;

            case 0:
                System.out.println("Tic Tac TIE!");
                break;

            case 1:
                winner=1;
                break;

            case 2:
                winner=2;
                break;
        }

        if(winner == ai_p)
            System.out.println("I won this election by a lot!");
        else if(winner == 3-ai_p)
            System.out.println("yuppy-doo, who won's not you!");

        return true;
    }

    public static void p1Move(){
        if(ai_p == 1)
            aiMove();
        else
            humanMove();
    }

    public static void p2Move(){
        if(ai_p == 2)
            aiMove();
        else
            humanMove();
    }

    public static void humanMove(){
        String move = cliMove();
        int x = move.charAt(0)-'0';
        int y = move.charAt(1)-'0';

        try {
            board.move(3-ai_p, x, y);
        } catch (WrongMoveException e) {
            System.out.println("CAnnot do that move, try with another");
            humanMove();
        }
    }

    private static void aiMove() {
//        return randomMove();
        Pair<Integer,Integer> move = ai.move();
        try {
            board.move(ai_p, move.getFirst(), move.getSecond());
        } catch (WrongMoveException e) {
            System.out.println("This shouldn't happen");
        }
    }

    private static void randomMove() {
        //version_1.AI involved
        double x = Math.random()*3;
        double y = Math.random()*3;
        try {
            board.move(ai_p, (int)x, (int)y);
        } catch (WrongMoveException e) {
            randomMove();
        }
    }

    public static String cliMove(){
        String input="";
        System.out.print("Your move: ");
        input = scanner.nextLine();
        while(!input.matches("[0-2][0-2]")) {
            System.out.println("PLis write just two numbers bw 0 and 2\n e.g. 11,10,01,20,22,...");
            System.out.print("Your move: ");
            input = scanner.nextLine();
        }

        return input;
    }
}
