package ris58h.tictactoe.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ris58h.tictactoe.GameUtils;
import ris58h.tictactoe.domain.Game;
import ris58h.tictactoe.repository.GameRepository;


@RestController
public class ApiController {
    @Autowired
    GameRepository gameRepository;

    @PostMapping("/start")
    Object start(@RequestParam(name = "dimension") int dimension) {
        if (dimension <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Game newGame = new Game();
        newGame.setBoard(new byte[dimension * dimension]);
        newGame = gameRepository.save(newGame);
        return newGame;
    }

    @PostMapping("/move")
    Object move(@RequestParam(name = "id") long id,
              @RequestParam(name = "x") int x,
              @RequestParam(name = "y") int y) {
        if (x < 0 || y < 0) {
            return ResponseEntity.badRequest().build();
        }

        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found!"));

        if (game.isFinished()) {
            return ResponseEntity.badRequest().build();
        }

        byte[] board = game.getBoard();
        int dimension = GameUtils.dimension(board);

        if (x >= dimension || y >= dimension) {
            return ResponseEntity.badRequest().build();
        }

        int offset = GameUtils.offset(dimension, x, y);

        if (board[offset] != Game.EMPTY) {
            return ResponseEntity.badRequest().build();
        }

        board[offset] = Game.X;
        if (GameUtils.isFinished(board)) {
            game.setFinished(true);
        } else {
            int computersTurnIndex = GameUtils.indexOf(board, Game.EMPTY);
            board[computersTurnIndex] = Game.O;
            if (GameUtils.isFinished(board)) {
                game.setFinished(true);
            }
        }

        game = gameRepository.save(game);

        return game;
    }
}
