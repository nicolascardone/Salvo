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
    @JoinColumn(name = "game")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player")
    private Player player;

    @OneToMany(mappedBy = "gamePlayer_id", fetch = FetchType.EAGER)
    private Set<Ship> ships;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    @OrderBy
    private Set<Salvo> salvos;

    public GamePlayer() {
    }

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

    public Map<String, Object> makeGamePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().makePlayerDTO());
        return dto;
    }
    //List<Map<String, Object>> lista1
    //        for(GamePlayer lgp:getGame().getGameplayers()){
    //            for(Salvo )
    //}

    public GamePlayer openente() {
         return this.getGame().getGameplayers().stream().filter(b -> !b.getId().equals(this.getId())).findFirst().orElse(null);
    }



    public Map<String, Object> makeGameViewDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getGame().getId());
        dto.put("created", this.getGame().getCreationDate());
        dto.put("gameState", GameState());
        dto.put("gamePlayers", this.getGame().getGameplayers().stream().map(d -> d.makeGamePlayerDTO()).collect(Collectors.toList()));
        dto.put("ships", this.getShips().stream().map(x -> x.makeShipDTO()).collect(Collectors.toList()));
        dto.put("salvoes", this.getGame().getGameplayers().stream().flatMap(z -> z.getSalvos().stream().map(x -> x.makeSalvoDTO())).collect(Collectors.toList()));

        Map<String, Object> hits = new LinkedHashMap<>();
        if (openente() == null || this.getShips().size() == 0 || openente().getShips().size() == 0) {
            hits.put("self", new ArrayList<>());
            hits.put("opponent", new ArrayList<>());
        } else {
            hits.put("self", this.makeSelfDTO());
            hits.put("opponent", openente().makeSelfDTO());
        }
        dto.put("hits", hits);

        return dto;
    }

    public List<Map<String, Object>> makeSelfDTO() {
        List<Map<String, Object>> dto = new ArrayList<>();

        Ship carrier = this.getShips().stream().filter(s -> s.getType().equals("carrier")).findFirst().get();
        List<String> carrierLocations = carrier.getShipLocations();

        Ship battleship = this.getShips().stream().filter(s -> s.getType().equals("battleship")).findFirst().get();
        List<String> battleshipLocations = battleship.getShipLocations();

        Ship submarine = this.getShips().stream().filter(s -> s.getType().equals("submarine")).findFirst().get();
        List<String> submarineLocations = submarine.getShipLocations();

        Ship destroyer = this.getShips().stream().filter(s -> s.getType().equals("destroyer")).findFirst().get();
        List<String> destroyerLocations = destroyer.getShipLocations();

        Ship patrolboat = this.getShips().stream().filter(s -> s.getType().equals("patrolboat")).findFirst().get();
        List<String> patrolboatLocations = patrolboat.getShipLocations();


        int carrier_total = 0;
        int battleship_total = 0;
        int submarine_total = 0;
        int destroyer_total = 0;
        int patrolboat_total = 0;

        for (Salvo salvo : this.openente().getSalvos()) {

            List<String> hitLocations = new ArrayList<>();
            Map<String, Object> hitsturn = new LinkedHashMap<>();

            int carrier_turno = 0;
            int battleship_turno = 0;
            int submarine_turno = 0;
            int destroyer_turno = 0;
            int patrolboat_turno = 0;
            int missed = salvo.getSalvoLocations().size();

            for (String tirosLocations : salvo.getSalvoLocations()) {


                if (carrierLocations.contains(tirosLocations)) {
                    hitLocations.add(tirosLocations);
                    carrier_turno++;
                    carrier_total++;

                }
                if (battleshipLocations.contains(tirosLocations)) {
                    hitLocations.add(tirosLocations);
                    battleship_turno++;
                    battleship_total++;
                }
                if (submarineLocations.contains(tirosLocations)) {
                    hitLocations.add(tirosLocations);
                    submarine_turno++;
                    submarine_total++;
                }
                if (destroyerLocations.contains(tirosLocations)) {
                    hitLocations.add(tirosLocations);
                    destroyer_turno++;
                    destroyer_total++;
                }
                if (patrolboatLocations.contains(tirosLocations)) {
                    hitLocations.add(tirosLocations);
                    patrolboat_turno++;
                    patrolboat_total++;
                }
            }


            Map<String, Object> damage = new LinkedHashMap<>();
            damage.put("carrierHits", carrier_turno);
            damage.put("battleshipHits", battleship_turno);
            damage.put("submarineHits", submarine_turno);
            damage.put("destroyerHits", destroyer_turno);
            damage.put("patrolboatHits", patrolboat_turno);

            damage.put("carrier", carrier_total);
            damage.put("battleship", battleship_total);
            damage.put("submarine", submarine_total);
            damage.put("destroyer", destroyer_total);
            damage.put("patrolboat", patrolboat_total);


            hitsturn.put("turn", salvo.getTurn());
            hitsturn.put("hitLocations", hitLocations);
            hitsturn.put("damages", damage);
            hitsturn.put("missed", missed - hitLocations.size());

            dto.add(hitsturn);

        }

        return dto;
    }

    public Optional<Score> getScore() {
        return this.getPlayer().getScore(this.getGame());
    }

    public String GameState(){
        System.out.println("HOLA");
        String gameState = "UNDEFINED";
        if(this.getShips().size() < 5 ){
            gameState = "PLACESHIPS";
            return gameState;
        }
        if(openente() == null){
            gameState = "WAITINGFOROPP";
            return gameState;
        }

        if(openente().getShips().size() != 5){
            gameState = "WAIT";
            return gameState;
        }

        GamePlayer player1 = this.getGame().getGameplayers().stream().min(Comparator.comparing(gamePlayer -> gamePlayer.getId())).get();

        GamePlayer player2 = this.getGame().getGameplayers().stream().max(Comparator.comparing(gamePlayer -> gamePlayer.getId())).get();

        int player1Turns = player1.getSalvos().size();
        int player2Turns = player2.getSalvos().size();

        GamePlayer player = this;

        if (this.getId()==player1.getId()){
            if (this.barcosHundidos(openente(), player)){
                if (player1Turns>player2Turns){
                    gameState =  "WAIT";
                    return gameState ;
                }
                else if (this.barcosHundidos(player,openente())){
                    gameState  =  "TIE";
                    return gameState ;

                }
                else {
                    gameState  =  "WON";
                    return gameState ;
                }
            }
            if (this.barcosHundidos(player,openente())){
                gameState  =  "LOST";
                return gameState ;
            }
        }
        else {
            if(this.barcosHundidos(openente(),player)){
                if (this.barcosHundidos(player,openente())){
                    gameState  = "TIE";
                    return gameState ;
                }
                else {
                    gameState  = "WON";
                    return gameState ;
                }
            }
            if(this.barcosHundidos(player, openente())){
                if (player1Turns==player2Turns){
                    gameState  = "LOST";
                    return gameState ;
                }
            }
        }
        if((this.getSalvos().size() - openente().getSalvos().size()) > 0){
            gameState = "WAIT";
            return gameState;
        }
        if(this.getSalvos().size() == openente().getSalvos().size()) {
            System.out.println("ACA2");
            if (this.getId() > openente().getId()) {
                System.out.println("ACA");
                gameState = "WAIT";
                return gameState;
            } else {
                gameState = "PLAY";
                return gameState;
            }
        }


        return gameState;
    }

    public boolean barcosHundidos(GamePlayer gpBarcos, GamePlayer gpSalvos) {

        GamePlayer opponent = this.openente();

        if (!gpBarcos.getShips().isEmpty() && !gpSalvos.getSalvos().isEmpty()) {
            return  gpSalvos.getSalvos()
                    .stream().flatMap(salvo -> salvo.getSalvoLocations().stream()).collect(Collectors.toList())
                    .containsAll(gpBarcos.getShips()
                            .stream().flatMap(ship -> ship.getShipLocations().stream())
                            .collect(Collectors.toList()));
        }
        return false;
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;


    }
}
