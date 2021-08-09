package com.codeoftheweb.salvo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String userName;

    @OneToMany(mappedBy="playerID", fetch=FetchType.EAGER)
    Set<GamePlayer> gameplayers;

    public Player() {}

    public Player(String userName) {
        this.userName = userName;
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public List<Game> getGames() {
        return gameplayers.stream().map(sub -> sub.getGameID()).collect(Collectors.toList());
    }

}