package co.uk.mcb.clubwin.listener;



import co.uk.mcb.clubwin.ServiceLocator;
import co.uk.mcb.clubwin.mail.ClubWinEMail;
import co.uk.mcb.clubwin.model.ClubWin;
import co.uk.mcb.clubwin.model.ClubWinRepo;
import co.uk.mcb.clubwin.model.ClubWinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    JobCompletionNotificationListener(){}

    @Autowired
    ClubWinService clubWinService;

    @Autowired
    ClubWinRepo clubWinRepo;

    @Override
    public void afterJob(JobExecution jobExecution){
        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            log.info("JOB FINISHED");
            processMail();
        }
    }


    public void processMail(){

        // TO BE IMPLEMENTED : TO GET FROM CMS SERVICE
        List<String> clubContactEmails = new ArrayList<String>();
        List<ClubWin> validClubWins = clubWinRepo.findValidWinClub();
        ClubWin validClubWin;
       for(int i = 0 ; i< clubContactEmails.size(); i++){
           validClubWin = validClubWins.get(i);
           new ClubWinEMail(validClubWin.getWinCode(),clubContactEmails.get(i)).send();
           setWinInValid(validClubWin);
       }
    }

    private void setWinInValid(ClubWin clubWin){
        clubWin.setValid(false);
        clubWinRepo.save(clubWin);
    }

}
