package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ShipRepository shipRepository;
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
     }
    @RequestMapping("/games")
        public Map<String, Object> getController(Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>();

    if (isGuest(authentication)){
        dto.put("player","Guest");
    }
    else
    {
    dto.put("player",playerRepository.findByUserName(authentication.getName()).makePlayerDTO());
    }

        dto.put("games", gameRepository.findAll().stream().map(x -> x.makeGameDTO()).collect(Collectors.toList()));
        return dto;
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity <Map<String, Object>>findGamePlayer(@PathVariable Long gamePlayerId, Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();

        if(playerRepository.findByUserName(authentication.getName()) .getGameplayers().stream()
                .anyMatch(m->m.getId().equals(gamePlayerId))){
            return new ResponseEntity<>(gamePlayer.makeGameViewDTO(), HttpStatus.ACCEPTED);
        }else{
            return new ResponseEntity<>(makeMap("que miras virgo",7), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path="/games")
    public  ResponseEntity<Map> createGame(Authentication authentication) {


        LocalDateTime Tiempo = LocalDateTime.now();

        Game newgame = gameRepository.save(new Game(Tiempo));
        GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(
                LocalDateTime.now(),newgame,this.playerRepository.findByUserName(authentication.getName())));

        return new ResponseEntity<>(makeMap("gpid",newGamePlayer.getId()), HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUsername(@RequestParam String email, @RequestParam String password) {
        if (email.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
        }
        Player player = playerRepository.findByUserName(email) ;
        if (player != null) {
            return new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.CONFLICT);
        }

        Player newPlayer = playerRepository.save(new Player(email,passwordEncoder.encode(password)));
        return new ResponseEntity<>(makeMap("id", newPlayer.getId()), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/game/{nn}/players",method = {RequestMethod.GET,RequestMethod.POST})
    public  ResponseEntity<Map> joinGameButton(@PathVariable Long nn,Authentication authentication) {

//        if(gameRepository .findByUserName(authentication.getName()).getGameplayers().stream().findFirst().get().getPlayer().getId() == )
        if(playerRepository.findByUserName(authentication.getName()) ==null){

            return new ResponseEntity<>(makeMap("No esta autorizado",0), HttpStatus.UNAUTHORIZED);
        }
        if(gameRepository.getById(nn).getId()==null){

            return new ResponseEntity<>(makeMap("No existe",0), HttpStatus.FORBIDDEN);

        }
        if( gameRepository.getById(nn).getGameplayers().size() >=2){
            return new ResponseEntity<>(makeMap("Ya estamos todos",0), HttpStatus.FORBIDDEN);

        }

        GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(
                LocalDateTime.now(), gameRepository.getById(nn), this.playerRepository.findByUserName(authentication.getName())
        ));
        return new ResponseEntity<>(makeMap("gpid",newGamePlayer.getId()), HttpStatus.CREATED);

    }

    @RequestMapping(value = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST )
    public ResponseEntity <Map<String, Object >> addship (@PathVariable long gamePlayerId,@RequestBody List<Ship> ships ,Authentication authentication) {

        if(isGuest(authentication)) {
        return new ResponseEntity<>(makeMap("error", "No te logueaste"), HttpStatus.UNAUTHORIZED);
        }else  if(!gamePlayerRepository.existsById(gamePlayerId)) {
            return new ResponseEntity<>(makeMap("error", "El gameplayer no existe"), HttpStatus.UNAUTHORIZED);
        }else if(gamePlayerRepository.getById(gamePlayerId).getPlayer() != playerRepository.findByUserName(authentication.getName())){
            return new ResponseEntity<>(makeMap("error", "Nadie te invitó pa"),HttpStatus.UNAUTHORIZED);
        }else if(gamePlayerRepository.getById(gamePlayerId).getShips().size() > 0){
            return new ResponseEntity<>(makeMap("error", "Ya tenes todos los barcos"),HttpStatus.FORBIDDEN);
        }else {
            for(Ship ship : ships){
                ship.setGamePlayer_id(gamePlayerRepository.getById((gamePlayerId)));
                shipRepository.save(ship);
            }
            return new ResponseEntity<>(makeMap("creaste barcos",ships), HttpStatus.CREATED);
        }
    }
    @RequestMapping(value = "/games/players/{gameplayerid}/ships",method = RequestMethod.GET)
    public  ResponseEntity <Map<String, Object>>PlaceShips(@PathVariable Long gameplayerid
            ,Authentication authentication) {
            Map<String, Object> dto = new LinkedHashMap<>();
        if(isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "No te logueaste"), HttpStatus.UNAUTHORIZED);
        }else  if(!gamePlayerRepository.existsById(gameplayerid)) {
            return new ResponseEntity<>(makeMap("error", "El gameplayer no existe"), HttpStatus.UNAUTHORIZED);
        }else if(gamePlayerRepository.getById(gameplayerid).getPlayer() != playerRepository.findByUserName(authentication.getName())) {
            return new ResponseEntity<>(makeMap("error", "Nadie te invitó pa"), HttpStatus.UNAUTHORIZED);
        }else {
            dto.put("ship", gamePlayerRepository.findById(gameplayerid).get().getShips().stream().map(b -> b.makeShipDTO()).collect(Collectors.toList()));

            return new ResponseEntity<>(makeMap("ships", dto ),HttpStatus.ACCEPTED);
        }
        }


        private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;

    }
    }