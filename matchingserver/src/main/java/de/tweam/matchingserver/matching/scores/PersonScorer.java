package de.tweam.matchingserver.matching.scores;

import de.tweam.matchingserver.data.Person;
import twitter4j.TwitterException;

public interface PersonScorer {
    double getUserScore(Person onePerson, Person otherPerson) throws TwitterException;
}
