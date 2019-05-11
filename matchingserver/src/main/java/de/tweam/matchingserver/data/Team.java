package de.tweam.matchingserver.data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    List<Person> teamPeople;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    Person teamFor;

    public Team(){

    }

    public Team(Person teamFor, List members) {
        this.teamPeople = members;
        this.teamFor = teamFor;
    }

    public List<Person> getTeamPeople() {
        return teamPeople;
    }

    public Person getTeamFor() {
        return teamFor;
    }


}
