package de.tweam.matchingserver.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findFirstByTeamFor_TwitterHandle(String twitterHandle);
}
