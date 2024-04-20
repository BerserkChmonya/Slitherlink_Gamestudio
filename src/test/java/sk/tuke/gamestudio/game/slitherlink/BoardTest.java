package sk.tuke.gamestudio.game.slitherlink;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.game.slitherlink.core.Board;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BoardTest {
    private final Board board;
    private final int size;

    public BoardTest(){
        Random random = new Random();
        size = random.nextInt(20) + 4;
        board = new Board(size);
    }

    @Test
    public void checkCellsCount(){
        int cellsCount = 0;
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                if(board.getField()[row][col].getClue() >= 0)
                    cellsCount++;
            }
        }
        int lowerBound = size*size/100*20;
        int upperBound = size*size/100*60;
        assertTrue(cellsCount > lowerBound && cellsCount < upperBound && cellsCount != 0);
    }

    @Test
    public void checkNoAdjacentCells(){
        assertFalse(board.hasAdjacentCells(3, 3) || board.hasAdjacentCells(3, 0));
    }
    @Test
    public void testSetLine() {
        int row;
        int col;
        do {
            row = new Random().nextInt(size);
            col = new Random().nextInt(size);
        }while(board.getField()[row][col].getClue() == 0);

        board.set_line(row, col, 0);

        assertEquals(1, board.getField()[row][col].getLines()[0]);
    }
}
