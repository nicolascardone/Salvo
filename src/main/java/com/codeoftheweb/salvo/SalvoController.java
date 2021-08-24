package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController
{
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @RequestMapping("/games")
    public Map<String, Object> game() {
        Map<String,Object > dto = new LinkedHashMap<>();
        dto.put("games",gameRepository.findAll().stream().map(x -> x.makeGameDTO()).collect(Collectors.toList()));
        return dto;
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String, Object> findGamePlayer(@PathVariable Long gamePlayerId) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();
    return gamePlayer.makeGameViewDTO();
    }
}
