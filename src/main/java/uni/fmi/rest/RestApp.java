package uni.fmi.rest;

import org.glassfish.jersey.server.ResourceConfig;
import uni.fmi.filter.AuthenticationFilter;
import uni.fmi.rest.binder.Binder;
import uni.fmi.util.ConfigUtil;

public class RestApp extends ResourceConfig {
    public RestApp() {
        register(AuthenticationFilter.class);
        addProperties(ConfigUtil.getLoadedProps());
        register(new Binder());
        packages(true, "uni.fmi.endpoint");
    }
}
