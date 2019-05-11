package de.tweam.matchingserver.data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    List<User> teamUsers;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    User teamFor;

    public Team(User teamFor, List members) {
        this.teamUsers = members;
        this.teamFor = teamFor;
    }

    public List<User> getTeamUsers() {
        return teamUsers;
    }

    public User getTeamFor() {
        return teamFor;
    }


}
