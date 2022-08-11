package eu.suro.auth;

import com.google.common.cache.CacheBuilder;
import eu.suro.api.module.Module;
import eu.suro.auth.commands.RegisterCommand;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import server.AuthGrpc;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

        //cache users
        public LoadingCache<String, > users = CacheBuilder
                .newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(
                        new CacheLoader<String, UserOuterClass.UserM>() {
                            @Override
                            public UserOuterClass.UserM load(String key) throws Exception {
                                return user.getUser(key.toLowerCase(Locale.ROOT));
                            }
                        }
                );

        @Override
        public void initBukkit() {
            throw new UnsupportedOperationException("Not supported yet.");
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
            AuthGrpc.AuthStub stub = AuthGrpc.newStub(getChannel());
            AuthGrpc.AuthBlockingStub blockingStub = AuthGrpc.newBlockingStub(getChannel());
            RegisterCommand(RegisterCommand.class);
        }
    }
}
