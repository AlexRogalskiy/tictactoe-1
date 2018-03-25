package ris58h.tictactoe;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameUtilsTest {

    @Test
    public void testDimension() {
        byte[] board = new byte[] {0,0,0, 0,0,0, 0,0,0};
        assertEquals(3, GameUtils.dimension(board));
    }

    @Test
    public void testOffset() {
        assertEquals(7, GameUtils.offset(3, 1, 2));
    }

    @Test
    public void testIndexOf() {
        byte[] board = new byte[] {0,0,0, 0,0,0, 1,0,0};
        assertEquals(6, GameUtils.indexOf(board, (byte) 1));
    }

    @Test
    public void isFinished() {
        byte[] nonFinishedBoard = new byte[] {0,0,0, 0,0,0, 1,0,0};
        assertFalse(GameUtils.isFinished(nonFinishedBoard));

        byte[] colFinishedBoard = new byte[] {1,0,0, 1,0,0, 1,0,0};
        assertTrue(GameUtils.isFinished(colFinishedBoard));

        byte[] rowFinishedBoard = new byte[] {0,0,0, 1,1,1, 0,0,0};
        assertTrue(GameUtils.isFinished(rowFinishedBoard));

        byte[] diagonal1FinishedBoard = new byte[] {1,0,0, 0,1,0, 0,0,1};
        assertTrue(GameUtils.isFinished(diagonal1FinishedBoard));

        byte[] diagonal2FinishedBoard = new byte[] {0,0,1, 0,1,0, 1,0,0};
        assertTrue(GameUtils.isFinished(diagonal2FinishedBoard));
    }
}