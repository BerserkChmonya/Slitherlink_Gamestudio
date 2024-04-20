package sk.tuke.gamestudio.game.slitherlink.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.slitherlink.core.Board;
import sk.tuke.gamestudio.game.slitherlink.core.Cell;
import sk.tuke.gamestudio.game.slitherlink.core.GameState;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;


import java.util.*;

public class UI {
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private ScoreService scoreService;
    private Board board;

    Scanner scanner = new Scanner(System.in);
    public void play() {
        printLabel();
        String player = getPlayerName();
        int level = 1;
        int points = 0;
        while(true) {
            int size = getLevelSize(level);
            this.board = new Board(size);
            do {
                show();
                handleInput();
            } while (board.getState() == GameState.PLAYING);

            show();

            if (board.getState() == GameState.SOLVED) {
                System.out.println("Solved!");
                points = points + ((size <= 5)? 1:(size<=8)? 3: 6);
            } else if (board.getState() == GameState.FAILED) {
                System.out.println("Failed!");
                level--;
            }
            System.out.println("Your points: " + points);

            askForAComment(player);
            if(!askForContinue(player, points)){
                askForARating(player);
                break;
            }

            level++;
        }
    }

    private void show() {
        if(board != null){
            printBoard();
        }
    }

    private void handleInput() {
        try{
            System.out.print("ROW: ");
            int row = scanner.nextInt();
            System.out.print("COL: ");
            int col = scanner.nextInt();
            System.out.print("Line: 0-L, 1-T, 2-R, 3-B: ");
            int line = scanner.nextInt();
            System.out.println();
            if(!board.set_line(row - 1, col - 1, line))
                System.out.println("This can't be done!");
            board.checkGameState();
        } catch (InputMismatchException e) {
            System.out.println("Write numbers please!");
        }
        finally {
            scanner.nextLine();
        }
    }

    private int getLevelSize(int level) {
        Random random = new Random();
        return (level <=6)? random.nextInt(3)+3: (level <=13)? random.nextInt(3)+6: random.nextInt(3)+9;
    }

    private String getPlayerName() {
        String username;
        do {
            System.out.println("Enter your username(without spaces):");
            username = scanner.nextLine();
        }while(username.isEmpty() || username.contains(" "));

        return username;
    }

    private void printLabel() {
        String asciiArt = """
                _____ _      ____  ______  __ __    ___  ____   _      ____  ____   __  _
               / ___/| |    |    ||      ||  |  |  /  _]|    \\ | |    |    ||    \\ |  |/ ]
               (   \\_ | |     |  | |      ||  |  | /  [_ |  D  )| |     |  | |  _  ||  ' /
                \\__  || |___  |  | |_|  |_||  _  ||    _]|    / | |___  |  | |  |  ||    \\
               /  \\ ||     | |  |   |  |  |  |  ||   [_ |    \\ |     | |  | |  |  ||     \\
               \\    ||     | |  |   |  |  |  |  ||     ||  .  \\|     | |  | |  |  ||  .  |
                \\___||_____||____|  |__|  |__|__||_____||__|\\_||_____||____||__|__||__|\\_|""".stripTrailing();

        System.out.println(asciiArt);
    }

    public void askForAComment(String player) {
        System.out.println();
        String answer;
        do {
            System.out.print("Do you want to let a comment? y/n: ");
            answer = scanner.nextLine();
        }while (!(answer.equals("y") || answer.equals("n")));
        if(answer.equals("y")) {
            System.out.println("Please write your comment down->");
            String comment = scanner.nextLine();
            commentService.addComment(new Comment("slitherlink", player, comment, getCurrentDate()));
            System.out.println("Your comment has been saved!");
            System.out.println("\n There are comments for this game now:");
            List<Comment> comments = commentService.getComments("slitherlink");
            Collections.reverse(comments);
            for(Comment comm : comments) {
                System.out.println("~" + comm.getPlayer() + ": " + comm.getComment());
            }
        }
    }

    public void askForARating(String player) {
        System.out.println();
        String answer;
        do {
            System.out.print("Do you want to give a rating?) y/n: ");
            answer = scanner.nextLine();
        }while (!(answer.equals("y") || answer.equals("n")));
        if(answer.equals("y")) {
            int rating = 0;
            do{
                System.out.print("Please write a number between 0 and 5, 0 - The worst that i have been seen, 5 - This is masterpiece:  ");
                try {
                    rating = scanner.nextInt();
                }
                catch (NumberFormatException e){
                    System.out.println("Please enter a number!");
                }
            } while (rating < 0 || rating > 5);

            ratingService.setRating(new Rating("slitherlink", player, rating, getCurrentDate()));
            int av_rating = ratingService.getAverageRating("slitherlink");
            System.out.print("Thank You for rating). This is current av rating: ");
            System.out.println((av_rating == -1)?"Sorry, there is not rating now(": av_rating);
        }
    }

    public boolean askForContinue(String player, int points) {
        System.out.println();

        String answer;
        do {
            System.out.print("Do you want to continue? y/n: ");
            answer = scanner.nextLine();
        }while (!(answer.equals("y") || answer.equals("n")));
        boolean Continue = answer.equals("y");

        if(!Continue) {
            scoreService.addScore(new Score("slitherlink", player, points, getCurrentDate()));
            System.out.println("Your score has been saved!");

            List<Score> scores = scoreService.getTopScores("slitherlink");
            System.out.println("Top scores in this game now:");
            System.out.println();
            int place = 1;
            for(Score score : scores) {
                System.out.println(place +". " + score.toString());
                place++;
            }
            System.out.println();
        }

        return Continue;
    }

    public void printBoard() {
        int size = board.getField().length;
        System.out.print("  ");
        for(int i = 0; i <= size; i++){
            System.out.print("---");
        }
        System.out.println();
        for(int pRow = 0; pRow < size*2; pRow++){
            if(pRow%2 != 0) System.out.print(pRow/2+1 + "| ");
            else System.out.print("   ");
            for(int pCol = 0; pCol < size; pCol++){
                if(pRow%2 != 0) {
                    if(pCol==0)
                        System.out.print((board.getField()[pRow/2][pCol].getLines()[0] == 1) ? "|" : " ");
                    System.out.print((board.getField()[pRow/2][pCol].getClue() == -1)?" ":board.getField()[pRow/2][pCol]);
                    System.out.print((board.getField()[pRow/2][pCol].getLines()[2] == 1) ? " |" : "  ");
                    if(pCol== size-1) System.out.print(" |");
                }
                else{
                    Cell cell = board.getField()[pRow/2][pCol];
                    int[] lines = cell.getLines();
                    if(lines[1] == 1)
                        System.out.print("---");
                    else System.out.print("   ");
                }
            }
            System.out.println();
        }
        System.out.print("   ");
        for(int i = 0; i < size; i++) {
            System.out.print((board.getField()[size-1][i].getLines()[3] == 1)?"---":"   ");
        }
        System.out.println();
        System.out.print("  ");
        for(int i = 0; i <= size; i++){
            System.out.print("---");
        }
        System.out.println();
        System.out.print("    ");
        for(int i = 0; i < size; i++){
            System.out.print(i+1 + ". ");
        }
        System.out.println();
    }

    private Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
}
