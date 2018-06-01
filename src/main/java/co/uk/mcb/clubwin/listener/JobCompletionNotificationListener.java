package co.uk.mcb.clubwin.listener;



import co.uk.mcb.clubwin.mail.ClubWinEmail;
import co.uk.mcb.clubwin.model.ClubWin;
import co.uk.mcb.clubwin.model.ClubWinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    JobCompletionNotificationListener(){}

    @Autowired
    ClubWinService clubWinService;

    @Override
    public void afterJob(JobExecution jobExecution){
        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            log.info("JOB FINISHED");
            processMail();
        }
    }

    /*
        Process Mails : iterate through the contact list and gets the valid codes and the mail is send voa the ClubWinEmail
    */
    public void processMail(){

        // TO BE IMPLEMENTED : TO GET FROM CMS SERVICE
        List<String> clubContactEmails = new ArrayList<String>();
        List<ClubWin> validClubWins = clubWinService.findValidClubWin();
        ClubWin validClubWin;
       for(int i = 0 ; i< clubContactEmails.size(); i++){
           validClubWin = validClubWins.get(i);
           new ClubWinEmail(validClubWin.getWinCode(),clubContactEmails.get(i)).send();
           setWinInValid(validClubWin);
       }
    }

    /*
    Sets win codes in valid once the mail is sent
     */
    private void setWinInValid(ClubWin clubWin){
        clubWin.setValid(false);
        clubWinService.saveClubWin(clubWin);
    }

}
