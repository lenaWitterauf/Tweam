package de.tweam.matchingserver.twitter.bot;

import de.tweam.matchingserver.data.BotData;
import de.tweam.matchingserver.data.BotDataRepository;
import de.tweam.matchingserver.data.Person;
import de.tweam.matchingserver.data.Team;
import de.tweam.matchingserver.twitter.TwitterProvider;
import de.tweam.matchingserver.twitter.tweets.TweetReader;
import de.tweam.matchingserver.twitter.user.UserReader;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class BotManagement {
    private static final Log logger = LogFactory.getLog(BotManagement.class);
    private static final String teamSuccessText = "Hey! You are a team :-) ";

    private final BotDataRepository botDataRepository;
    private final TwitterProvider twitterProvider;
    private final TweetReader tweetReader;
    private final UserReader userReader;
    private final ScheduledExecutorService scheduler;

    @Autowired
    public BotManagement(BotDataRepository botDataRepository,
                         TwitterProvider twitterProvider,
                         TweetReader tweetReader,
                         UserReader userReader) {
        this.botDataRepository = botDataRepository;
        this.twitterProvider = twitterProvider;
        this.tweetReader = tweetReader;
        this.userReader = userReader;
        this.scheduler = Executors.newScheduledThreadPool(1);

        start();
    }

    public void start() {
        initializeBotData();
        scheduler.scheduleAtFixedRate(new BotRoutine(botDataRepository, tweetReader, twitterProvider), 0, 15, TimeUnit.MINUTES);
    }

    private void initializeBotData() {
        List<BotData> botData = botDataRepository.findAll();
        if (botData == null || botData.isEmpty()) {
            BotData createdBotData = new BotData("hacktival_tweam");
            botDataRepository.saveAndFlush(createdBotData);
        }
    }

    public void stop() throws InterruptedException {
        scheduler.shutdown();
        scheduler.awaitTermination(10, TimeUnit.SECONDS);
    }

    public void communicateMatching(List<Team> matchedTeams) {
        BotData botData = botDataRepository.findAll().get(0);

        for (Team team : matchedTeams) {
            List<Person> personsConnected = team.getTeamPeople().stream().filter(
                    person -> botData.getUsersAnswered().contains(person.getTwitterHandle())
            ).collect(Collectors.toList());

            if (personsConnected.isEmpty()) {
                continue;
            }

            String personLinkText = personsConnected.stream().map(person -> " @" + person.getTwitterHandle()).collect(Collectors.joining());
            try {
                twitterProvider.getTwitterInstance().updateStatus(teamSuccessText + personLinkText);
            } catch (TwitterException exception) {
                logger.error("Error sending tweet for users " + personLinkText, exception);
            }
        }
    }
}
