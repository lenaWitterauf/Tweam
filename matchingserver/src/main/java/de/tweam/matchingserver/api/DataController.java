package de.tweam.matchingserver.api;


import de.tweam.matchingserver.api.ApiExceptionHandler.ApiException;
import de.tweam.matchingserver.data.*;
import de.tweam.matchingserver.matching.UserMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import twitter4j.TwitterException;

import java.util.List;
import java.util.Optional;

@RestController()
public class DataController {
    private final TeamRepository teamRepository;
    private final PersonRepository personRepository;
    private final UserDataService userDataService;
    private final UserMatcher userMatcher;

    @Autowired
    public DataController(TeamRepository teamRepository, PersonRepository personRepository, UserDataService userDataService, UserMatcher userMatcher) {
        this.teamRepository = teamRepository;
        this.personRepository = personRepository;
        this.userDataService = userDataService;
        this.userMatcher = userMatcher;
    }

    @GetMapping("/team/all")
    public List<Team> all() {
        return teamRepository.findAll();
    }

    @GetMapping("/team/{handle}")
    @ExceptionHandler(ApiException.class)
    public Team teamByHandle(@PathVariable String handle) {
        Optional<Team> team = teamRepository.findAll().stream().filter(team1 -> team1.getTeamPeople().stream().anyMatch(person -> person.getTwitterHandle().equals(handle))).findAny();
        if (!team.isPresent()) {
            boolean userExist = personRepository.findFirstByTwitterHandle(handle) != null;
            if (userExist) {
                throw new ApiException("Person has no Test");
            } else {
                throw new ApiException("Person Handle is not registered");
            }
        }
        return team.get();
    }

    @GetMapping("/user/{handle}")
    public Person userByHandle(@PathVariable String handle) {
        Person firstByTwitterHandle = personRepository.findFirstByTwitterHandle(handle);
        if (firstByTwitterHandle == null) {
            throw new ApiException("No Person with this Handle is registered");
        }
        return firstByTwitterHandle;
    }


    @PostMapping(path = "/user/create", consumes = "application/json")
    public Person createUser(@RequestBody CreateUser userToCreate) throws TwitterException {
        try {
            return userDataService.createUser(userToCreate.handle, userToCreate.getKeywordsLowercase());
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
