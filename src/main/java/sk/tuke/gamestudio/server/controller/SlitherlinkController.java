package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.slitherlink.core.Board;
import sk.tuke.gamestudio.game.slitherlink.core.Cell;
import sk.tuke.gamestudio.game.slitherlink.core.CellState;
import sk.tuke.gamestudio.game.slitherlink.core.GameState;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.*;

@Controller
@RequestMapping("/slitherlink")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class SlitherlinkController {
    @Autowired
    private UserController userController;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;

    private int level = 1;
    private Board board = new Board(getLevelSize(level));
    private int points = 0;

    public int getSize() {
        return board.field.length;
    }

    public void processCommand(String Row, String Column, String Line) {
        if ( Row == null || Column == null || Line == null) return;

        try {
            int row = Integer.parseInt(Row);
            int column = Integer.parseInt(Column);
            int line = Integer.parseInt(Line);
            if (board.getState() == GameState.PLAYING) {
                board.set_line(row, column, line);
                board.checkGameState();
                if (board.getState() == GameState.SOLVED) {
                    points = points + ((level <= 6)? 1:(level<=13)? 3: 6);
                    level++;
                    board = new Board(getLevelSize(level));
                }
                else if (board.getState() == GameState.FAILED) {
                    board = new Board(getLevelSize(level));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("")
    public String slitherlink(
            @RequestParam(value = "row", required = false) String row,
            @RequestParam(value = "column", required = false) String column,
            @RequestParam(value = "line", required = false) String line,
            Model model
    ) {
        processCommand(row, column, line);
        model.addAttribute("message", level < 2 ? "Good Luck!": level < 5? "Good!": level < 10? "Excellent!": "You are not a human)");
        fillModel(model);
        model.addAttribute("htmlField", getHtmlField()); // Add this line
        return "slitherlink";
    }

    @RequestMapping("/new")
    public String newGame(Model model) {
        scoreService.addScore(new Score("slitherlink", userController.getLoggedUser().getUsername(), points, new Date()));
        level = 1;
        points = 0;
        board = new Board(getLevelSize(level));
        fillModel(model);
        return "redirect:/slitherlink";
    }

    private void fillModel(Model model) {
        model.addAttribute("scores", scoreService.getTopScores("mines"));
        model.addAttribute("htmlField", getHtmlField());
        model.addAttribute("comments", commentService.getComments("slitherlink"));
        model.addAttribute("rating", ratingService.getAverageRating("slitherlink"));
    }

    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        var size = board.getField().length;
        sb.append("<div style='display: flex; justify-content: center;'>\n"); // Add this line
        sb.append("<style>\n");
        sb.append("table, th, td {\n");
        sb.append("    border: none;\n"); // Add this line
        sb.append("}\n");
        sb.append("</style>\n");
        sb.append(String.format("<table style='border-collapse: collapse; position: relative; separate; line-height:0; width: %dpx; height: %dpx'>\n", size*64, size*64));

        for (int row = 0; row < size; row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < size; column++) {
                Cell cell = board.getField()[row][column];
                List<String> imageNames = getImageNames(cell);
                sb.append(String.format("<td style='width: 64px; height: 64px; padding: 0; border: 0; position: relative; left: %dpx; top: %dpx;'>\n", -4*column, -4*row));
                sb.append(String.format("<a href='#' onclick=\"showDialog(%d, %d)\">\n", row, column));
                if (cell.getClue() != -1) {
                    sb.append(String.format("<img src='/images/%s' style=' position: absolute; transform-origin: top left; transform: scale(2);'>\n", imageNames.get(0)));
                }
                sb.append(String.format("<img src='/images/%s' style='width: 64px; height: 64px;'>\n", imageNames.get(1)));
                sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }

        sb.append("</table>\n");
        sb.append("</div>\n"); // Add this line
        sb.append("<script>\n");
        sb.append("function showDialog(row, column) {\n");
        sb.append("    Swal.fire({\n");
        sb.append("        title: 'Select Line',\n");
        sb.append("        input: 'select',\n");
        sb.append("        inputOptions: {\n");
        sb.append("            '0': 'Left',\n");
        sb.append("            '1': 'Top',\n");
        sb.append("            '2': 'Right',\n");
        sb.append("            '3': 'Bottom'\n");
        sb.append("        },\n");
        sb.append("        showCancelButton: true,\n");
        sb.append("    }).then((result) => {\n");
        sb.append("        if (result.isConfirmed) {\n");
        sb.append("            window.location.href = '/slitherlink?row=' + row + '&column=' + column + '&line=' + result.value;\n");
        sb.append("        }\n");
        sb.append("    });\n");
        sb.append("}\n");
        sb.append("</script>\n");
        return sb.toString();
    }


    private List<String> getImageNames(Cell cell) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();

        sb.append("Sprite-");
        sb1.append("Sprite-");

        sb.append(cell.getClue());
        if(cell.getState() == CellState.CLOSED) {
            sb.append("g");
        }
        sb.append(".png");

        for (int line : cell.getLines()) {
            sb1.append(line);
        }
        sb1.append(".png");

        return new ArrayList<>()
        {{
            add(sb.toString());
            add(sb1.toString());
        }};
    }


    private int getLevelSize(int level) {
        Random random = new Random();
        return (level <=6)? random.nextInt(3)+3: (level <=13)? random.nextInt(3)+6: random.nextInt(3)+9;
    }

    public int getPoints() {
        return points;
    }

    public int getLevel() {
        return level;
    }
}
