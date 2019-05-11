package de.tweam.matchingserver.twitter.follower;

import de.tweam.matchingserver.twitter.TwitterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

@Component
public class FollowerReader {
    private final TwitterProvider twitterProvider;

    @Autowired
    public FollowerReader(TwitterProvider twitterProvider) {
        this.twitterProvider = twitterProvider;
    }

    public List<Long> read(String username) throws TwitterException {
        Twitter twitter = twitterProvider.getTwitterInstance();
        List<Long> followers = new ArrayList<>();
        IDs followersIDs;
        long cursor = -1;
        do {
            followersIDs = twitter.getFollowersIDs(username, cursor);
            long[] followersIDsArray = followersIDs.getIDs();
            for (long followersID : followersIDsArray) {
                followers.add(followersID);
            }
            cursor = followersIDs.getNextCursor();
        } while (followersIDs.hasNext());

        return followers;
    }
}
