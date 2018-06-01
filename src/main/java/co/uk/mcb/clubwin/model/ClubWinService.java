package co.uk.mcb.clubwin.model;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubWinService {

    private ClubWinRepo clubWinRepo;

    ClubWinService(ClubWinRepo clubWinRepo){
        this.clubWinRepo = clubWinRepo;
    }

    void saveClubWin(ClubWin clubWin){
        clubWinRepo.save(clubWin);
    }

    List<ClubWin> findAll(){
       return clubWinRepo.findAll();
    }


    List<ClubWin> findValidClubWin(){
        return clubWinRepo.findValidWinClub();
    }


}
