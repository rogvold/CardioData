package com.cardiodata.managers;

import com.cardiodata.core.jpa.ApiToken;
import com.cardiodata.core.jpa.User;
import com.cardiodata.core.jpa.UserAccount;
import com.cardiodata.enums.AccountStatusEnum;
import com.cardiodata.enums.AccountTypeEnum;
import com.cardiodata.enums.UserRoleEnum;
import com.cardiodata.enums.UserStatusEnum;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.ResponseConstants;
import com.cardiodata.json.TokenManagerLocal;
import com.cardiodata.utils.StringUtils;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author rogvold
 */
@Stateless
public class UserManager implements UserManagerLocal {

    @PersistenceContext(unitName = "CardioDataCorePU")
    EntityManager em;
    @EJB
    TokenManagerLocal tokenMan;

    private boolean userExists(String email) throws CardioDataException {
        if (email == null || "".equals(email)) {
            throw new CardioDataException("email is not specified");
        }
        Query q = em.createQuery("select u from User u where u.email = :email").setParameter("email", email);
        List<User> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public User loginUser(AccountTypeEnum aType, String login, String password) throws CardioDataException {
        if (login == null) {
            throw new CardioDataException("login is not specified");
        }
        if (aType == null) {
            throw new CardioDataException("type is not specified");
        }
        if (AccountTypeEnum.EMAIL.equals(aType)) {
            if (!StringUtils.isValidEmail(login)) {
                throw new CardioDataException("email is not valid");
            }
            if (password == null || password.isEmpty()) {
                throw new CardioDataException("password is not specified");
            }
        }
        UserAccount acc = getAccount(aType, login, password);
        if (acc == null) {
            throw new CardioDataException("user account with specified information is not found", ResponseConstants.LOGIN_FAILED_CODE);
        }
        User u = getUserById(acc.getUserId());
        return u;
    }

    @Override
    public User loginUserByEmail(String email, String password) throws CardioDataException {
        return loginUser(AccountTypeEnum.EMAIL, email, password);
    }

    @Override
    public User getUserByToken(String token) throws CardioDataException {
        if (token == null) {
            throw new CardioDataException("token is not specified");
        }
        Query q = em.createQuery("select t from ApiToken t where t.token = :token").setParameter("token", token);
        List<ApiToken> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            throw new CardioDataException("user with specified token is not found");
        }
        ApiToken t = list.get(0);
        if (t.getExpirationDate() < new Date().getTime()) {
            throw new CardioDataException("token is expired");
        }
        return getUserById(t.getUserId());
    }

    @Override
    public User getUserById(Long userId) throws CardioDataException {
        if (userId == null) {
            throw new CardioDataException("user with id = " + userId + " does not exist in the system");
        }
        User u = em.find(User.class, userId);
        return u;
    }

    private UserAccount getAccount(AccountTypeEnum aType, String login, String password) throws CardioDataException {
        Query q = null;
        String jpql = "select ua from UserAccount ua where ua.accountType = :type and ua.login = :login";
        if (password != null) {
            jpql += " and ua.password = :password";
            q = em.createQuery(jpql).setParameter("type", aType).setParameter("login", login).setParameter("password", password);
        } else {
            q = em.createQuery(jpql).setParameter("type", aType).setParameter("login", login);
        }
        List<UserAccount> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    private UserAccount getAccount(AccountTypeEnum aType, String login) throws CardioDataException {
        Query q = null;
        String jpql = "select ua from UserAccount ua where ua.accountType = :type and ua.login = :login";
        q = em.createQuery(jpql).setParameter("type", aType).setParameter("login", login);
        List<UserAccount> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public User registerUser(AccountTypeEnum aType, String login, String password) throws CardioDataException {
        if (getAccount(aType, login) != null) {
            throw new CardioDataException("user with specified login has been alterady registered in the system", ResponseConstants.REGISTRATION_FAILED_CODE);
        }

        User u = new User();
        u.setRegistrationDate((new Date()).getTime());
        u.setLastLoginDate((new Date()).getTime());
        u.setAccountStatus(AccountStatusEnum.FREE);
        u.setUserRole(UserRoleEnum.USER);
        u.setUserStatus(UserStatusEnum.ACTIVE);
        u = em.merge(u);

        UserAccount acc = new UserAccount(login, password, aType, u.getId());
        em.merge(acc);

        ApiToken t = new ApiToken(u.getId(), StringUtils.generateRandomString(ApiToken.TOKEN_LENGTH), (new Date().getTime() + ApiToken.EXPIRATION_TIME));
        em.merge(t);
        return u;
    }

    @Override
    public User updateUserProfile(Long userId, String firstName, String lastName) throws CardioDataException {
        User u = getUserById(userId);
        if (u == null) {
            throw new CardioDataException("user with id = " + userId + " is not found");
        }
        u.setFirstName(firstName);
        u.setLastName(lastName);
        return em.merge(u);
    }
}