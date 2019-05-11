package de.tweam.matchingserver.data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String twitterHandle;
    @Transient //FIXME, remove this later
    private Set<String> userKeywords;

    public User(){
        userKeywords = new HashSet<>();
    }

    public User(String name, String twitterHandle,Set userKeyWords) {
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

    public Set<String> getUserKeywords() {
        return userKeywords;
    }

    public boolean addUserKeyword(String keyword){
        return userKeywords.add(keyword);
    }
}
