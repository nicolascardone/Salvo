package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private LocalDateTime joinDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player")
    private Player player;

    @OneToMany(mappedBy="gamePlayer_id", fetch=FetchType.EAGER)
    private Set<Ship> ships;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Salvo> salvos;

    @ElementCollection
    @Column(name = "opponent")
    private List<String> opponent = new ArrayList<>();

    @ElementCollection
    @Column(name = "self")
    private List<String> self = new ArrayList<>();

    public GamePlayer () {}

    public GamePlayer(LocalDateTime joinDate, Game game, Player player) {
        this.joinDate = joinDate;
        this.game = game;
        this.player = player;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Salvo> getSalvos() {
        return salvos;
    }

    public void setSalvos(Set<Salvo> salvos) {
        this.salvos = salvos;
    }

    public Map<String, Object> makeGamePlayerDTO(){
        Map<String, Object>  dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().makePlayerDTO());
        return dto;
    }
    //List<Map<String, Object>> lista1
    //        for(GamePlayer lgp:getGame().getGameplayers()){
    //            for(Salvo )
    //}


    public Map<String, Object> makeGameViewDTO(){
        Map<String, Object>  dto = new LinkedHashMap<>();
        dto.put("id", this.getGame().getId());
        dto.put("created",this.getGame().getCreationDate());
        dto.put("gameState","PLACESHIPS");
        dto.put("gamePlayers", this.getGame().getGameplayers().stream().map(d -> d.makeGamePlayerDTO()).collect(Collectors.toList()));
        dto.put("ships", this.getShips().stream().map(x -> x.makeShipDTO()).collect(Collectors.toList()));
        dto.put("salvoes",this.getGame().getGameplayers().stream().flatMap(z -> z.getSalvos().stream().map(x -> x.makeSalvoDTO())).collect(Collectors.toList()));
        dto.put("hits",this.makeHitsDTO());

        return dto;
    }
    public Map<String, Object> makeHitsDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("self",self);
        dto.put("opponent",opponent);
        return dto;
    }
    public Optional<Score> getScore(){
        return this.getPlayer().getScore(this.getGame());
    }

}
