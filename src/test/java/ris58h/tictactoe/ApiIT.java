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
import ris58h.tictactoe.domain.GameState;
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

    private static final String GAMES_URL = "/games";

    @Test
    public void shouldGetErrorForUnknownGameId() {
        ResponseEntity<String> response = restTemplate.getForEntity(gameUrl(0), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void startShouldGetErrorIfNoParamsProvided() {
        ResponseEntity<String> response = restTemplate.postForEntity(GAMES_URL, null, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void startShouldGetErrorForZeroDimension() {
        ResponseEntity<String> response = restTemplate.postForEntity(GAMES_URL, dimensionRequest(0), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(response.getBody(), equalTo("Invalid dimension."));
    }

    @Test
    public void startShouldGetErrorForNegativeDimension() {
        ResponseEntity<String> response = restTemplate.postForEntity(GAMES_URL, dimensionRequest(-1), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(response.getBody(), equalTo("Invalid dimension."));
    }

    @Test
    public void shouldReturnGameById() {
        ResponseEntity<Game> startResponse = restTemplate.postForEntity(GAMES_URL, dimensionRequest(5), Game.class);
        assertThat(startResponse.getStatusCode(), equalTo(HttpStatus.OK));
        long gameId = startResponse.getBody().id;

        ResponseEntity<Game> response = restTemplate.getForEntity(gameUrl(gameId), Game.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody().id, equalTo(gameId));
    }

    @Test
    public void shouldGetErrorForDuplicateTurn() {
        ResponseEntity<Game> startResponse = restTemplate.postForEntity(GAMES_URL, dimensionRequest(3), Game.class);
        assertThat(startResponse.getStatusCode(), equalTo(HttpStatus.OK));
        long gameId = startResponse.getBody().id;

        String turnUrl = turnUrl(gameId);
        HttpEntity<?> turnRequest = turnRequest(1, 2);
        ResponseEntity<Game> turnResponse = restTemplate.postForEntity(turnUrl, turnRequest, Game.class);
        assertThat(turnResponse.getStatusCode(), equalTo(HttpStatus.OK));
        Game game = turnResponse.getBody();
        assertThat(game.id, equalTo(gameId));
        assertThat(game.board[7], equalTo(GameState.X));
        assertTrue(GameUtils.indexOf(game.board, GameState.O) >= 0);
        assertFalse(game.finished);

        ResponseEntity<String> duplicateTurnResponse = restTemplate.postForEntity(turnUrl, turnRequest, String.class);
        assertThat(duplicateTurnResponse.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(duplicateTurnResponse.getBody(), equalTo("Cell is not empty."));
    }

    @Test
    public void testGame() {
        int dimension = 3;

        ResponseEntity<Game> startResponse = restTemplate.postForEntity(GAMES_URL, dimensionRequest(dimension), Game.class);
        assertThat(startResponse.getStatusCode(), equalTo(HttpStatus.OK));
        Game game = startResponse.getBody();
        assertThat(game.board.length, equalTo(9));
        assertFalse(game.finished);

        Long gameId = game.id;
        String turnUrl = turnUrl(gameId);

        int turn = 1;
        while (!game.finished) {
            // There can't be more than d^2/2+1 turns.
            assertTrue(turn < (dimension * dimension) / 2 + 1);

            int indexOfEmpty = GameUtils.indexOf(game.board, GameState.E);
            assertTrue(indexOfEmpty >= 0);

            int x = indexOfEmpty % dimension;
            int y = indexOfEmpty / dimension;
            HttpEntity<?> request = turnRequest(x, y);
            ResponseEntity<Game> response = restTemplate.postForEntity(turnUrl, request, Game.class);
            assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
            game = response.getBody();

            turn++;
        }

        ResponseEntity<String> afterFinishResponse = restTemplate.postForEntity(turnUrl, turnRequest(2, 2), String.class);
        assertThat(afterFinishResponse.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(afterFinishResponse.getBody(), equalTo("Game is already finished."));
    }

    private static String gameUrl(long id) {
        return GAMES_URL + "/" + id;
    }

    private static String turnUrl(long id) {
        return gameUrl(id) + "/turn";
    }

    private static HttpEntity<?> dimensionRequest(int dimension) {
        return new HttpEntity<>(paramsMap("dimension", String.valueOf(dimension)), null);
    }

    private static HttpEntity<?> turnRequest(int x, int y) {
        return new HttpEntity<>(paramsMap("x", String.valueOf(x),
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
