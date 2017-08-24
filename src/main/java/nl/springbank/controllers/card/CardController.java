package nl.springbank.controllers.card;

import com.googlecode.jsonrpc4j.JsonRpcError;
import com.googlecode.jsonrpc4j.JsonRpcErrors;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NoEffectError;
import nl.springbank.exceptions.NotAuthorizedError;
import nl.springbank.objects.OpenedCardObject;

/**
 * @author Sven Konings
 */
@JsonRpcService("/api/card")
public interface CardController {
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

    /**
     * Unblock a blocked PIN card.
     *
     * @param authToken The authentication token, obtained with getAuthToken
     * @param iBAN      The number of the bank account
     * @param pinCard   The number of the pinCard
     * @throws InvalidParamValueError One or more parameter has an invalid value. See message.
     * @throws NotAuthorizedError     The authenticated user is not authorized to perform this action.
     * @throws NoEffectError          If the card is not blocked this method will have no effect.
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueError.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedError.class, code = 419),
            @JsonRpcError(exception = NoEffectError.class, code = 420)
    })
    void unblockCard(
            @JsonRpcParam("authToken") String authToken,
            @JsonRpcParam("iBAN") String iBAN,
            @JsonRpcParam("pinCard") String pinCard
    ) throws InvalidParamValueError, NotAuthorizedError, NoEffectError;
}
