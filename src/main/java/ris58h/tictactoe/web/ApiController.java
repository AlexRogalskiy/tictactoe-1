package ris58h.tictactoe.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ris58h.tictactoe.util.GameUtils;
import ris58h.tictactoe.domain.Game;
import ris58h.tictactoe.repository.GameRepository;


@RestController
public class ApiController {
    @Autowired
    GameRepository gameRepository;

    @PostMapping("/games")
    Object newGame(@RequestParam(name = "dimension") int dimension) {
        if (dimension <= 0) {
            return ResponseEntity.badRequest().body("Invalid dimension.");
        }
        Game newGame = new Game();
        newGame.board = new byte[dimension * dimension];
        newGame = gameRepository.save(newGame);
        return newGame;
    }

    @GetMapping("/games/{gameId}")
    Game getGame(@PathVariable(name = "gameId") long id) {
        return gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game not found!"));
    }

    @PostMapping("/games/{gameId}/turn")
    Object takeTurn(@PathVariable(name = "gameId") long id,
              @RequestParam(name = "x") int x,
              @RequestParam(name = "y") int y) {
        if (x < 0 || y < 0) {
            return ResponseEntity.badRequest().body("Invalid coordinates.");
        }

        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found!"));

        if (game.finished) {
            return ResponseEntity.badRequest().body("Game is already finished.");
        }

        byte[] board = game.board;
        int dimension = GameUtils.dimension(board);

        if (x >= dimension || y >= dimension) {
            return ResponseEntity.badRequest().body("Invalid coordinates.");
        }

        int offset = GameUtils.offset(dimension, x, y);

        if (board[offset] != Game.EMPTY) {
            return ResponseEntity.badRequest().body("Cell is not empty.");
        }

        board[offset] = Game.X;
        if (GameUtils.isFinished(board)) {
            game.finished = true;
        } else {
            int computersTurnIndex = GameUtils.indexOf(board, Game.EMPTY);
            board[computersTurnIndex] = Game.O;
            if (GameUtils.isFinished(board)) {
                game.finished = true;
            }
        }

        game = gameRepository.save(game);

        return game;
    }
}
