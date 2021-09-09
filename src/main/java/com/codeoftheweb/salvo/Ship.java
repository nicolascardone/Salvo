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
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer_id;

    @ElementCollection
    @Column(name = "shipLocation")
    private List<String> shipLocations = new ArrayList<>();

    public Ship() {}

    public Ship(String type, GamePlayer gamePlayer_id, List<String> shipLocations) {
        this.type = type;
        this.gamePlayer_id = gamePlayer_id;
        this.shipLocations = shipLocations;
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

    public List<String> getShipLocations() {
        return shipLocations;
    }

    public void setShipLocations(List<String> shipLocations) {
        this.shipLocations = shipLocations;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String,Object > makeShipDTO(){
        Map<String,Object > dto = new LinkedHashMap<>();

        dto.put("type", this.getType());
        dto.put("locations", this.getShipLocations());
    return dto;
    }
}