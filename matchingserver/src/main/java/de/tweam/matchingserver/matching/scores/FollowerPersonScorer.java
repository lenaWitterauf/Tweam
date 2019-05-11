package de.tweam.matchingserver.matching.scores;

import de.tweam.matchingserver.data.Person;
import de.tweam.matchingserver.data.PersonRepository;
import de.tweam.matchingserver.twitter.follower.FollowingsReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FollowerPersonScorer implements PersonScorer {
    private final FollowingsReader followingsReader;
    private final PersonRepository personRepository;

    @Autowired
    public FollowerPersonScorer(FollowingsReader followingsReader, PersonRepository personRepository) {
        this.followingsReader = followingsReader;
        this.personRepository = personRepository;
    }

    @Override
    public double getUserScore(Person onePerson, Person otherPerson) throws TwitterException {
        maybeUpdateUserFollowings(onePerson);
        maybeUpdateUserFollowings(otherPerson);

        Set<Long> oneUserFollowings = new HashSet<>(onePerson.getUserFollowings());
        Set<Long> otherUserFollowings = new HashSet<>(otherPerson.getUserFollowings());

        if (oneUserFollowings.isEmpty() || otherUserFollowings.isEmpty()) {
            return -1;
        }

        int allFollowingsCount = oneUserFollowings.size() + otherUserFollowings.size();
        int similarFollowingsCount = oneUserFollowings.stream().filter(
                otherUserFollowings::contains
        ).collect(Collectors.toSet()).size() * 2;

        return (double) similarFollowingsCount / (double) allFollowingsCount;
    }

    private void maybeUpdateUserFollowings(Person person) throws TwitterException {
        if (person.getUserFollowings() == null || person.getUserFollowings().isEmpty()) {
            person.setUserFollowings(new ArrayList<>(followingsReader.read(person.getTwitterHandle())));
            personRepository.saveAndFlush(person);
        }
    }

}
