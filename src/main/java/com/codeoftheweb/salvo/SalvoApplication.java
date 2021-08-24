package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);}
	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository,
                                      GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository,
                                      SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
		return (args) -> {
		Player player2 = new Player("nicolascardone@gmail.com");
		Player player1 = new Player("ncardone@pioix.edu.ar");
		Player player3 = new Player("juanitraga@pioix.edu.ar");
		Player player4 = new Player("leandro@pioix.edu.ar");
		Player player5 = new Player("messi@pioix.edu.ar");
        playerRepository.save(player1);
        playerRepository.save(player2);
        playerRepository.save(player3);
        playerRepository.save(player4);
        playerRepository.save(player5);

        Game game1 = new Game(LocalDateTime.now());
        Game game2 = new Game(LocalDateTime.now().plusHours(1));
        Game game3 = new Game(LocalDateTime.now().plusHours(2));

        gameRepository.save(game1);
        gameRepository.save(game2);
        gameRepository.save(game3);

        GamePlayer gameplayer1 = new GamePlayer(LocalDateTime.now(), game1, player1);
        GamePlayer gameplayer2 = new GamePlayer(LocalDateTime.now(), game1, player2);
        GamePlayer gameplayer3 = new GamePlayer(LocalDateTime.now().plusHours(1), game2, player3);
        GamePlayer gameplayer4 = new GamePlayer(LocalDateTime.now().plusHours(1), game2, player4);
        GamePlayer gameplayer5 = new GamePlayer(LocalDateTime.now().plusHours(2), game3, player5);
        GamePlayer gameplayer6 = new GamePlayer(LocalDateTime.now().plusHours(2), game3, player1);
        gamePlayerRepository.save(gameplayer1);
		gamePlayerRepository.save(gameplayer2);
		gamePlayerRepository.save(gameplayer3);
		gamePlayerRepository.save(gameplayer4);
		gamePlayerRepository.save(gameplayer5);
		gamePlayerRepository.save(gameplayer6);

		Ship ship1 = new Ship("carrier", gameplayer2, Arrays.asList("A1","B1","C1","D1","E1"));
        Ship ship2 = new Ship("battleship", gameplayer1, Arrays.asList("G1","G2","G3","G4"));
        Ship ship3 = new Ship("submarine", gameplayer3 , Arrays.asList("D3","E3","F3"));
        Ship ship4 = new Ship("patrol boat", gameplayer1 , Arrays.asList("I3","J3"));
        Ship ship5 = new Ship("patrol boat", gameplayer4 , Arrays.asList("H3","H4"));
        shipRepository.save(ship1);
        shipRepository.save(ship2);
        shipRepository.save(ship3);
        shipRepository.save(ship4);
        shipRepository.save(ship5);

        Salvo salvo1 = new Salvo(1,gameplayer1, Arrays.asList("G1","G2","G3"));
        Salvo salvo2 = new Salvo(1,gameplayer2,Arrays.asList("A1","A2","A3"));
        Salvo salvo3 = new Salvo(2,gameplayer1, Arrays.asList("H2","H3","H4"));
        Salvo salvo4 = new Salvo(2,gameplayer2, Arrays.asList("G7","G8"));
        Salvo salvo5 = new Salvo(1, gameplayer3,Arrays.asList("H4","H5","H6"));
        Salvo salvo6 = new Salvo(1, gameplayer4, Arrays.asList("F2","F3","F4","F5"));
        salvoRepository.save(salvo1);
        salvoRepository.save(salvo2);
        salvoRepository.save(salvo3);
        salvoRepository.save(salvo4);
        salvoRepository.save(salvo5);
        salvoRepository.save(salvo6);

        Score score1 = new Score(LocalDateTime.now().plusHours(3) ,2.5,player1,game1);
        Score score2 = new Score(LocalDateTime.now().plusHours(3) ,0.5,player2,game1);
        scoreRepository.save(score1);
        scoreRepository.save(score2);
		};
	}
}
