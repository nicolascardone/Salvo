package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
		Player player2 = new Player("nicolascardone@gmail.com", passwordEncoder().encode("123"));
		Player player1 = new Player("ncardone@pioix.edu.ar", passwordEncoder().encode("ABC"));
		Player player3 = new Player("juanitraga@pioix.edu.ar", passwordEncoder().encode("APO"));
		Player player4 = new Player("leandro@pioix.edu.ar", passwordEncoder().encode("523"));
		Player player5 = new Player("messi@pioix.edu.ar", passwordEncoder().encode("B12"));
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


        //Salvo salvo1 = new Salvo(1,gameplayer1, Arrays.asList("G1","G2","G3"));
            // Salvo salvo2 = new Salvo(1,gameplayer2,Arrays.asList("A1","A2","A3"));
            //Salvo salvo3 = new Salvo(2,gameplayer1, Arrays.asList("H2","H3","H4"));
            //Salvo salvo4 = new Salvo(2,gameplayer2, Arrays.asList("G7","G8"));
            //Salvo salvo5 = new Salvo(1, gameplayer3,Arrays.asList("H4","H5","H6"));
            //Salvo salvo6 = new Salvo(1, gameplayer4, Arrays.asList("F2","F3","F4","F5"));
            //salvoRepository.save(salvo1);
            //salvoRepository.save(salvo2);
            //salvoRepository.save(salvo3);
            //salvoRepository.save(salvo4);
            //salvoRepository.save(salvo5);
            //salvoRepository.save(salvo6);


		};
	}
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName -> {
            Player player = playerRepository.findByUserName(inputName);
            if (player != null) {
                return new User(player.getUserName(), player.getPassword(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
}
@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
     private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/game_view/**").hasAuthority("USER")
                .antMatchers("/web/games.html").permitAll()
                .antMatchers("/api/games").permitAll()
                .antMatchers("/h2-console/").permitAll()
                .and().headers().frameOptions().disable()
                .and().csrf().ignoringAntMatchers("/h2-console/")
                .and()
                .cors().disable();

        http.formLogin()
                .usernameParameter("name")
                .passwordParameter("pwd")
                .loginPage("/api/login")
                ;
        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}

