package de.tweam.matchingserver.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Component
public class UserDataService {

    private final UserRepository userRepository;

    @Autowired
    public UserDataService(UserRepository userRepository){
        this.userRepository = userRepository;
        HashSet<String> set = new HashSet<>();
        set.add("Hacktival");
        set.add("Backenddev");
        User testUser = new User("anotherUser", "@SchlottiP", set);
        userRepository.save(testUser);
    }
}
