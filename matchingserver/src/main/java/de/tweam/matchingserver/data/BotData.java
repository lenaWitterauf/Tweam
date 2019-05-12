package de.tweam.matchingserver.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_bot")
public class BotData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tweetsAnswered", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "tweetsAnswered")
    private List<Long> tweetsAnswered;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usersAnswered", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "usersAnswered")
    private Set<String> usersAnswered;

    private String hashtag;


    public BotData() {
        this.tweetsAnswered = new ArrayList<>();
        this.usersAnswered = new HashSet<>();
    }

    public BotData(String hashtag) {
        this(new ArrayList<>(), new HashSet<>(), hashtag);
    }

    public BotData(List<Long> tweetsAnswered, Set<String> usersAnswered, String hashtag) {
        this.tweetsAnswered = tweetsAnswered;
        this.usersAnswered = usersAnswered;
        this.hashtag = hashtag;
    }

    public List<Long> getTweetsAnswered() {
        return tweetsAnswered;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public Set<String> getUsersAnswered() {
        return usersAnswered;
    }
}
