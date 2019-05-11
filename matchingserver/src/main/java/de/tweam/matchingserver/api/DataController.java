package de.tweam.matchingserver.api;


import de.tweam.matchingserver.api.ApiExceptionHandler.ApiException;
import de.tweam.matchingserver.data.Team;
import de.tweam.matchingserver.data.TeamRepository;
import de.tweam.matchingserver.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class DataController {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/team/all")
    private List<Team> all() {
        return teamRepository.findAll();
    }

    @GetMapping("/team/{handle}")
    @ExceptionHandler(ApiException.class)
    private Team all(@PathVariable String handle) {
        Team firstByTeamFor_twitterHandle = teamRepository.findFirstByTeamFor_TwitterHandle(handle);
        if (firstByTeamFor_twitterHandle == null) {
            boolean userExist = userRepository.findFirstByTwitterHandle(handle)!= null;
            if(userExist){
                throw new ApiException("User has no Test");
            }else {
                throw new ApiException("User Handle is not registered");
            }
        }
        return firstByTeamFor_twitterHandle;
    }
}
