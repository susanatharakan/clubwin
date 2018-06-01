package co.uk.mcb.clubwin.mail

import com.sendinblue.Sendinblue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Primary
@Service
/*
 Third Party Service to send emails
 */
class SendinBlueService {
    private static final Logger logger = LoggerFactory.getLogger(SendinBlueService)
    private String fromEmail
    private Sendinblue sendinblue
    private String apiUrl = "https://api.sendinblue.com/v2.0"

    @Autowired
    SendinBlueService(
            @Value('${sendinblue.api_key}') String apiKey,
            @Value('${email.fromemail}') String fromEmail) {
        this.fromEmail = fromEmail
        this.sendinblue = new Sendinblue(apiUrl, apiKey)

    }

    /*
      Send method
      params : to - to address
      params : subject - subject of the mail
      params : text - text body of the mail
     */
    void send(String to, String subject, String text) {
        Map<String, String> toEmail = [:]
        toEmail.put(to, to)

        Map<String, Object> data = [:]
        data.put("to", toEmail)
        data.put("from", [fromEmail, fromEmail])
        data.put("subject", subject)
        data.put("text", text)
        logger.info(sendinblue.send_email(data))

    }

    /* Send method to multiple addresses
        params : to - List<> of addresses
        params : subject - subject of the mail
        params : text - text body of the mail
     */
    void send(List<String> to, String subject, String text) {
        Map<String, String> toEmail = [:]
        to.each {
            toEmail.put(it, it)
        }

        Map<String, Object> data = [:]
        data.put("to", toEmail)
        data.put("from", [fromEmail, fromEmail])
        data.put("subject", subject)
        data.put("text", text)
        logger.info(sendinblue.send_email(data))

    }

    /*
    sendHtml method sends mail in HTMl format
    params : to - to address
    params : subject
    params : html - html body content of the mail
     */
    void sendHtml(String to, String subject, String html) {
        Map<String, String> toEmail = [:]
        toEmail.put(to, to)

        Map<String, Object> data = [:]
        data.put("to", toEmail)
        data.put("from", [fromEmail, fromEmail])
        data.put("subject", subject)
        data.put("html", html)
        logger.info(sendinblue.send_email(data))

    }

    /*
     sendHtml method to send HTMl content to multiple addresses
     params : to - List<> to addresses
     params : subject - subject of the mail
     params : html content of the mail
     */
    void sendHtml(List<String> to, String subject, String html) {
        Map<String, String> toEmail = [:]
        to.each {
            toEmail.put(it, it)
        }

        Map<String, Object> data = [:]
        data.put("to", toEmail)
        data.put("from", [fromEmail, fromEmail])
        data.put("subject", subject)
        data.put("html", html)
        logger.info(sendinblue.send_email(data))
    }

    /*
    sendTemplate - method to send based on template Id
    params: - to - address
    params : templateId - integer value of the template Id
    substitutions : Map<>  - data
     */
    void sendTemplate(String to, Integer templateId, Map<String, String> substitutions) {
        def data = [:]

        data.put("id", templateId)
        data.put("to", to)
        data.put("attr", substitutions)

        logger.info(sendinblue.send_transactional_template(data))
    }
}
