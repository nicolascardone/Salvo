package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayerID;

    public Ship (){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ship(GamePlayer gamePlayerID) {
        this.gamePlayerID = gamePlayerID;
    }

    public GamePlayer getGamePlayerID() {
        return gamePlayerID;
    }

    public void setGamePlayerID(GamePlayer gamePlayerID) {
        this.gamePlayerID = gamePlayerID;
    }
}
