
package bo.com.tesla.useful.cross;


import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 *
 * @author acallejas
 */
@Service
public class SendEmail {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public SendEmail(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

  

    public void sendHTML(String from, String to, String assunto, String body) {
        try {        	
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(assunto);
            helper.setText(body, true);
            mailSender.send(mail);
        } catch (Exception e) {
           e.printStackTrace();
            System.out.println("ERROR EN ENVIO SMS");
            System.out.println(e.toString());
        }
    }

    
    
   

}
