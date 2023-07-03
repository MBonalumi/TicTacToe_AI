package version_2;

import java.util.HashSet;
import java.util.Set;

public class Board {

    private int size;
    private int winSize;

    private int[][] boardMap;
    private Set<Integer> availableMoves;

    private boolean isTerminal;     //false if not over, true if win/lose/tie
    private int outcome;    //0 is nothing, -1 is a tie, #P is winner player


    public void initialize(int size, int winSize){
        this.size = size;
        this.winSize = winSize;

        isTerminal = false;

        availableMoves = new HashSet<>();
        boardMap = new int[size][size];

        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++) {
                boardMap[i][j] = 0;
                availableMoves.add(i*size + j);
            }
        }
    }
    public Board(int size, int winSize) { initialize(size, winSize); }
    public Board(int size) { initialize(size, size); }
    public Board() { initialize(3, 3); }
    public Board(Board board) {
        this.size = board.size;
        this.winSize = board.winSize;
        this.availableMoves = new HashSet<>(board.availableMoves);
        this.boardMap = board.getBoardMap().clone();
    }


    public int move(int playerValue, int i, int j) {
        if(boardMap[i][j] != 0)
            return -1;

        boardMap[i][j] = playerValue;
        availableMoves.remove(i*size + j);

        outcome = checkWin(i,j);
        return outcome;
    }
    public int move(int playerValue, int move) {
        return move(playerValue, move/size, move%size);
    }

    public int undo_move(int i, int j) {
        if(boardMap[i][j] == 0)
            return -1;

        boardMap[i][j] = 0;
        availableMoves.add(i*size + j);

        //TODO: or maybe just outcome = 0;
        outcome = checkWin(i,j);
        return outcome;
    }
    public int undo_move(int move) {
        return undo_move(move/size, move%size);
    }

    public boolean is_move_feasible(int i, int j) { return boardMap[i][j]==0; }
    public boolean is_move_feasible(int move) { return is_move_feasible(move/size, move%size); }

    public int checkWin(int i, int j) {
        isTerminal = true; //sets it to true in advance, before returning 0 sets it back to false

        //0 -> nothing,   -1 -> tie,   #P -> win of player P (either 1 or 2)
        int x = boardMap[i][j];

        //___ checkRow + checkCol ___
        int row_eq=0, col_eq=0; //in row or in column
        for(int k=0; k<size; k++) {
            if (x == boardMap[i][k]) row_eq ++;
            else row_eq = 0;

            if (x == boardMap[k][j]) col_eq ++;
            else col_eq = 0;

            if (row_eq == winSize) return x;
            if (col_eq == winSize) return x;
        }

        //___ checkDiag ___
        /*
         * diag1 is like y=x, diag2 is like y=-x
         *
         * IDEA: for diag1,       min{up_edge_dist, right_edge_dist} + min{down_edge_dist, left_edge_dist} >= winSize
         *       for diag2 just invert coupling bw up/down and right/left
         */

        int r = (size-j) -1;     //-1 beacuse   (e.g.) i,j can be {0,1,2} while size is 3, so j=2 has nothing to the right
        int l = j;
        int d = (size-i) -1;
        int u = i;

        //KEEP IN MIND: using these values you're not considering the cell (i,j), so add 1

        int diag1_length = Math.min(l,u) + Math.min(r,d) + 1;
        int diag2_length = Math.min(r,u) + Math.min(l,d) + 1;

        if(diag1_length >= winSize) {
            int start_i1, start_j1;   //(i,j) upper,righter
            start_i1 = i - Math.min(i,j);
            start_j1 = j - Math.min(i,j);
            int diag1_eq=0;

            for(int k=0; k<diag1_length; k++) {
                if (x == boardMap[start_i1+k][start_j1+k]) diag1_eq ++;
                else diag1_eq = 0;

                if (diag1_eq == winSize) return x;
            }
        }
        if(diag2_length >= winSize) {
            int start_i2, start_j2;   //(i,j) upper,righter
            start_i2 = i + Math.min(size-i-1,j);
            start_j2 = j - Math.min(size-i-1,j);
            int diag2_eq=0;

            for(int k=0; k<diag2_length; k++) {
                if (x == boardMap[start_i2-k][start_j2+k]) diag2_eq ++;
                else diag2_eq = 0;

                if (diag2_eq == winSize) return x;
            }
        }

        //checkTie
        if(availableMoves.size() == 0)
            return -1;   //nobody has won and moves are over

        //else
        isTerminal = false;
        return 0;
    }

    /**
     *
     * @return the outcome of the game:
     * <p>  ->  '0'   if the state is not terminal</p>
     * <p>  ->  '-1'  if the state is a tie</p>
     * <p>  ->  '1'   if P1 is the winner</p>
     * <p>  ->  '2'   if P2 is the winner</p>
     */
    public int getOutcome() { return outcome; }
    public boolean isTerminal() { return isTerminal; }
    public int getSize() { return size; }
    public int getWinSize() { return winSize; }
    public int[][] getBoardMap() { return boardMap.clone(); }
    public Set<Integer> getAvailableMoves() { return new HashSet<Integer>(availableMoves); }
    /*
    * ANOTHER WAY
    * public HashSet<Integer> getAvailableMoves() { return (HashSet<Integer>) ((HashSet<Integer>) availableMoves).clone(); }
    */


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RED = "\u001B[31m";
    public void cliPrint() {
        String toPrint;

        System.out.print("  ");
        for(int j=0; j<size; j++){
            System.out.print("  "+j+" ");
        }
        System.out.println();

        for(int i=0; i<size; i++) {

            System.out.print("  ");
            for(int j=0; j<size; j++) System.out.print("+---");
            System.out.println("+");

            System.out.print(i+" ");
            for(int j=0; j<size; j++) {
                if(boardMap[i][j] == 0) toPrint = " ";
                else if(boardMap[i][j] == 1) toPrint = ANSI_CYAN + "X" + ANSI_RESET;
                else toPrint = ANSI_RED + "O" + ANSI_RESET;
                System.out.print("| " + toPrint + " ");
            }
            System.out.println("|");
        }

        System.out.print("  ");
        for(int j=0; j<size; j++) System.out.print("+---");
        System.out.println("+");
    }

}
