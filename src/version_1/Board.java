package version_1;

import version_1.Exceptions.WrongMoveException;

public class Board {
    private final int size = 3;

    private int board[][] = new int[size][size];

    public Board(){
        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
                board[i][j] = 0;
    }

    public int[][] getBoard() {
        return board;
    }

    public void move(int player, int x, int y) throws WrongMoveException {
        if(board[x][y] != 0)
            throw new WrongMoveException();

        board[x][y] = player;
    }

    public int checkWins() {
        int res=0;

        for(int i=0; i<3 && res<=0; i++) {
            for(int j=0; j<3; j++)
                if(board[i][j] == 0)
                    res = -1;

            if (board[i][0]!=0 && board[i][0]==board[i][1] && board[i][0]==board[i][2])  res=board[i][0];
            if (board[0][i]!=0 && board[0][i]==board[1][i] && board[0][i]==board[2][i])  res=board[0][i];
            if(i==0)
                if (board[0][0]!=0 && board[0][0]==board[1][1] && board[0][0]==board[2][2])  res=board[1][1];
            if(i==2)
                if (board[2][0]!=0 && board[2][0]==board[1][1] && board[2][0]==board[0][2])  res=board[1][1];
        }

        return res;
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RED = "\u001B[31m";
    public void cliPrint(){
        String toPrint;

        for(int i=0; i<size; i++) {
            System.out.println("+---+---+---+");
            for(int j=0; j<size; j++) {
                if(board[i][j] == 0) toPrint = " ";
                else if(board[i][j] == 1) toPrint = ANSI_CYAN + "X" + ANSI_RESET;
                else toPrint = ANSI_RED + "O" + ANSI_RESET;
                System.out.print("| " + toPrint + " ");
            }
            System.out.println("|");
        }
        System.out.println("+---+---+---+");
    }

}
