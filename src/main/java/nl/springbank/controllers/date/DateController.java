package nl.springbank.controllers.date;

import com.googlecode.jsonrpc4j.JsonRpcError;
import com.googlecode.jsonrpc4j.JsonRpcErrors;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.objects.DateObject;

/**
 * @author Sven Konings
 */
@JsonRpcService("/api/date")
public interface DateController {
    /**
     * Method that asks the server to process the passing of a specified number of days.
     *
     * @param nrOfDays The number of days that should be simulated.
     * @throws InvalidParamValueError One or more parameter has an invalid value. See message.
     */
    @JsonRpcErrors(@JsonRpcError(exception = InvalidParamValueError.class, code = 418))
    void simulateTime(@JsonRpcParam("nrOfDays") int nrOfDays) throws InvalidParamValueError;

    /**
     * Method that asks the server to restore the state to it’s initial value.
     */
    void reset();

    /**
     * Method that asks the server to get it’s simulated date.
     *
     * @return An array of dictionaries containing the following members:
     * <ul>
     * <li><b>date</b> The date of the system</li>
     * </ul>
     */
    DateObject getDate();
}
