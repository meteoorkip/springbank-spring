package nl.springbank.controllers.overdraft;

import com.googlecode.jsonrpc4j.JsonRpcError;
import com.googlecode.jsonrpc4j.JsonRpcErrors;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NotAuthorizedError;
import nl.springbank.objects.OverdraftLimitObject;

/**
 * @author Sven Konings
 */
@JsonRpcService("/api/overdraft")
public interface OverdraftController {
    /**
     * Method that sets the overdraft limit for bank account.
     *
     * @param authToken      The authentication token, obtained with getAuthToken
     * @param iBAN           The number of the bank account
     * @param overdraftLimit The new overdraft limit for this bank account
     * @throws InvalidParamValueError One or more parameter has an invalid value. See message.
     * @throws NotAuthorizedError     The authenticated user is not authorized to perform this action.
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueError.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedError.class, code = 419)
    })
    void setOverdraftLimit(
            @JsonRpcParam("authToken") String authToken,
            @JsonRpcParam("iBAN") String iBAN,
            @JsonRpcParam("overdraftLimit") int overdraftLimit
    ) throws InvalidParamValueError, NotAuthorizedError;

    /**
     * Method that asks the server for the overdaft limit of a bank account.
     *
     * @param authToken The authentication token, obtained with getAuthToken
     * @param iBAN      The number of the bank account
     * @return A dictionary containing the following members:
     * <ul>
     * <li><b>overdraftLimit</b> The overdaft limit of this bank account</li>
     * </ul>
     * @throws InvalidParamValueError One or more parameter has an invalid value. See message.
     * @throws NotAuthorizedError     The authenticated user is not authorized to perform this action.
     */
    OverdraftLimitObject getOverdraftLimit(
            @JsonRpcParam("authToken") String authToken,
            @JsonRpcParam("iBAN") String iBAN
    ) throws InvalidParamValueError, NotAuthorizedError;
}
