package email;

//imports all neccessary classes from Java Mail API and JavaBeans Activation Framework
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class EmailVerification {
    //Attribute for the email verification code sent to the user
    private int code;
    //Mutator method that generates a 4-digit code and sets the attribute
    public void generateCode() {
        Random rand = new Random();
        code = rand.nextInt(9000) + 1000;
    }
    //Accessor method that returns the code value
    public int getCode() {
        return code;
    }
    //Instance method which sends an email to the user given their email address
    public void sendEmail(String email) {
        //Creates a properties object
        Properties properties = new Properties();
        //Declares variables
        final String from = "devopsfinancial@gmail.com";

        //Sets up properties for sending an email through a Gmail account using Simple Mail Transfer Protocol
        //It enables SMTP authentication, which means that the sender needs to provide valid credentials
        properties.put("mail.smtp.auth", "true");
        //This property ensures that the communication between the email client and the SMTP server is encrypted
        properties.put("mail.smtp.starttls.enable", "true");
        //This property specifies the hostname of the SMTP server to be used, which will be the Gmail SMTP
        properties.put("mail.smtp.host", "smtp.gmail.com");
        //This line sets the email address of the sender
        properties.put("mail.smtp.user", "devopsfinancial@gmail.com");
        properties.put("mail.smtp.port", 587);

        //Creates a session object with the specified properties and authenticator
        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("devopsfinancial@gmail.com", "orsqxidnsoiyunww");
            }
        });

        try {
            //Creates a MimeMessage object
            MimeMessage message = new MimeMessage(session);
            //Sets the email address of the sender
            message.setFrom(new InternetAddress(from));
            //Adds the recipient's email address to the message
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Sets the title of the email
            message.setSubject("This is your verification code");
            generateCode();
            //Sets the content of the email
            message.setText("Your 4 digit verification code is : " + code);
            //Sends the message using the Transport class
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to validate an email address
    public boolean validateEmail(String email){
        Pattern pattern;
        String emailRegex;
        //Checks if the email is null or empty
        if(email == null || email.isEmpty()){
            return false;
        }
        //Regular expression pattern for email validation
        emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@"+"(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";


        //Compiles the pattern into a regular expression object
        pattern = Pattern.compile(emailRegex);
        //Matches the pattern against the given email string
        if(pattern.matcher(email).matches()){
            return true;
        }else{
            return false;
        }
    }
}
