package sk.tuke.gamestudio.game.slitherlink.core;
import java.util.Random;

public class Board {
    public Cell[][] field;
    private final int size;
    private final int[][] visited;
    private GameState state = GameState.PLAYING;
    public Board(int size){
        this.size = size;
        visited = generateVisitedField();
        float Size = (float) size;
        // generate until not get a normal field
        do {
            field = generateBoard();
        }
        while (cellsCount() <= (int)((Size*Size)/100*20) || cellsCount() >= (int)((Size*Size)/100*60) || hasAdjacentCells(3, 3) || hasAdjacentCells(3, 0));
    }

    public Cell[][] getField() {
        return field;
    }

    public void setField(Cell[][] field){
        this.field = field;
    }

    public boolean hasAdjacentCells(int clue1, int clue2) {
        //directions for adjacent cells
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}, {1, -1}, {-1, -1}};

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (field[row][col].getClue() == clue1) {
                    // Check adjacent cells
                    for (int[] dir : directions) {
                        int adjacentRow = row + dir[0];
                        int adjacentCol = col + dir[1];

                        if (isValid(adjacentRow, adjacentCol, size, size)) {
                            if (field[adjacentRow][adjacentCol].getClue() == clue2) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
    // generate visited field of cells and their spaces for lines near // It uses for algorithm to check if the field is solved
    private int[][] generateVisitedField() {
        int[][] field = new int[size*2+1][size*2+1];
        for(int row = 0; row < size*2+1; row++){
            for(int col = 0; col < size*2+1; col++){
                if(row%2 != 0 && col%2 != 0)
                    field[row][col] = -1;
                else
                    field[row][col] = 0;
            }
        }

        return field;
    }

    public boolean set_line(int row, int column, int line_num) {
        if(row > size || column > size || row < 0 || column < 0){
            return false;
        }
        if(field[row][column].set_Line(line_num)) {
            if (line_num == 0 && column != 0)
                field[row][column - 1].set_Line(2);
            else if (line_num == 1 && row != 0)
                field[row - 1][column].set_Line(3);
            else if (line_num == 2 && column != size - 1)
                field[row][column + 1].set_Line(0);
            else if (line_num == 3 && row != size - 1)
                field[row + 1][column].set_Line(1);

            int visitedRow = row*2+1 + ((line_num==1)?-1:(line_num==3)?1:0);
            int visitedCol = column*2+1 + ((line_num==0)?-1:(line_num==2)?1:0);
            visited[visitedRow][visitedCol] = 1;

            if(visitedRow-1 >=0 && visited[visitedRow-1][visitedCol] != -1) visited[visitedRow-1][visitedCol] = 1;
            if(visitedRow+1 < size*2+1 && visited[visitedRow+1][visitedCol] != -1) visited[visitedRow+1][visitedCol] = 1;
            if(visitedCol-1 >=0 && visited[visitedRow][visitedCol-1] != -1) visited[visitedRow][visitedCol-1] = 1;
            if(visitedCol+1 < size*2+1 && visited[visitedRow][visitedCol+1] != -1) visited[visitedRow][visitedCol+1] = 1;
            return true;
        }
        else return false;
    }

    private Cell[][] generateBoard() {
        Cell[][] field = new Cell[size][size];
        Random random = new Random();

        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                if(random.nextInt(4) == 0){
                    int num = random.nextInt(4);
                    field[row][col] = new Cell(num);
                }
                else field[row][col] = new Cell(-1);
            }
        }

        return field;
    }

    private int cellsCount() {
        int cellsCount = 0;
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                if(field[row][col].getClue() >= 0)
                    cellsCount++;
            }
        }
        return cellsCount;
    }
    // Uses a depth-first search algorithm to check if the field is solved or failed
    public void checkGameState() {
        boolean isCellsClosed = isAllClosed();
        int visitedSize = visited.length;
        int loopCount = 0;

        boolean[][] visit = new boolean[visitedSize][visitedSize];

        for (int vRow = 0; vRow < visitedSize; vRow++) {
            for (int vCol = 0; vCol < visitedSize; vCol++) {
                if (visited[vRow][vCol] == 1 && !visit[vRow][vCol] && dfs(visited, visit, vRow, vCol, -1, -1)) {
                    loopCount++;
                }
            }
        }
        if(hasBranch() || loopCount > 1 || (loopCount == 1 && !isCellsClosed)) state = GameState.FAILED;
        else if(loopCount == 1) {
            state = GameState.SOLVED;
        }
    }

    private boolean hasBranch() {
        int visitedSize = visited.length;

        for (int vRow = 0; vRow < visitedSize; vRow++) {
            for (int vCol = 0; vCol < visitedSize; vCol++) {
                // If the cell contains 1, check for neighbors
                if (visited[vRow][vCol] == 1) {
                    int count = countOnesAround(visited, vRow, vCol);
                    // If the count is higher than 2, it is a branch
                    if (count > 2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    // Count the number of 1s around the cell
    private int countOnesAround(int[][] grid, int row, int col) {
        int count = 0;
        int rows = grid.length;
        int cols = grid[0].length;

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = row + dr[i];
            int newCol = col + dc[i];

            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && grid[newRow][newCol] == 1) {
                count++;
            }
        }

        return count;
    }

    public static boolean dfs(int[][] grid, boolean[][] visited, int row, int col, int parentRow, int parentCol) {
        int rows = grid.length;
        int cols = grid[0].length;

        visited[row][col] = true;

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = row + dr[i];
            int newCol = col + dc[i];

            if (isValid(newRow, newCol, rows, cols) && grid[newRow][newCol] == 1) {
                if (!visited[newRow][newCol]) {
                    if (dfs(grid, visited, newRow, newCol, row, col))
                        return true;
                } else if (newRow != parentRow || newCol != parentCol) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isValid(int row, int col, int rows, int cols) {
        return row >= 0 && col >= 0 && row < rows && col < cols;
    }
    // check if all cells with clues are closed
    private boolean isAllClosed() {
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                Cell cell = field[row][col];
                if(cell.getClue()>0 && cell.getState() == CellState.OPENED)
                    return false;
            }
        }
        return true;
    }

    public GameState getState() {
        return state;
    }
}
