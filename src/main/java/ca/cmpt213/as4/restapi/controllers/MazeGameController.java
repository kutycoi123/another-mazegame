package ca.cmpt213.as4.restapi.controllers;

import ca.cmpt213.as4.restapi.ApiWrapperObjects.ApiBoardWrapper;
import ca.cmpt213.as4.restapi.ApiWrapperObjects.ApiGameWrapper;
import ca.cmpt213.as4.restapi.models.MazeGame;
import ca.cmpt213.as4.restapi.models.MoveDirection;
import org.graalvm.compiler.lir.sparc.SPARCMove;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MazeGameController {
    private final static int MAZE_HEIGHT = 15;
    private final static int MAZE_WIDTH = 20;
    private ArrayList<MazeGame> gameList = new ArrayList<>();

    @GetMapping("/api/about")
    public String getAuthorInfo() {
        return "TRUNG LAM NGUYEN - 301326848";
    }

    @GetMapping("/api/games")
    public List<ApiGameWrapper> getGameList() {
        List<ApiGameWrapper> list = new ArrayList<>();
        for (MazeGame game : gameList) {
            list.add(ApiGameWrapper.makeFromGame(game, list.size() + 1));
        }
        return list;
    }

    @PostMapping("/api/games")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiGameWrapper createGame() {
        MazeGame newGame = new MazeGame(MAZE_HEIGHT, MAZE_WIDTH);
        gameList.add(newGame);
        return ApiGameWrapper.makeFromGame(newGame, gameList.size());
    }

    @GetMapping("/api/games/{id}")
    public ApiGameWrapper getGame(@PathVariable("id") int id) {
        if (id < 1 || id > gameList.size()) {
            throw new IndexOutOfBoundsException();
        }
        return ApiGameWrapper.makeFromGame(gameList.get(id - 1), id);
    }

    @GetMapping("/api/games/{id}/board")
    public ApiBoardWrapper getGameBoard(@PathVariable("id") int id) {
        if (id < 1 || id > gameList.size()) {
            throw new IndexOutOfBoundsException();
        }
        return ApiBoardWrapper.makeFromGame(gameList.get(id - 1));
    }

    @PostMapping("/api/games/{id}/moves")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void makeAMove(@PathVariable("id") int id, @RequestBody String move) {
        if (id < 1 || id > gameList.size()) {
            throw new IndexOutOfBoundsException();
        }
        MazeGame game = gameList.get(id - 1);
        MoveDirection direction;
        switch (move) {
            case "MOVE_UP":
                direction = MoveDirection.UP;
                break;
            case "MOVE_DOWN":
                direction = MoveDirection.DOWN;
                break;
            case "MOVE_LEFT":
                direction = MoveDirection.LEFT;
                break;
            case "MOVE_RIGHT":
                direction = MoveDirection.RIGHT;
                break;
            case "MOVE_CATS":
                game.moveCats();
                return;
            default:
                throw new IllegalArgumentException();
        }
        if (!game.isValidMove(direction)) {
            throw new IllegalArgumentException();
        }
        game.run(direction);
    }

    @PostMapping("/api/games/{id}/cheatstate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void activateCheatState(@PathVariable("id") int id, @RequestBody String cheat) {
        if (id < 1 || id > gameList.size()) {
            throw new IndexOutOfBoundsException();
        }
        MazeGame game = gameList.get(id - 1);
        switch (cheat) {
            case "1_CHEESE":
                game.setCheeseToWin(1);
                break;
            case "SHOW_ALL":
                game.setMazeRevealed(true);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND,
            reason = "Resource not found")
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public void badIdExceptionHandler() {

    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST,
            reason = "Bad request body")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badBodyExceptionHandler() {

    }


}
