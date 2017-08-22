package nl.springbank.services;

import nl.springbank.bean.BankAccountBean;
import nl.springbank.bean.CardBean;
import nl.springbank.bean.UserBean;
import nl.springbank.dao.UserDao;
import nl.springbank.exceptions.AuthenticationError;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NotAuthorizedError;
import nl.springbank.helper.AuthenticationHelper;
import nl.springbank.helper.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Service that does all operation regarding Users.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Get the user with the given user id.
     *
     * @param userId the given user id
     * @return the user
     * @throws InvalidParamValueError if an error occurred or the user doesn't exist
     */
    public UserBean getUser(long userId) throws InvalidParamValueError {
        UserBean user;
        try {
            user = userDao.findOne(userId);
            Assert.notNull(user, "User not found");
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return user;
    }

    /**
     * Get the user with the given username.
     *
     * @param username the given username
     * @return the user
     * @throws InvalidParamValueError if an error occurred or the user doesn't exist
     */
    public UserBean getUser(String username) throws InvalidParamValueError {
        UserBean user;
        try {
            user = userDao.findByUsername(username);
            Assert.notNull(user, "User not found");
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return user;
    }

    /**
     * Get all users.
     *
     * @return the list of users
     */
    public List<UserBean> getUsers() {
        return userDao.findAll();
    }

    /**
     * Get the user with the given username and password.
     *
     * @param username The given username
     * @param password The given password
     * @return the user
     * @throws AuthenticationError if an error occurred or the user doesn't exist
     */
    public UserBean authUser(String username, String password) throws AuthenticationError {
        UserBean user;
        try {
            user = userDao.findByUsernameAndPassword(username, password);
            Assert.notNull(user, "User not found");
        } catch (IllegalArgumentException e) {
            throw new AuthenticationError(e);
        }
        return user;
    }

    /**
     * Get the user that belongs to the given authentication token.
     *
     * @param authToken the given authentication token
     * @return the user
     * @throws NotAuthorizedError if an error occurred or the user doesn't exist
     */
    public UserBean getUserByAuth(String authToken) throws NotAuthorizedError {
        long userId = AuthenticationHelper.getUserId(authToken);
        UserBean userBean;
        try {
            userBean = userDao.findOne(userId);
            Assert.notNull(userBean, "User not found");
        } catch (IllegalArgumentException e) {
            throw new NotAuthorizedError(e);
        }
        return userBean;
    }

    /**
     * Check if the user that belongs to the given authentication token is the holder of the given bank account.
     *
     * @param bankAccount the given bank account
     * @param authToken   the given authentication token
     * @throws NotAuthorizedError if the user is not the holder of the given bank account
     */
    public void checkHolder(BankAccountBean bankAccount, String authToken) throws NotAuthorizedError {
        checkHolder(bankAccount, getUserByAuth(authToken));
    }

    /**
     * Check if the given user is the holder of the given bank account.
     *
     * @param bankAccount the given bank account
     * @param user        the given user
     * @throws NotAuthorizedError if the user is not the holder of the given bank account
     */
    public void checkHolder(BankAccountBean bankAccount, UserBean user) throws NotAuthorizedError {
        if (!bankAccount.getHolder().equals(user)) {
            throw new NotAuthorizedError("User is not eligible to get access");
        }
    }

    /**
     * Check if the user that belongs to the given authentication token is allowed to access the given bank account.
     *
     * @param bankAccount the given bank account
     * @param authToken   the given authentication token
     * @throws NotAuthorizedError if the user is not allowed to access the given bank account
     */
    public void checkAccess(BankAccountBean bankAccount, String authToken) throws NotAuthorizedError {
        checkAccess(bankAccount, getUserByAuth(authToken));
    }

    /**
     * Check if the given user is allowed to access the given bank account.
     *
     * @param bankAccount the given bank account
     * @param user        the given user
     * @throws NotAuthorizedError if the user is not allowed to access the given bank account
     */
    public void checkAccess(BankAccountBean bankAccount, UserBean user) throws NotAuthorizedError {
        if (!bankAccount.getAccessUsers().contains(user)) {
            throw new NotAuthorizedError("User is not eligible to get access");
        }
    }

    /**
     * Check if the user that belongs to the given authentication token is the owner of the given card.
     *
     * @param card      the given card
     * @param authToken the given authentication token
     * @throws NotAuthorizedError if the user is not the owner of the given card
     */
    public void checkCardOwner(CardBean card, String authToken) throws NotAuthorizedError {
        checkCardOwner(card, getUserByAuth(authToken));
    }

    /**
     * Check if the given user is the owner of the given card.
     *
     * @param card the given card
     * @param user the given user
     * @throws NotAuthorizedError if the user is not the owner of the given card
     */
    public void checkCardOwner(CardBean card, UserBean user) throws NotAuthorizedError {
        if (!card.getUser().equals(user)) {
            throw new NotAuthorizedError("User is not the card owner");
        }
    }

    /**
     * Creates a new user.
     *
     * @param name            Customer name
     * @param surname         Customer surname
     * @param initials        Customer initials
     * @param dob             Customer date of birth
     * @param ssn             Customer SSN
     * @param address         Customer address
     * @param telephoneNumber Customer telephoneNumber
     * @param email           Customer email
     * @param username        Customer username
     * @param password        Customer password
     * @return the created user
     * @throws InvalidParamValueError If the user can't be created
     */
    public UserBean newUser(String name, String surname, String initials, String dob, String ssn, String address, String telephoneNumber, String email, String username, String password) throws InvalidParamValueError {
        UserBean user = new UserBean();
        user.setName(name);
        user.setSurname(surname);
        user.setInitials(initials);
        user.setDateOfBirth(DateHelper.getDateFromString(dob));
        user.setBsn(ssn);
        user.setStreetAddress(address);
        user.setTelephoneNumber(telephoneNumber);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        return saveUser(user);
    }

    /**
     * Save the given user.
     *
     * @param user the given user
     * @return the saved user
     */
    public UserBean saveUser(UserBean user) {
        return userDao.save(user);
    }

    /**
     * Save the given users.
     *
     * @param users the given users
     * @return the list of saved users
     */
    public List<UserBean> saveUsers(Iterable<UserBean> users) {
        return userDao.save(users);
    }

    /**
     * Delete the user with the given user id.
     *
     * @param userId the given user id
     */
    public void deleteUser(long userId) {
        userDao.delete(userId);
    }

    /**
     * Delete the given user.
     *
     * @param user the given user
     */
    public void deleteUser(UserBean user) {
        userDao.delete(user);
    }

    /**
     * Delete the given users.
     *
     * @param users the given users
     */
    public void deleteUsers(Iterable<UserBean> users) {
        userDao.delete(users);
    }
}
