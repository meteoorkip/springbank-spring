package nl.springbank.controllers.info;

import com.googlecode.jsonrpc4j.JsonRpcError;
import com.googlecode.jsonrpc4j.JsonRpcErrors;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NotAuthorizedError;
import nl.springbank.objects.*;

import java.util.List;

/**
 * @author Sven Konings
 */
@JsonRpcService("/api/info")
public interface InfoController {
    /**
     * Retrieves the balance of a bank account.
     *
     * @param authToken The authentication token, obtained with getAuthToken
     * @param iBAN      The number of the bank account
     * @return A dictionary containing the following members:
     * <ul>
     * <li><b>balance</b> The balance of the account</li>
     * </ul>
     * @throws InvalidParamValueError One or more parameter has an invalid value. See message.
     * @throws NotAuthorizedError     The authenticated user is not authorized to perform this action.
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueError.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedError.class, code = 419)
    })
    BalanceObject getBalance(
            @JsonRpcParam("authToken") String authToken,
            @JsonRpcParam("iBAN") String iBAN
    ) throws InvalidParamValueError, NotAuthorizedError;

    /**
     * Retrieves overview of recent transactions of a bank account.
     *
     * @param authToken        The authentication token, obtained with getAuthToken
     * @param iBAN             The number of the bank account
     * @param nrOfTransactions The number of transactions requested
     * @return An array of dictionaries containing the following members:
     * <ul>
     * <li><b>sourceIBAN</b> The number of the source bank account</li>
     * <li><b>targetIBAN</b> The number of the target bank account</li>
     * <li><b>targetName</b> The name of the target bank account holder</li>
     * <li><b>date</b> The date of the transaction</li>
     * <li><b>amount</b> The amount transferred</li>
     * <li><b>description</b> Description of the transaction</li>
     * </ul>
     * @throws InvalidParamValueError One or more parameter has an invalid value. See message.
     * @throws NotAuthorizedError     The authenticated user is not authorized to perform this action.
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueError.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedError.class, code = 419)
    })
    List<TransactionObject> getTransactionsOverview(
            @JsonRpcParam("authToken") String authToken,
            @JsonRpcParam("iBAN") String iBAN,
            @JsonRpcParam("nrOfTransactions") int nrOfTransactions
    ) throws InvalidParamValueError, NotAuthorizedError;

    /**
     * Retrieves an overview of all bank accounts a user has access to. User can only request
     * this for himself.
     *
     * @param authToken The authentication token, obtained with getAuthToken
     * @return An array of dictionaries containing the following members:
     * <ul>
     * <li><b>iBAN</b> The IBAN of the bank account</li>
     * <li><b>owner</b> The username of the accounts owner</li>
     * </ul>
     * @throws InvalidParamValueError One or more parameter has an invalid value. See message.
     * @throws NotAuthorizedError     The authenticated user is not authorized to perform this action.
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueError.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedError.class, code = 419)
    })
    List<UserAccessObject> getUserAccess(
            @JsonRpcParam("authToken") String authToken
    ) throws InvalidParamValueError, NotAuthorizedError;

    /**
     * Retrieves an overview of all users who have access to a bank account. Only the owner
     * of an account can perform this request.
     *
     * @param authToken The authentication token, obtained with getAuthToken
     * @param iBAN      The IBAN of the bank account
     * @return An array of dictionaries containing the following members:
     * <ul>
     * <li><b>username</b> The username of a UserAccount that has access to this bank account</li>
     * </ul>
     * @throws InvalidParamValueError One or more parameter has an invalid value. See message.
     * @throws NotAuthorizedError     The authenticated user is not authorized to perform this action.
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueError.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedError.class, code = 419)
    })
    List<BankAccountAccessObject> getBankAccountAccess(
            @JsonRpcParam("authToken") String authToken,
            @JsonRpcParam("iBAN") String iBAN
    ) throws InvalidParamValueError, NotAuthorizedError;

    /**
     * Method that requests all events logged between two dates.
     *
     * @param beginDate Begin date of period for which log are requested
     * @param endDate   End date of period for which log are requested
     * @return An array of dictionaries containing the following members:
     * <ul>
     * <li><b>timeStamp</b> TimeStamp of the event</li>
     * <li><b>eventLog</b> Info about the event</li>
     * </ul>
     * @throws InvalidParamValueError One or more parameter has an invalid value. See message.
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueError.class, code = 418)
    })
    List<EventLogObject> getEventLogs(
            @JsonRpcParam("beginDate") String beginDate,
            @JsonRpcParam("endDate") String endDate
    ) throws InvalidParamValueError;
}
