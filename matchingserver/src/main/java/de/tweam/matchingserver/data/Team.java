package de.tweam.matchingserver.data;

import java.util.HashSet;
import java.util.Set;

public class Team {
    Set<User> teamUsers;
    User teamFor;
    public Team(User teamFor, Set members){
        this.teamUsers = members;
        this.teamFor = teamFor;
    }

    public Set<User> getTeamUsers() {
        return teamUsers;
    }

    public User getTeamFor() {
        return teamFor;
    }


}
