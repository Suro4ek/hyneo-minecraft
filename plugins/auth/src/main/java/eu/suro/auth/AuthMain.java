package eu.suro.auth;


import eu.suro.api.module.Module;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public class AuthMain extends Plugin {
    /**
     * Constructor to be used by plugin manager for plugin instantiation.
     * Your plugins have to provide constructor with this exact signature to
     * be successfully loaded by manager.
     *
     * @param wrapper
     */
    public AuthMain(PluginWrapper wrapper)
    {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("START");
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }
    @Extension
    public static class AuthModule extends Module {

        @Override
        public void initBukkit() {
            System.out.println("START1");
        }

        @Override
        public String getName() {
            return "Auth";
        }

        @Override
        public boolean configExists() {
            return true;
        }

        @Override
        public void initProxy() {
            System.out.println("START2");
            new AuthProxy();
        }
    }
}
