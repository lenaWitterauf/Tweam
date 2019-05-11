package de.tweam.matchingserver.matching.scores;

import de.tweam.matchingserver.data.Person;
import de.tweam.matchingserver.twitter.follower.FollowingsReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FollowerPersonScorer implements PersonScorer {
    private final FollowingsReader followingsReader;

    @Autowired
    public FollowerPersonScorer(FollowingsReader followingsReader) {
        this.followingsReader = followingsReader;
    }

    @Override
    public double getUserScore(Person onePerson, Person otherPerson) throws TwitterException {
        Set<Long> oneUserFollowings = followingsReader.read(onePerson.getTwitterHandle());
        Set<Long> otherUserFollowings = followingsReader.read(otherPerson.getTwitterHandle());

        if (oneUserFollowings.isEmpty() || otherUserFollowings.isEmpty()) {
            return -1;
        }

        int allFollowingsCount = oneUserFollowings.size() + otherUserFollowings.size();
        int similarFollowingsCount = oneUserFollowings.stream().filter(
                otherUserFollowings::contains
        ).collect(Collectors.toSet()).size() * 2;

        return (double) similarFollowingsCount / (double) allFollowingsCount;
    }

}
