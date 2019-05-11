package de.tweam.matchingserver.twitter.follower;

import de.tweam.matchingserver.twitter.TwitterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.HashSet;
import java.util.Set;

@Component
public class FollowingsReader {
    private final TwitterProvider twitterProvider;

    @Autowired
    public FollowingsReader(TwitterProvider twitterProvider) {
        this.twitterProvider = twitterProvider;
    }

    public Set<Long> read(String username) throws TwitterException {
        Twitter twitter = twitterProvider.getTwitterInstance();
        Set<Long> followings = new HashSet<>();
        IDs followingsIDs;
        long cursor = -1;
        do {
            followingsIDs = twitter.getFriendsIDs(username, cursor);
            long[] followingsIDsArray = followingsIDs.getIDs();
            for (long followingsID : followingsIDsArray) {
                followings.add(followingsID);
            }
            cursor = followingsIDs.getNextCursor();
        } while (followingsIDs.hasNext());

        return followings;
    }
}
