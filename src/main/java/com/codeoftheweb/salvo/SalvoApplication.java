package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);}
	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
		return (args) -> {
		Player player1 = new Player("nicolascardone@gmail.com");
		Player player2 = new Player("ncardone@pioix.edu.ar");

        playerRepository.save(player1);
        playerRepository.save(player2);

        Game game1 = new Game(LocalDateTime.now());
        Game game2 = new Game(LocalDateTime.now().plusHours(1));
        Game game3 = new Game(LocalDateTime.now().plusHours(2));
        Game game4 = new Game(LocalDateTime.now().plusHours(3));
        Game game5 = new Game(LocalDateTime.now().plusHours(4));
        Game game6 = new Game(LocalDateTime.now().plusHours(5));
        gameRepository.save(game1);
        gameRepository.save(game2);
        gameRepository.save(game3);
        gameRepository.save(game4);
        gameRepository.save(game5);
        gameRepository.save(game6);

        GamePlayer gameplayer1 = new GamePlayer(LocalDateTime.now().plusHours(1), game1, player1);
        GamePlayer gameplayer2 = new GamePlayer(LocalDateTime.now(), game1, player2);

        gamePlayerRepository.save(gameplayer1);
		gamePlayerRepository.save(gameplayer2);
		};
	}
}
