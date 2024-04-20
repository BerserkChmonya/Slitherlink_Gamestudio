package sk.tuke.gamestudio.entity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
@Entity
@NamedQueries({
        @NamedQuery(
                name = "Comment.getComments",
                query = "SELECT c FROM Comment c WHERE c.game = :game"
        ),
        @NamedQuery(
                name = "Comment.resetComments",
                query = "DELETE FROM Comment"
        )
})
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int ident;
    private String game;
    private String player;
    private String comment;
    private Date commentedOn;

    public Comment(String game, String player, String comment, Date commentedOn) {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }

    public Comment() {

    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(Date commentedOn) {
        this.commentedOn = commentedOn;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    @Override
    public String toString() {
        return "Score{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", comment=" + comment +
                ", commentedOn=" + commentedOn +
                '}';
    }
}
