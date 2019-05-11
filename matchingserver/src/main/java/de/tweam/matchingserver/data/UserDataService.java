package de.tweam.matchingserver.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserDataService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public UserDataService(UserRepository userRepository, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }
}
