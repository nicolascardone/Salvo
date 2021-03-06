package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private LocalDateTime creationDate;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gameplayers;

    @OneToMany(mappedBy="gameID", fetch=FetchType.EAGER)
    Set<Score> scores;

    public Game () {}

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public Game(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<GamePlayer> getGameplayers() {
        return gameplayers;
    }

    public void setGameplayers(Set<GamePlayer> gameplayers) {
        this.gameplayers = gameplayers;
    }
    public Map<String, Object> makeGameDTO(){
        Map<String, Object>  dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("created", this.getCreationDate());
        dto.put("gamePlayers" , this.getGameplayers().stream().map(gp -> gp.makeGamePlayerDTO()).collect(Collectors.toList()));
        dto.put("scores", this.getGameplayers().stream()
                .map(x ->
                        {
                         if(x.getScore().isPresent()){
                             return x.getScore().get().makeScoreDTO();
                         }   else
                         { return "";}
                        }
                ));
        return dto;
    }
    //@JsonIgnore
    //public List<Player> getPlayers() {
    //    return gameplayers.stream().map(sub -> sub.getPlayer()).collect(Collectors.toList());
    //}
}
