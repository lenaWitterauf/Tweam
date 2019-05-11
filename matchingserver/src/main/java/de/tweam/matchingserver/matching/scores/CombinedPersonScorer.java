package de.tweam.matchingserver.matching.scores;

import de.tweam.matchingserver.data.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CombinedPersonScorer implements PersonScorer {
    private static final Logger logger = LoggerFactory.getLogger(CombinedPersonScorer.class);

    private final FollowerPersonScorer followerPersonScorer;
    private final TweetContentPersonScorer tweetContentPersonScorer;

    @Autowired
    public CombinedPersonScorer(FollowerPersonScorer followerPersonScorer, TweetContentPersonScorer tweetContentPersonScorer) {
        this.followerPersonScorer = followerPersonScorer;
        this.tweetContentPersonScorer = tweetContentPersonScorer;
    }

    @Override
    public double getUserScore(Person onePerson, Person otherPerson) {
        double tweetContentScore = tweetContentPersonScorer.getUserScore(onePerson, otherPerson);
        double followerPersonScore = followerPersonScorer.getUserScore(onePerson, otherPerson);
        if (tweetContentScore == -1 && followerPersonScore == -1) {
            logger.error("No information for both users :-(");
            return 0;
        }

        if (tweetContentScore == -1) {
            return followerPersonScore;
        }

        if (followerPersonScore == -1) {
            return tweetContentScore;
        }

        return (followerPersonScore + tweetContentScore) / 2;
    }
}
