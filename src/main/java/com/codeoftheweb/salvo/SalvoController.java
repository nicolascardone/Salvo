package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
    }