package ris58h.tictactoe;

import ris58h.tictactoe.domain.Game;

public class GameUtils {
    public static int dimension(byte[] board) {
        return (int) Math.sqrt(board.length);
    }

    public static int offset(int dimension, int x, int y) {
        return y * dimension + x;
    }

    public static int indexOf(final byte[] board, final byte state) {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == state) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isFinished(byte[] board) {
        int dimension = dimension(board);
        byte d1 = board[offset(dimension, 0, 0)];
        byte d2 = board[offset(dimension, 0, dimension - 1)];
        boolean d1finished = true;
        boolean d2finished = true;
        for (int i = 0; i < dimension; i++) {
            if (rowFinished(board, i) || columnFinished(board, i)) {
                return true;
            }
            byte d1v = board[offset(dimension, i, i)];
            if (d1v == Game.EMPTY || d1 != d1v) {
                d1finished = false;
            }
            byte d2v = board[offset(dimension, i, dimension - 1 - i)];
            if (d2v == Game.EMPTY || d2 != d2v) {
                d2finished = false;
            }
        }
        return d1finished || d2finished;
    }

    private static boolean rowFinished(byte[] board, int rowIndex) {
        int dimension = dimension(board);
        byte state = board[offset(dimension, 0, rowIndex)];
        if (state == Game.EMPTY) {
            return false;
        }
        for (int x = 1; x < dimension; x++) {
            if (state != board[offset(dimension, x, rowIndex)]) {
                return false;
            }
        }
        return true;
    }

    private static boolean columnFinished(byte[] board, int columnIndex) {
        int dimension = dimension(board);
        byte state = board[offset(dimension, columnIndex, 0)];
        if (state == Game.EMPTY) {
            return false;
        }
        for (int y = 1; y < dimension; y++) {
            if (state != board[offset(dimension, columnIndex, y)]) {
                return false;
            }
        }
        return true;
    }
}
