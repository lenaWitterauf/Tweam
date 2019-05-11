package de.tweam.matchingserver.data;

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

    @ElementCollection
    @CollectionTable(name="userKeywords", joinColumns=@JoinColumn(name="id"))
    @Column(name = "userKeywords")
    private List<String> userKeywords;


    @ElementCollection
    @CollectionTable(name = "userTweets", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "userTweets", columnDefinition = "TEXT")
    private List<String> userTweets;


    @ElementCollection
    @CollectionTable(name = "userFollowings", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "userFollowings")
    private List<Long> userFollowings;




    public Person() {
        userKeywords = new ArrayList<>();
    }

    public Person(String name, String twitterHandle, String imageUrl, List<String> userKeyWords) {
        this.userName = name;
        this.twitterHandle = twitterHandle;
        this.imageUrl = imageUrl;
        this.userKeywords = userKeyWords;
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
        return userKeywords;
    }

    public boolean addUserKeyword(String keyword) {
        return userKeywords.add(keyword);
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
