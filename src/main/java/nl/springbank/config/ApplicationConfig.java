package nl.springbank.config;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImplExporter;
import nl.springbank.helper.jsonrpc.JsonRpcInvocationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the application.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Configuration
public class ApplicationConfig {

    private static final JsonRpcInvocationListener jsonRpcInvocationListener = new JsonRpcInvocationListener();

    public static JsonRpcInvocationListener getJsonRpcInvocationListener() {
        return jsonRpcInvocationListener;
    }

    @Bean
    public static AutoJsonRpcServiceImplExporter autoJsonRpcServiceImplExporter() {
        AutoJsonRpcServiceImplExporter autoJsonRpcServiceImplExporter = new AutoJsonRpcServiceImplExporter();
        autoJsonRpcServiceImplExporter.setInvocationListener(jsonRpcInvocationListener);
        return autoJsonRpcServiceImplExporter;
    }
}
