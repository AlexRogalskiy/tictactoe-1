package ris58h.tictactoe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ris58h.tictactoe.domain.Game;
import ris58h.tictactoe.util.GameUtils;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiIT {

	@Autowired
	private TestRestTemplate restTemplate;

    private static final String START_URL = "/start";
    private static final String MOVE_URL = "/move";

    @Test
    public void startShouldFailIfNoParamsProvided() {
        ResponseEntity<Game> response = restTemplate.postForEntity(START_URL, null, Game.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void startShouldFailForZeroDimension() {
        ResponseEntity<Game> response = restTemplate.postForEntity(START_URL, dimensionRequest(0), Game.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void startShouldFailForNegativeDimension() {
        ResponseEntity<Game> response = restTemplate.postForEntity(START_URL, dimensionRequest(-1), Game.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void testGame() {
        int dimension = 3;

        ResponseEntity<Game> startResponse = restTemplate.postForEntity(START_URL, dimensionRequest(dimension), Game.class);
        assertThat(startResponse.getStatusCode(), equalTo(HttpStatus.OK));
        Game startGame = startResponse.getBody();
        assertThat(startGame.getBoard().length, equalTo(9));
        assertFalse(startGame.isFinished());

        Long gameId = startGame.getId();

        HttpEntity<?> moveRequest = moveRequest(gameId, 1, 2);
        ResponseEntity<Game> moveResponse = restTemplate.postForEntity(MOVE_URL, moveRequest, Game.class);
        assertThat(moveResponse.getStatusCode(), equalTo(HttpStatus.OK));
        Game move1Game = moveResponse.getBody();
        assertThat(move1Game.getId(), equalTo(gameId));
        assertThat(move1Game.getBoard()[7], equalTo(Game.X));
        assertTrue(GameUtils.indexOf(move1Game.getBoard(), Game.O) >= 0);
        assertFalse(move1Game.isFinished());

        ResponseEntity<Game> duplicateMoveResponse = restTemplate.postForEntity(MOVE_URL, moveRequest, Game.class);
        assertThat(duplicateMoveResponse.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));

        Game game = move1Game;
        int turn = 1;
        while (!game.isFinished()) {
            // There can't be more than d^2/2+1 turns.
            assertTrue(turn < (dimension * dimension) / 2 + 1);

            int indexOfEmpty = GameUtils.indexOf(game.getBoard(), Game.EMPTY);
            assertTrue(indexOfEmpty >= 0);

            int x = indexOfEmpty % dimension;
            int y = indexOfEmpty / dimension;
            HttpEntity<?> request = moveRequest(gameId, x, y);
            ResponseEntity<Game> response = restTemplate.postForEntity(MOVE_URL, request, Game.class);
            assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
            game = response.getBody();

            turn++;
        }

        ResponseEntity<Game> afterFinishResponse = restTemplate.postForEntity(MOVE_URL, moveRequest(gameId, 2, 2), Game.class);
        assertThat(afterFinishResponse.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    private static HttpEntity<?> dimensionRequest(int dimension) {
        return new HttpEntity<>(paramsMap("dimension", String.valueOf(dimension)), null);
    }

    private static HttpEntity<?> moveRequest(long id, int x, int y) {
        return new HttpEntity<>(paramsMap("id", String.valueOf(id),
                "x", String.valueOf(x),
                "y", String.valueOf(y)), null);
    }

    private static MultiValueMap<String, String> paramsMap(String... kvs) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        for (int i = 0; i < kvs.length; i++,i++) {
            String key = kvs[i];
            String value = kvs[i + 1];
            params.add(key, value);
        }
        return params;
    }
}
