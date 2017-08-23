package nl.springbank.controllers.authentication;

import com.googlecode.jsonrpc4j.JsonRpcError;
import com.googlecode.jsonrpc4j.JsonRpcErrors;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import nl.springbank.exceptions.AuthenticationError;
import nl.springbank.objects.AuthenticationObject;

/**
 * @author Tristan de Boer
 * @author Sven Konings
 */
@JsonRpcService("/api/authentication")
public interface AuthenticationController {
    /**
     * Attempts to log in and returns an authentication token.
     *
     * @param username The username of the customer
     * @param password The password of the customer
     * @return A dictionary containing the following members:
     * <ul>
     * <li><b>authToken</b> The authToken string</li>
     * </ul>
     * @throws AuthenticationError The user could not be authenticated. Invalid username, password
     *                             or combination.
     */
    @JsonRpcErrors(@JsonRpcError(exception = AuthenticationError.class, code = 422))
    AuthenticationObject getAuthToken(
            @JsonRpcParam(value = "username") String username,
            @JsonRpcParam(value = "password") String password
    ) throws AuthenticationError;
}
