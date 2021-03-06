package de.tweam.matchingserver.matching;

import de.tweam.matchingserver.data.Person;
import de.tweam.matchingserver.data.PersonRepository;
import de.tweam.matchingserver.data.Team;
import de.tweam.matchingserver.data.TeamRepository;
import de.tweam.matchingserver.matching.scores.PersonScorer;
import de.tweam.matchingserver.twitter.follower.FollowingsReader;
import de.tweam.matchingserver.twitter.tweets.TweetContentReader;
import de.tweam.matchingserver.twitter.user.UserReader;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserMatcher {
    private static final Logger logger = LoggerFactory.getLogger(UserMatcher.class);
    private static final long updateIntervalMillis = 1000 * 60 * 60 * 24;

    private final PersonScorer personScorer;
    private final TeamRepository teamRepository;

    private final TweetContentReader tweetContentReader;
    private final FollowingsReader followingsReader;
    private final UserReader userReader;

    private final PersonRepository personRepository;


    @Autowired
    public UserMatcher(@Qualifier("combinedPersonScorer") PersonScorer personScorer,
                       TeamRepository teamRepository, PersonRepository personRepository,
                       TweetContentReader tweetContentReader, FollowingsReader followingsReader, UserReader userReader) {
        this.personScorer = personScorer;
        this.teamRepository = teamRepository;
        this.tweetContentReader = tweetContentReader;
        this.followingsReader = followingsReader;
        this.personRepository = personRepository;
        this.userReader = userReader;
    }

    public List<Team> calculateTeams(List<Person> personsToMatch, int teamSize) {
        ArrayList<Person> arrayPersonsToMatch = new ArrayList<>(maybeUpdatePersons(personsToMatch));
        double[][] matching = calculatePerPersonMatchings(arrayPersonsToMatch);
        int numberOfTeams = (int) Math.ceil((double) personsToMatch.size() / teamSize);
        Map<Person, Team> teamsPerPerson = new HashMap<>();
        int teamsCreated = 0;
        while (teamsPerPerson.size() < arrayPersonsToMatch.size()) {
            Pair<Integer, Integer> nextUserPairIndex = findNextLowestMaxUserIndex(matching);
            if (nextUserPairIndex.getKey() == -1 || nextUserPairIndex.getValue() == -1) {
                if (teamsCreated < numberOfTeams) {
                    Team team = teamRepository.saveAndFlush(new Team(new ArrayList<>()));
                    arrayPersonsToMatch.removeAll(teamsPerPerson.keySet());
                    team.getTeamPeople().add(arrayPersonsToMatch.get(0));
                    teamsPerPerson.put(arrayPersonsToMatch.get(0), team);
                    teamsCreated++;
                }
                break;
            }

            Person firstPerson = arrayPersonsToMatch.get(nextUserPairIndex.getKey());
            Person otherPerson = arrayPersonsToMatch.get(nextUserPairIndex.getValue());
            matching[nextUserPairIndex.getKey()][nextUserPairIndex.getValue()] = -1;
            matching[nextUserPairIndex.getValue()][nextUserPairIndex.getKey()] = -1;

            if (teamsPerPerson.containsKey(firstPerson) && teamsPerPerson.get(firstPerson).getTeamPeople().size() >= teamSize
                    || teamsPerPerson.containsKey(otherPerson) && teamsPerPerson.get(otherPerson).getTeamPeople().size() >= teamSize
                    || teamsPerPerson.containsKey(firstPerson) && teamsPerPerson.containsKey(otherPerson)
                    || !teamsPerPerson.containsKey(firstPerson) && !teamsPerPerson.containsKey(otherPerson) && teamsCreated >= numberOfTeams) {
                continue;
            }

            if (!teamsPerPerson.containsKey(firstPerson) && !teamsPerPerson.containsKey(otherPerson)) {
                Team team = teamRepository.saveAndFlush(new Team(new ArrayList<>()));
                team.getTeamPeople().add(firstPerson);
                teamsPerPerson.put(firstPerson, team);
                teamsCreated++;
            }

            if (teamsPerPerson.containsKey(firstPerson)) {
                teamsPerPerson.get(firstPerson).getTeamPeople().add(otherPerson);
                teamsPerPerson.put(otherPerson, teamsPerPerson.get(firstPerson));
            } else {
                teamsPerPerson.get(otherPerson).getTeamPeople().add(firstPerson);
                teamsPerPerson.put(firstPerson, teamsPerPerson.get(otherPerson));
            }
        }

        return saveTeamStates(teamsPerPerson.values());
    }

    private List<Team> saveTeamStates(Collection<Team> teams) {
        Set<Team> distinctTeams = new HashSet<>(teams);
        distinctTeams.forEach(teamRepository::saveAndFlush);
        return new ArrayList<>(distinctTeams);
    }

    private Pair<Integer, Integer> findNextLowestMaxUserIndex(double[][] matching) {
        int minMaxUserIndex = -1;
        int minMaxUserIndexPartner = -1;
        double minMaxRank = Double.MAX_VALUE;

        for (int i = 0; i < matching.length; i++) {
            double maxRankForIndex = -1;
            int maxRankingPartner = -1;

            for (int j = 0; j < matching.length; j++) {
                if (matching[i][j] < 0) { // negative score indicates no matching possible
                    continue;
                }

                if (matching[i][j] > maxRankForIndex) {
                    maxRankForIndex = matching[i][j];
                    maxRankingPartner = j;
                }
            }

            if (maxRankForIndex == -1) { // no match possible for this user
                continue;
            }

            if (maxRankForIndex < minMaxRank) {
                minMaxRank = maxRankForIndex;
                minMaxUserIndex = i;
                minMaxUserIndexPartner = maxRankingPartner;
            }
        }

        return new Pair<Integer, Integer>(minMaxUserIndex, minMaxUserIndexPartner);
    }

    private double[][] calculatePerPersonMatchings(ArrayList<Person> personsToMatch) {
        double[][] matching = new double[personsToMatch.size()][personsToMatch.size()];
        for (int i = 0; i < personsToMatch.size(); i++) {
            Person person = personsToMatch.get(i);
            matching[i][i] = -1;
            for (int j = i + 1; j < personsToMatch.size(); j++) {
                Person otherPerson = personsToMatch.get(j);
                double score;
                try {
                    score = personScorer.getUserScore(person, otherPerson);
                } catch (TwitterException exception) {
                    logger.error("Error matching users!", exception);
                    score = 0;
                }

                matching[i][j] = score;
                matching[j][i] = score;
            }
        }

        return matching;
    }

    private List<Person> maybeUpdatePersons(List<Person> persons) {
        final long currentTimestamp = System.currentTimeMillis();
        return persons.stream().map(person -> {
            if (person.getLastUpdateTimestamp() + updateIntervalMillis < currentTimestamp) {
                try {
                    person.setUserTweets(tweetContentReader.readTweetContents(person.getTwitterHandle()));
                    person.setUserFollowings(new ArrayList<>(followingsReader.read(person.getTwitterHandle())));
                    Status status = userReader.read(person.getTwitterHandle()).getStatus();
                    if (status != null) {
                        person.setStatusId(String.valueOf(status.getId()));
                    }
                    person.setLastUpdateTimestamp(currentTimestamp);
                    return personRepository.saveAndFlush(person);
                } catch (TwitterException exception) {
                    logger.error("Error when updating user", exception);
                }
            }
            return person;
        }).collect(Collectors.toList());
    }

}
