package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDateTime creationDate;

    @OneToMany(mappedBy="gameID", fetch=FetchType.EAGER)
    Set<GamePlayer> gameplayers;

    public Game () {}

    public Game(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<Player> getPlayers() {
        return gameplayers.stream().map(sub -> sub.getPlayerID()).collect(Collectors.toList());
    }
}
