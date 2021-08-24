package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private LocalDateTime finishDate;
    private double score;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "playerID")
    private Player playerID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gameID")
    private Game gameID;

    public Score() {}

    public Score(LocalDateTime finishDate, double score, Player playerID, Game gameID) {
        this.finishDate = finishDate;
        this.score = score;
        this.playerID = playerID;
        this.gameID = gameID;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Player getPlayerID() {
        return playerID;
    }

    public void setPlayerID(Player playerID) {
        this.playerID = playerID;
    }

    public Game getGameID() {
        return gameID;
    }

    public void setGameID(Game gameID) {
        this.gameID = gameID;
    }

    public Map<String, Object> makeScoreDTO(){
        Map <String, Object>     dto =  new LinkedHashMap<>();
        dto.put("player", this.getPlayerID().getId());
        dto.put("score", this.getScore());
        dto.put("finishDate", this.getFinishDate());
        return dto;
    }
}
