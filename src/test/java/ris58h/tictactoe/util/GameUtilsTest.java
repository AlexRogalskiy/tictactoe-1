package ris58h.tictactoe.util;

import org.junit.Test;

import static org.junit.Assert.*;
import static ris58h.tictactoe.domain.GameState.*;

public class GameUtilsTest {

    @Test
    public void testDimension() {
        byte[] board = new byte[]{
                E, E, E,
                E, E, E,
                E, E, E
        };
        assertEquals(3, GameUtils.dimension(board));
    }

    @Test
    public void testEffset() {
        assertEquals(7, GameUtils.offset(3, 1, 2));
    }

    @Test
    public void testIndexEf() {
        byte[] board = new byte[]{
                E, E, E,
                E, E, E,
                X, E, E
        };
        assertEquals(6, GameUtils.indexOf(board, X));
    }

    @Test
    public void isFinished() {
        byte[] nonFinishedBoard = new byte[]{
                E, E, E,
                E, E, E,
                X, E, E
        };
        assertFalse(GameUtils.isFinished(nonFinishedBoard));

        byte[] colFinishedBoard = new byte[]{
                X, E, E,
                X, E, E,
                X, E, E
        };
        assertTrue(GameUtils.isFinished(colFinishedBoard));

        byte[] rowFinishedBoard = new byte[]{
                E, E, E,
                X, X, X,
                E, E, E
        };
        assertTrue(GameUtils.isFinished(rowFinishedBoard));

        byte[] diagonal1FinishedBoard = new byte[]{
                X, E, E,
                E, X, E,
                E, E, X
        };
        assertTrue(GameUtils.isFinished(diagonal1FinishedBoard));

        byte[] diagonal2FinishedBoard = new byte[]{
                E, E, X,
                E, X, E,
                X, E, E
        };
        assertTrue(GameUtils.isFinished(diagonal2FinishedBoard));
    }
}