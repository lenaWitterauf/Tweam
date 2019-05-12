package de.tweam.matchingserver.twitter.bot;

import de.tweam.matchingserver.data.BotData;
import de.tweam.matchingserver.data.BotDataRepository;
import de.tweam.matchingserver.matching.UserMatcher;
import de.tweam.matchingserver.twitter.TwitterProvider;
import de.tweam.matchingserver.twitter.tweets.TweetReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

import java.util.List;

public class BotRoutine implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(UserMatcher.class);
    private static final String statusText = "Hey @%s! I see you're attending #Hacktival today! Try Tweam to find your awesome team - tweam.tk";

    private final BotDataRepository botDataRepository;
    private final TweetReader tweetReader;
    private final TwitterProvider twitterProvider;

    public BotRoutine(BotDataRepository botDataRepository, TweetReader tweetReader, TwitterProvider twitterProvider) {
        this.botDataRepository = botDataRepository;
        this.tweetReader = tweetReader;
        this.twitterProvider = twitterProvider;
    }

    @Override
    public void run() {
        BotData botData = botDataRepository.findAll().get(0);
        try {
            List<Status> statusWithHashtag = tweetReader.readForHashtag(botData.getHashtag());
            if (statusWithHashtag == null || statusWithHashtag.isEmpty()) {
                logger.error("No tweets with hashtag " + botData.getHashtag());
                return;
            }

            statusWithHashtag.stream()
                    .filter(status -> !botData.getTweetsAnswered().contains(status.getId()))
                    .forEach(status -> respondToStatus(botData, status));
        } catch (TwitterException exception) {
            logger.error("Twitter Exception, oh no!", exception);
        }

        botDataRepository.saveAndFlush(botData);
    }

    private void respondToStatus(BotData botData, Status status) {
        try {
            StatusUpdate statusUpdate = new StatusUpdate(String.format(statusText, status.getUser().getScreenName()));
            statusUpdate.setInReplyToStatusId(status.getId());
            twitterProvider.getTwitterInstance().updateStatus(statusUpdate);
            botData.getTweetsAnswered().add(status.getId());
            botData.getUsersAnswered().add(status.getUser().getScreenName());
        } catch (TwitterException exception) {
            logger.error("Error sending status update", exception);
        }
    }
}
