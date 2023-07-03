package version_2;

import java.util.Set;

public class State {
    private Board board;
    private Set<Integer> actions;
    private Set<Integer> actions_not_av;

    public State(Board board, Set<Integer> actions, Set<Integer> actions_not_av) {
        this.board = board;
        this.actions = actions;
        this.actions_not_av = actions_not_av;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Set<Integer> getActions() {
        return actions;
    }

    public void setActions(Set<Integer> actions) {
        this.actions = actions;
    }

    public Set<Integer> getActions_not_av() {
        return actions_not_av;
    }

    public void setActions_not_av(Set<Integer> actions_not_av) {
        this.actions_not_av = actions_not_av;
    }
}
