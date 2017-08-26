package nl.springbank.helper.jsonrpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.jsonrpc4j.InvocationListener;
import nl.springbank.services.LogService;

import java.lang.reflect.Method;
import java.util.List;

public class JsonRpcInvocationListener implements InvocationListener {

    private LogService logService;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    @Override
    public void willInvoke(Method method, List<JsonNode> arguments) {
        // Do nothing.
    }

    @Override
    public void didInvoke(Method method, List<JsonNode> arguments, Object result, Throwable t, long duration) {
        if (logService == null) {
            System.out.println("Logging unavailable");
            return;
        }
        if (result != null) {
            logService.newLog("method: %s, arguments: %s, result: %s", method.getName(), arguments, result);
        } else {
            logService.newLog("method: %s, arguments: %s", method.getName(), arguments);
        }
        if (t != null) {
            logService.newErrorLog(t.toString());
        }
    }
}
