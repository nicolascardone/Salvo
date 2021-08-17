package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String shipType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer_id;

    @ElementCollection
    @Column(name = "shipLocation")
    private List<String> shipLocation = new ArrayList<>();

    public Ship() {}

    public Ship(String shipType, GamePlayer gamePlayer_id, List<String> shipLocation) {
        this.shipType = shipType;
        this.gamePlayer_id = gamePlayer_id;
        this.shipLocation = shipLocation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GamePlayer getGamePlayer_id() {
        return gamePlayer_id;
    }

    public void setGamePlayer_id(GamePlayer gamePlayer_id) {
        this.gamePlayer_id = gamePlayer_id;
    }

    public List<String> getShipLocation() {
        return shipLocation;
    }

    public void setShipLocation(List<String> shipLocation) {
        this.shipLocation = shipLocation;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public Map<String,Object > makeShipDTO(){
        Map<String,Object > dto = new LinkedHashMap<>();

        dto.put("type", this.getShipType());
        dto.put("locations", this.getShipLocation());
    return dto;
    }
}