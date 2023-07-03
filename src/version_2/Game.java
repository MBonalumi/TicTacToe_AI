package version_2;

import java.util.Scanner;

public class Game {
    private int size;
    private int winSize;
    private boolean isHumanFirst;
    private int humanPlayerValue;
    private AI_alphabeta ai;
    private Board board;

    Scanner scanner;

    private void initialize() {
        scanner = new Scanner(System.in);

        System.out.print("board size: ");
        int size = Integer.parseInt(scanner.nextLine());
        System.out.print("winning cells_in_a_row size: ");
        int winSize = Integer.parseInt(scanner.nextLine());
        System.out.print("do you want to begin? [y/n]: ");
        boolean isHumanFirst = scanner.nextLine().matches("[y,Y]");

        //TODO: control if bad input happens

        this.size = size;
        this.winSize = winSize;
        this.isHumanFirst = isHumanFirst;
        board = new Board(size, winSize);

        ai = new AI_alphabeta(board,  size, winSize, isHumanFirst ? 2 : 1);
        humanPlayerValue = isHumanFirst ? 1 : 2;

        begin();
//        DEBUG();
    }

    public Game() {
        initialize();
    }

    private void DEBUG(){
        board.move(1,2);
        board.move(1,3);
        board.move(1,4);
        board.undo_move(4);
    }

    private void begin(){

        board.cliPrint();
        System.out.println("\n\n\n");

        while(! board.isTerminal()) {
            if(isHumanFirst) humanMove();
            else aiMove();
            if (isHumanFirst) System.out.println("you've moved!");
            else System.out.println("ai has moved!");


            board.cliPrint();

            System.out.println("\n\n\n");

            if(board.isTerminal()) break;

            if(isHumanFirst) aiMove();
            else humanMove();
            if(isHumanFirst) System.out.println("ai has moved!");
            else System.out.println("you've moved!");
            board.cliPrint();

            System.out.println("\n\n\n");
        }

        switch(board.getOutcome()){
            case 0:
                System.out.println("THERE'S A PROBLEM, THIS SHOULDN'T HAPPEN");
                break;

            case -1:
                System.out.println("Tic Tac TIE\n...\tyet another BORING tie");
                break;

            case 1:
                if(isHumanFirst) System.out.println("CONGRATULATIONS, I MUST'VE WRITTEN BAD CODE");
                else System.out.println("YOU SUCK AND MY CODE RULES");
                break;

            case 2:
                if(isHumanFirst) System.out.println("YOU SUCK AND MY CODE RULES");
                else System.out.println("CONGRATULATIONS, I MUST'VE WRITTEN BAD CODE");
                break;
        }



    }

    private void aiMove() {
        int move = ai.makeMove();
        board.move(3-humanPlayerValue, move);
    }

    private void humanMove() {
        int move = askMove();
        board.move(humanPlayerValue, move);
    }

    private int askMove() {
        String input="";
        System.out.print("Your move: ");
        input = scanner.nextLine();

        if(input.toLowerCase().equals("hint"))
            System.out.println("ai hint: play " + ai.hint());

        int x = input.charAt(0)-'0';
        int y = input.charAt(1)-'0';

        while(!input.matches("[0-9][0-9]") || !board.is_move_feasible(x,y)) {
            System.out.println("PLis write just two numbers bw 0 and "+size+"\n e.g. 11,10,01,20,22,...");
            System.out.print("Your move: ");
            input = scanner.nextLine();

            if(input.toLowerCase().equals("hint"))
                System.out.println("ai hint: play " + ai.hint());

            x = input.charAt(0)-'0';
            y = input.charAt(1)-'0';
        }

        return x*size+y;
    }


}
