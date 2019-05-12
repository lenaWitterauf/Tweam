package de.tweam.matchingserver.data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tbl_team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<Person> teamPeople;

    public Team(){
    }

    public Team( List<Person> members) {
        this.teamPeople = members;
    }

    public List<Person> getTeamPeople() {
        return teamPeople;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id);
    }

    public Long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
