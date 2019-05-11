package de.tweam.matchingserver.api;


import de.tweam.matchingserver.api.ApiExceptionHandler.ApiException;
import de.tweam.matchingserver.data.*;
import de.tweam.matchingserver.matching.UserMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import twitter4j.TwitterException;

import java.util.List;

@RestController()
public class DataController {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private UserMatcher userMatcher;

    @GetMapping("/team/all")
    private List<Team> all() {
        return teamRepository.findAll();
    }

    @GetMapping("/team/{handle}")
    @ExceptionHandler(ApiException.class)
    private Team teambyHandle(@PathVariable String handle) {
        Team firstByTeamFor_twitterHandle = teamRepository.findFirstByTeamFor_TwitterHandle(handle);
        if (firstByTeamFor_twitterHandle == null) {
            boolean userExist = personRepository.findFirstByTwitterHandle(handle) != null;
            if (userExist) {
                throw new ApiException("Person has no Test");
            } else {
                throw new ApiException("Person Handle is not registered");
            }
        }
        return firstByTeamFor_twitterHandle;
    }

    @GetMapping("/user/{handle}")
    private Person userByHandle(@PathVariable String handle) {
        Person firstByTwitterHandle = personRepository.findFirstByTwitterHandle(handle);
        if (firstByTwitterHandle == null) {
            throw new ApiException("No Person with this Handle is registered");
        }
        return firstByTwitterHandle;
    }


    @PostMapping(path = "/user/create", consumes = "application/json")
    private Person createUser(@RequestBody CreateUser userToCreate) throws TwitterException {
        try {
            return userDataService.createUser(userToCreate.handle, userToCreate.keywords);
        } catch (TwitterException e) {
            if (e.resourceNotFound()) {
                throw new ApiException("User-Handle not Found");
            } else {
                throw e;
            }
        }

    }

    @RequestMapping(path = "/remap")
    public void remap(@RequestParam(defaultValue = "3", name = "size") int size) {
        teamRepository.deleteAll();
        userMatcher.calculateTeams(personRepository.findAll(), size);
    }

}
