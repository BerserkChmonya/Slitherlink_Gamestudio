package sk.tuke.gamestudio.game.slitherlink.core;

public class Cell {
    private int clue;
    //0-left, 1-top, 2-right, 3-bottom
    private final int[] lines;
    private CellState state;
    public Cell(int clue){
        this.clue = clue;
        lines = new int[4];
        state = (clue == 0)? CellState.CLOSED: CellState.OPENED;
    }

    public int getClue() {
        return clue;
    }
    public void setClue(int clue) {
        this.clue = clue;
        if(clue==0) state = CellState.CLOSED;
    }

    public CellState getState() {
        return state;
    }

    public int[] getLines() {
        return lines;
    }

    public boolean set_Line(int line_num) {
        if(line_num<0 || line_num>=4) return false;
        if(state == CellState.CLOSED) {
            return false;
        }
        if(lines[line_num] == 1) {
            return false;
        }
        if(this.clue == -1) {
            lines[line_num] = 1;
        }
        else {
            int linesCount = 0;
            for (int i = 0; i < 4; i++) {
                if (this.lines[i] == 1)
                    linesCount++;
            }
            lines[line_num] = 1;
            if (linesCount + 1 == clue)
                state = CellState.CLOSED;
        }
        return true;
    }

    @Override
    public String toString() {
        return Integer.toString(clue);
    }
}
