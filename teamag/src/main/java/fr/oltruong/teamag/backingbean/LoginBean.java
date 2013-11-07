package fr.oltruong.teamag.backingbean;

import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.exception.UserNotFoundException;
import fr.oltruong.teamag.qualifier.UserLogin;
import fr.oltruong.teamag.utils.Constants;

@ManagedBean
@SessionScoped
public class LoginBean extends Controller {

    @Inject
    private Logger logger;

    @Inject
    private MemberEJB memberEJB;

    @Inject
    private HttpServletRequest servletRequest;

    private Member member;

    private String username;

    private String password;

    public String getLoggedUserName() {
        if (getMember() == null) {
            return "Not loggedIn";
        } else {
            return getMember().getName();
        }
    }

    public boolean isLoggedIn() {
        return getMember() != null;
    }

    public boolean isNotLoggedIn() {
        return !isLoggedIn();
    }

    public boolean isAdministrator() {
        return getMember() != null && getMember().isAdministrator();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Member getMember() {
        if (member == null) {
            member = getMemberFromSession();
        }
        return member;
    }

    @Produces
    @UserLogin
    public Member getMemberFromSession() {

        if (servletRequest == null || servletRequest.getSession() == null) {
            return null;
        }

        return (Member) servletRequest.getSession().getAttribute(Constants.USER);

    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String login() {

        logger.info("Login={}", username);

        FacesMessage userMessage = null;
        Member member;
        try {
            member = memberEJB.findByName(username);
            logger.info(member.getName() + " found");
            userMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("welcome", username), "");
            setMember(member);
            getMember();
            servletRequest.getSession().setAttribute(Constants.USER, member);
            FacesContext.getCurrentInstance().addMessage(null, userMessage);
            return "welcome";
        } catch (UserNotFoundException e) {
            logger.warn("[" + username + "] not found");
            userMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("unknown", username), getMessage("tryagain"));

            FacesContext.getCurrentInstance().addMessage(null, userMessage);
            return "login.xhtml";
        }

    }

    public String logout() {

        logger.info("Logging out");
        setMember(null);
        servletRequest.getSession().setAttribute(Constants.USER, null);

        FacesMessage userMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("farewell", username), getMessage("seeYou"));

        FacesContext.getCurrentInstance().addMessage(null, userMessage);
        return "welcome.xhtml";
    }

}
