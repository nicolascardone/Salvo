package com.codeoftheweb.salvo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String userName;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gameplayers;

    @OneToMany(mappedBy="playerID", fetch=FetchType.EAGER)
    Set<Score> scores;

    public Player() {}

    public Set<GamePlayer> getGameplayers() {
        return gameplayers;
    }

    public void setGameplayers(Set<GamePlayer> gameplayers) {
        this.gameplayers = gameplayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public Player(String userName) {
        this.userName = userName;
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map <String, Object> makePlayerDTO(){
        Map <String, Object>     dto =  new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("email", this.getUserName());
        return dto;
    }

    public Optional<Score> getScore(Game juego){
        return this.getScores().stream().filter(f-> f.getGameID().getId().equals(juego.getId())).findFirst();
    }
    //@JsonIgnore
    //public List<Game> getGames() {
    //    return gameplayers.stream().map(sub -> sub.getGame()).collect(Collectors.toList());
    //}

}