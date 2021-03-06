package de.tweam.matchingserver.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tbl_user")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    @Column(unique = true)
    private String twitterHandle;

    private String imageUrl;

    @ColumnDefault("-1")
    private String statusId;

    @ColumnDefault("0")
    private long lastUpdateTimestamp;

    @ElementCollection
    @CollectionTable(name="userKeywords", joinColumns=@JoinColumn(name="id"))
    @Column(name = "userKeywords", columnDefinition = "TEXT")
    private List<String> userKeywords;


    @ElementCollection
    @CollectionTable(name = "userTweets", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "userTweets", columnDefinition = "TEXT")
    @JsonIgnore
    private List<String> userTweets;


    @ElementCollection
    @CollectionTable(name = "userFollowings", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "userFollowings")
    @JsonIgnore
    private List<Long> userFollowings;


    public Person() {
        userKeywords = new ArrayList<>();
    }

    public Person(String name, String twitterHandle, String imageUrl, String statusId, List<String> userKeyWords) {
        this.userName = name;
        this.twitterHandle = twitterHandle;
        this.imageUrl = imageUrl;
        this.userKeywords = userKeyWords;
        this.statusId = statusId;
        this.userTweets = new ArrayList<>();
        this.userFollowings = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public List<String> getUserKeywords() {
        if(userKeywords == null){
            userKeywords = new ArrayList<>();
        }
        return userKeywords;
    }

    public boolean addUserKeyword(String keyword) {
        return userKeywords.add(keyword);
    }

    public void setUserKeywords(List<String> userKeywords) {
        this.userKeywords = userKeywords;
    }

    public List<String> getUserTweets() {
        return userTweets;
    }

    public List<Long> getUserFollowings() {
        return userFollowings;
    }

    public Long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public void setUserFollowings(List<Long> userFollowings) {
        this.userFollowings = userFollowings;
    }


    public void setUserTweets(List<String> userTweets) {
        this.userTweets = userTweets;
    }


    public long getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(long lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
