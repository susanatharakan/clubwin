package co.uk.mcb.clubwin.mail

import co.uk.mcb.clubwin.ServiceLocator
import co.uk.mcb.clubwin.model.ClubWin
import org.springframework.stereotype.Service

/**
 *
 */
class ClubWinEMail {
    private ClubWin clubWin

    final String to
    final int templateId = ""
    final Map substitutions = [:]

    ClubWinEMail(String clubWinUrl, String to) {

        substitutions.put("CLUBWIN_URL", "http://myclubwin.co.uk/"+clubWinUrl);
    }

    def send() {
        def service = ServiceLocator.getBean(SendinBlueService)
        service.sendTemplate(to, templateId, substitutions)
    }
}
