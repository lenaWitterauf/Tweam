package de.tweam.matchingserver.data;

import de.tweam.matchingserver.twitter.user.UserReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.List;


@Component
public class UserDataService {

    private final PersonRepository personRepository;
    private final TeamRepository teamRepository;
    private final UserReader twitterUserProvider;

    @Autowired
    public UserDataService(PersonRepository personRepository, TeamRepository teamRepository, UserReader twitterProvider) {
        this.personRepository = personRepository;
        this.teamRepository = teamRepository;
        this.twitterUserProvider = twitterProvider;
    }


    public Person createUser(String handle, List<String> keywords) throws TwitterException {
        User read = twitterUserProvider.read(handle);
        Person person = new Person(read.getName(),handle, read.get400x400ProfileImageURL(), keywords);
        personRepository.saveAndFlush(person);
        return person;
    }
}
