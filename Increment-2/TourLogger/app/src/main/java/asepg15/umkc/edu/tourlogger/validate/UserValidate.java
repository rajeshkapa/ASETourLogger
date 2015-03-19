package asepg15.umkc.edu.tourlogger.validate;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import asepg15.umkc.edu.tourlogger.R;
import asepg15.umkc.edu.tourlogger.dto.User;

/**
 * Created by Rajesh on 3/10/2015.
 */
public class UserValidate {

    public String validateRegisterUser(User user){
        if(user.firstName.isEmpty()){
            return buildMessage("first name");
        }else if(user.lastName.isEmpty()) {
            return buildMessage("last name");
        }
        else if(user.mobileNum.isEmpty()||!(user.mobileNum.trim().length()==10)||(user.mobileNum==null))
        {
            return buildMessage("valid mobile number");
        }
        else if (!isValidEmail(user.email))
            return buildMessage("valid email");
        else if (user.password.isEmpty()){
            return buildMessage("password");
        }
        else if(user.repPassword.isEmpty()){
            return "Please re-enter Password";
        }
        else if (!(user.password.length()>=8)){
            return buildMessage("password");
        }
        else if(!user.password.equals(user.repPassword))
            return buildMessage("Please re-enter Correct Password");

        return "ok";
    }

    private String buildMessage(String Arg1){
        return "Please enter a "+Arg1;
    }

    public String validateLoginUser(String username,String password) {
        if(username.isEmpty())
            return buildMessage("username");
        else if (password.isEmpty())
        {
            return buildMessage("password");
        }
        return  "ok";
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }



}
