package asepg15.umkc.edu.tourlogger.dto;

/**
 * Created by Rajesh on 3/10/2015.
 */
public class User {

    public String firstName;
    public String lastName;
    public String mobileNum;
    public String email;
    public String password;
    public String repPassword;

    public User(String firstName,
                String lastName,
                String mobileNum,
                String email,
                String password,
                String repPassword)
    {
        this.firstName=firstName;
        this.lastName=lastName;
        this.mobileNum=mobileNum;
        this.email=email;
        this.password=password;
        this.repPassword=repPassword;
    }


}
