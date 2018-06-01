package co.uk.mcb.clubwin.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClubWinRepo extends JpaRepository<ClubWin, Long> {

    List<ClubWin> findAll();

    @Query("select cw from ClubWin cw where valid is true")
    List<ClubWin> findValidWinClub();

}
