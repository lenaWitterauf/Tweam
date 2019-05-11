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

    @ElementCollection
    @CollectionTable(name="userKeywords", joinColumns=@JoinColumn(name="id"))
    @Column(name="userkeywords")
    private List<String> userKeywords;

    public Person() {
        userKeywords = new ArrayList<>();
    }

    public Person(String name, String twitterHandle, List<String> userKeyWords) {
        this.userName = name;
        this.twitterHandle = twitterHandle;
        this.userKeywords = userKeyWords;
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

    public Long getId() {
        return id;
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
