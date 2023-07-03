package version_1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AI {

    Board board;
    int p;  //either 1 or 2
    final int size = 9, size_b = 3;

    public AI() {}
    public AI(Board board){
        this();
        this.board = board;
    }
    public AI(Board board, int p){
        this(board);
        this.p = p;
    }

    private int eval(int[] s){
        return eval(s,p)-eval(s,3-p);
    }

    private int eval(int[] s, int pl){
        int adv = 3-pl;

        int diag1=0, diag2=0;
        Set<Integer> rows = new HashSet<>();
        Set<Integer> cols = new HashSet<>();
        for(int i=0; i<size_b; i++)
            for(int j=0; j<size_b; j++)
                if(s[i*3+j] == adv){
                    if(diag2==0 && i+j==2)
                        diag2=1;
                    if(diag1==0 && i==j)
                        diag1=1;
                    rows.add(i);
                    cols.add(j);
                }
        return 8-diag1-diag2-cols.size()-rows.size();
    }

    public Pair<Integer,Integer> move(){
        int b[][] = board.getBoard();
        int state[] = new int[size];
        for(int i=0; i<size; i++)
            state[i] = b[i/size_b][i%size_b];

//        printState(state);
        return minimax_decision(state);

//        int h = eval(board.getBoard());

    }

    private int utility(int state[]) {  //returns 1,2 if p1/p2 has won, 0 if draw, -1 if not terminal
        int res=0;

        for(int j=0; j<3 && res<=0; j++) {
            for(int i=0; i<3; i++)
                if(state[i*3+j] == 0)
                    res = -1;

            if (state[3*j]!=0 && state[3*j]==state[3*j+1] && state[3*j]==state[3*j+2])  res=state[3*j];
            if (state[j]!=0 && state[j]==state[3+j] && state[j]==state[(2*3)+j])  res=state[j];
            if(j==0)
                if (state[0]!=0 && state[0]==state[4] && state[4]==state[8])  res=state[0];
            if(j==2)
                if (state[2]!=0 && state[2]==state[4] && state[4]==state[6])  res=state[2];
        }

        return res;
    }

    private Pair<Integer, Integer> minimax_decision(int[] state){
        int[] r = new int[state.length];
        System.arraycopy(state, 0, r, 0, state.length);

        ArrayList<Integer> actions = new ArrayList<>();
        for(int i=0; i<9; i++)
            if(r[i] == 0)
                actions.add(i);

//        System.out.println(actions.size());

        ArrayList<Integer> best_acts = new ArrayList<>();
        int best_v=-10, best_a=-1;
        int val;
        int val1;

        for(int action : actions){
            r[action] = p;
            val1 = utility(r);
            if(val1==p){
                printState(r);
                return new Pair<>(action/3, action%3);
            }
            val = min_decision(r);
            if(val > best_v) {
                best_v = val;
                best_a = action;
                best_acts.clear();
            }
            if(val == best_v){
                best_acts.add(action);
            }
            r[action] = 0;
        }

        ArrayList<Integer> real_best_acts = new ArrayList<>();
        int x, maxX=-100;
        for(int act : best_acts){
            r[act] = p;
            x = eval(r);
            if(x>maxX){
                maxX = x;
                real_best_acts.clear();
            }
            if(x==maxX) {
                real_best_acts.add(act);
            }
            r[act] = 0;
        }

        if(real_best_acts.size() != 0) {
            int rand = (int) (Math.random() * real_best_acts.size());
            best_a = real_best_acts.get(rand);
        }

//        for(int act : real_best_acts)
//            System.out.print(act + " ");
//        System.out.println();

        return new Pair<>(best_a/3,best_a%3);
        //evaluates positions, calling max_decision


    }

    private int min_decision(int[] state) {
        int[] r = new int[state.length];
        System.arraycopy(state, 0, r, 0, state.length);

        ArrayList<Integer> actions = new ArrayList<>();
        for(int i=0; i<9; i++)
            if(r[i] == 0)
                actions.add(i);

        if(actions.size() == 0) {
            return 0;
        }

        int best_v=10;
        int val;
        int val1;

        for(int action : actions){
            r[action] = 3-p;
            val1=utility(r);
            if(val1==3-p){
                return -99;
            }
            val = max_decision(r);
            if(val < best_v) {
                best_v = val;
            }
            r[action] = 0;
        }

        return best_v;
    }

    private int max_decision(int[] state) {
        int[] r = new int[state.length];
        System.arraycopy(state, 0, r, 0, state.length);

        ArrayList<Integer> actions = new ArrayList<>();
        for(int i=0; i<9; i++)
            if(r[i] == 0)
                actions.add(i);

        if(actions.size() == 0) {
            return 0;
        }

        int best_v=-10;
        int val;
        int val1;

        for(int action : actions){
            r[action] = p;
            val1=utility(r);
            if(val1==p){
                return 100;
            }
            val = min_decision(r);
            if(val > best_v) {
                best_v = val;
            }
            r[action] = 0;
        }

        return best_v;
    }

    public void printState(int[] s) {
        for(int i=0; i<s.length; i++)
            System.out.print(s[i]);

        System.out.println();
    }
}
