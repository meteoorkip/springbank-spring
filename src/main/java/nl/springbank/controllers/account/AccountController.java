package nl.springbank.controllers.account;

import com.googlecode.jsonrpc4j.JsonRpcError;
import com.googlecode.jsonrpc4j.JsonRpcErrors;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NotAuthorizedError;
import nl.springbank.objects.OpenedAccountObject;
import nl.springbank.objects.OpenedCardObject;

/**
 * @author Tristan de Boer
 * @author Sven Konings
 */
@JsonRpcService("/api/account")
public interface AccountController {
    /**
     * Open a bank account. This method also creates a CustomerAccount and PIN card for
     * this account.
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
     * @return A dictionary containing the following members:
     * <ul>
     * <li><b>iBAN</b> The number of the created bank account</li>
     * <li><b>pinCard</b> The number of the created pin card</li>
     * <li><b>pinCode</b> The pin code of the created pin card</li>
     * </ul>
     * @throws InvalidParamValueError One or more parameter has an invalid value. See message.
     */
    @JsonRpcErrors({@JsonRpcError(exception = InvalidParamValueError.class, code = 418)})
    OpenedAccountObject openAccount(
            @JsonRpcParam("name") String name,
            @JsonRpcParam("surname") String surname,
            @JsonRpcParam("initials") String initials,
            @JsonRpcParam("dob") String dob,
            @JsonRpcParam("ssn") String ssn,
            @JsonRpcParam("address") String address,
            @JsonRpcParam("telephoneNumber") String telephoneNumber,
            @JsonRpcParam("email") String email,
            @JsonRpcParam("username") String username,
            @JsonRpcParam("password") String password
    ) throws InvalidParamValueError;

    /**
     * Open an additional account for an existing customer. Also creates a PIN card for this
     * account.
     *
     * @param authToken The authentication token, obtained with getAuthToken
     * @return A dictionary containing the following members:
     * <ul>
     * <li><b>iBAN</b> The number of the created bank account</li>
     * <li><b>pinCard</b> The number of the created pin card</li>
     * <li><b>pinCode</b> The pin code of the created pin card</li>
     * </ul>
     * @throws NotAuthorizedError The authenticated user is not authorized to perform this action.
     */
    @JsonRpcErrors({@JsonRpcError(exception = NotAuthorizedError.class, code = 419)})
    OpenedAccountObject openAdditionalAccount(@JsonRpcParam("authToken") String authToken) throws NotAuthorizedError;

    /**
     * Close a bank account. Also invalidates the corresponding pin card. If this is the customers
     * last bank account, it also closes the customer account.
     *
     * @param authToken The authentication token, obtained with getAuthToken
     * @param iBAN      The number of the bank account
     * @throws InvalidParamValueError One or more parameter has an invalid value. See message.
     * @throws NotAuthorizedError     The authenticated user is not authorized to perform this action.
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueError.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedError.class, code = 419)
    })
    void closeAccount(
            @JsonRpcParam("authToken") String authToken,
            @JsonRpcParam("iBAN") String iBAN
    ) throws InvalidParamValueError, NotAuthorizedError;

    /**
     * Invalidate a PIN Card and obtain a new one.
     *
     * @param authToken The authentication token, obtained with getAuthToken
     * @param iBAN      The number of the bank account
     * @param pinCard   The number of the pinCard
     * @param newPin    Boolean to indicate if the user wants a new PIN code
     * @return A dictionary containing the following members:
     * <ul>
     * <li><b>pinCard</b> The number of the new pin card</li>
     * <li><b>pinCode</b> (optinal) The new pin code of the pin card</li>
     * </ul>
     * @throws InvalidParamValueError One or more parameter has an invalid value. See message.
     * @throws NotAuthorizedError     The authenticated user is not authorized to perform this action.
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueError.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedError.class, code = 419)
    })
    OpenedCardObject invalidateCard(
            @JsonRpcParam("authToken") String authToken,
            @JsonRpcParam("iBAN") String iBAN,
            @JsonRpcParam("pinCard") String pinCard,
            @JsonRpcParam("newPin") boolean newPin
    ) throws InvalidParamValueError, NotAuthorizedError;
}
