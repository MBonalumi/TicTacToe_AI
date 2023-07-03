package version_2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AI_alphabeta {
    private final int CALCULATIONS = 200*1000*1000;
    private final int pos_INF = 999;
    private final int neg_INF = -999;

    Board board;
    int myPlayerID;  //either 1 or 2
    private int size;
    private int winSize;

    public AI_alphabeta() {}
    public AI_alphabeta(Board board, int size, int winSize){
        this();
        this.board = board;
        this.size = size;
        this.winSize = winSize;
    }
    public AI_alphabeta(Board board, int size, int winSize, int myPlayerID){
        this(board, size, winSize);
        this.myPlayerID = myPlayerID;
    }

    public int makeMove() {
        return alphabeta_search();
    }

    private int alphabeta_search() {
        State state = new State(new Board(board), board.getAvailableMoves(), new HashSet<>());

        int depth = calculate_depth(state.getActions().size(), 0);  //TODO: should be calculated based on the number of available moves

        Map<Integer,Integer> values = new HashMap<>();
        int value;
        int best_value = neg_INF;
        int best_action = -1;
        for(int a : state.getActions()) {
            state.getActions_not_av().add(a);
            state.getBoard().move(myPlayerID,a);
            value = min_value(state, neg_INF, pos_INF, depth, 0);
            state.getBoard().undo_move(a);
            state.getActions_not_av().remove(a);

            values.put(a, value);
            if(value >= best_value) { best_value = value; best_action = a; }
        }

        //TODO: remove debug prints
//        for(int a : state.getActions())
//            System.out.print(a+" ");
//        System.out.println();
        System.out.print("ALL MOVES: ");
        for(int i: values.keySet())
            System.out.print(i+":"+values.get(i)+"  ");
        System.out.println();
        System.out.print("BEST: ");
        for(int i: values.keySet())
            if(values.get(i)==best_value)
                System.out.print(i+":"+values.get(i)+"  ");
        System.out.println();

        return best_action;
    }

    private int calculate_depth(int moves, int depth) {

        if(depth==size*size)
            return depth;

        int calc=1;

        for(int i=moves; i>moves-depth; i--)
            calc*=i;

        if(calc < CALCULATIONS)
            return calculate_depth(moves, depth+1);

        System.out.println(depth-1+" -> "+calc/(moves-depth+1));
        return depth-1;
    }


    private int max_value(State state, int alpha, int beta, int max_depth, int depth) {
        if (state.getBoard().isTerminal()) {
            int outcome = state.getBoard().getOutcome();
            return outcome==-1 ? 0 : outcome==myPlayerID ? pos_INF : neg_INF;       //translates b.outcome: -1 lose, 0 tie, 1 win
        }
        if (depth >= max_depth)
            return heuristic(state);

        int v = neg_INF;

        int v_next;
        for(int a : state.getActions()) {
            if(state.getActions_not_av().contains(a)) continue;

            state.getActions_not_av().add(a);
            state.getBoard().move(myPlayerID, a);
            v_next = min_value(state, alpha, beta, max_depth, depth+1);
            state.getBoard().undo_move(a);
            state.getActions_not_av().remove(a);

            if(v_next > v) v=v_next;

            if(v >= beta) return v;

            if(v > alpha) alpha = v;
        }

        return v;
    }

    private int min_value(State state, int alpha, int beta, int max_depth, int depth) {
        if (state.getBoard().isTerminal()) {
            int outcome = state.getBoard().getOutcome();
            return outcome==-1 ? 0 : outcome==myPlayerID ? pos_INF : neg_INF;       //translates b.outcome: neg_INF lose, 0 tie, pos_INF win
        }
        if (depth >= max_depth)
            return heuristic(state);

        int v = pos_INF;

        int v_next;
        for(int a : state.getActions()) {
            if(state.getActions_not_av().contains(a)) continue;

            state.getActions_not_av().add(a);
            state.getBoard().move(3-myPlayerID, a);
            v_next = max_value(state, alpha, beta, max_depth, depth+1);
            state.getBoard().undo_move(a);
            state.getActions_not_av().remove(a);

            if(v_next < v) v = v_next;

            if(v <= alpha) return v;

            if(v < beta) beta = v;
        }

        return v;
    }

    private int heuristic(State state){ return heuristic(state.getBoard().getBoardMap()); }
    public int heuristic(Board board){ return heuristic(board.getBoardMap()); }
    private int heuristic(int[][] b) {
        int max_value=0;
        int min_value=0;

        int row_count_1=0, col_count_1=0;
        int row_count_2=0, col_count_2=0;
        int me=myPlayerID, other=3-myPlayerID;

        //diagonals stuff
        int centre = (int) (size-1)/2;
        int u=centre, d=size-1-centre, r, l;    //directions to count diagonal length
        int length1, length2, start_i, start_j;
        int diag1_count_1=0, diag2_count_1=0, diag1_count_2=0, diag2_count_2=0;

        /*
         * counts the non-other-player cells,
         * if the count is winsize or more
         * it means on that row i can win one more time
         *
         * code does this for each row/column for each player (4 controls)
         */
        for(int h=0; h<size; h++) {
            row_count_1=0; col_count_1=0;
            row_count_2=0; col_count_2=0;
            diag1_count_1=0; diag2_count_1=0;
            diag1_count_2=0; diag2_count_2=0;

            for(int k=0; k<size; k++) {
                // --MAX player (me) count
                // --row
                if(b[h][k] != other) {
                    row_count_1++;
                    if(row_count_1 >= winSize) max_value++;
                }
                else row_count_1 = 0;
                // --column
                if(b[k][h] != other) {
                    col_count_1++;
                    if(col_count_1 >= winSize) max_value++;
                }
                else col_count_1 = 0;

                // --MIN player (other) count
                // --row
                if(b[h][k] != me) {
                    row_count_2++;
                    if(row_count_2 >= winSize) min_value++;
                }
                else row_count_2 = 0;
                // --column
                if(b[k][h] != me) {
                    col_count_2++;
                    if(col_count_2 >= winSize) min_value++;
                }
                else col_count_2 = 0;
            }

            //diagonals
            r = size-1-h;
            l = h;

            //diag1
            length1 = Math.min(u,l)+Math.min(d,r)+1;
            if(length1 >= winSize) {
                start_i = centre - Math.min(u,l);
                start_j = h - Math.min(u,l);
                for(int k=0; k<length1; k++) {
                    //me
                    if(b[start_i+k][start_j+k] != other) {
                        diag1_count_1++;
                        if(diag1_count_1 >= winSize) max_value++;
                    }
                    else diag1_count_1=0;

                    //other
                    if(b[start_i+k][start_j+k] != me) {
                        diag1_count_2++;
                        if(diag1_count_2 >= winSize) min_value++;
                    }
                    else diag1_count_2=0;

                }
            }

            //direction u,r + d,l - diag2
            length2 = Math.min(u,r)+Math.min(d,l)+1;
            if(length2 >= winSize) {
                start_i = centre + Math.min(d,l);
                start_j = h - Math.min(d,l);
                for (int k = 0; k < length2; k++) {
                    //me
                    if(b[start_i-k][start_j+k] != other) {
                        diag2_count_1++;
                        if(diag2_count_1 >= winSize) max_value++;
                    }
                    else diag2_count_1 = 0;

                    //other
                    if(b[start_i-k][start_j+k] != me) {
                        diag2_count_2++;
                        if(diag2_count_2 >= winSize) min_value++;
                    }
                    else diag2_count_2 = 0;
                }
            }

        }

        return max_value-min_value;
    }

    public int hint() {
        myPlayerID = 3-myPlayerID;
        int hint_value = alphabeta_search();
        myPlayerID = 3-myPlayerID;

        return hint_value;
    }
}
