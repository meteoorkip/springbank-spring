package nl.springbank.dao;

import nl.springbank.bean.UserBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserDao. Communicates with the database and returns objects of type {@link UserBean}.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Transactional
public interface UserDao extends JpaRepository<UserBean, Long> {
    /**
     * Get the user with the given username.
     *
     * @param username the given username
     * @return the user, or {@code null} if it doesn't exist
     */
    UserBean findByUsername(String username);

    /**
     * Get the user with the given username and password.
     *
     * @param username the given username
     * @param password the given password
     * @return the user, or {@code null} if it doesn't exist
     */
    UserBean findByUsernameAndPassword(String username, String password);
}
