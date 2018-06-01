package co.uk.mcb.clubwin.mail

import co.uk.mcb.cms.form.Club
import hu.meruem.ServiceLocator

/**
 * sendinblue 1199 -templateId , sents mail to Club contactEmail  when the club is activated
 */
class ClubWinEMail {
    private Club club

    final String to
    final int templateId = 1199
    final Map substitutions = [:]

    ClubWinEMail(Club club) {
        this.club = club
        to = club.contactEmail

        substitutions.put("FIRST_NAME", club.contactFirstName)
        substitutions.put("BETTING_URL", club.desiredUrl)
    }

    def send() {
        def service = ServiceLocator.getBean(SendinBlueService)
        service.sendTemplate(to, templateId, substitutions)
    }
}
