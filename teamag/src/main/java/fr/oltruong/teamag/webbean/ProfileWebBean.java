package fr.oltruong.teamag.webbean;


import com.google.common.base.Preconditions;
import fr.oltruong.teamag.entity.Member;

/**
 * @author Olivier Truong
 */
public class ProfileWebBean {

    private Member member;

    private String name;

    private String company;

    private String email;

    private String password;

    private String passwordAgain;

    public ProfileWebBean(Member member) {
        Preconditions.checkArgument(member != null);

        this.member = member;
        this.name = member.getName();
        this.company = member.getCompany();
        this.email = member.getEmail();

    }

    public Member getMember() {
        return member;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordAgain() {
        return passwordAgain;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }
}