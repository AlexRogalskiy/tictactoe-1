package ris58h.tictactoe.exception;

public class GameNotFoundException extends RuntimeException {
    private final long gameId;

    public GameNotFoundException(long gameId) {
        this.gameId = gameId;
    }

    public long getGameId() {
        return gameId;
    }
}
