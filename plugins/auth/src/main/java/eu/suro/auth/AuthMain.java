package eu.suro.auth;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import eu.suro.api.path.Path;
import eu.suro.api.plugin.Plugin;
import eu.suro.api.plugin.PluginDescriptor;
import eu.suro.auth.commands.RegisterCommand;

import eu.suro.auth.events.JoinListener;
import eu.suro.auth.user.User;
import net.md_5.bungee.api.connection.PendingConnection;
import org.jetbrains.annotations.Nullable;
import org.pf4j.Extension;
import server.AuthGrpc;
import server.AuthOuterClass;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static eu.suro.bungee.ProxyMain.getChannel;

@Extension
@PluginDescriptor(
        name = "Auth",
        description = "Authentication plugin for BungeeCord"
)
public class AuthMain extends Plugin {

    @Inject
    private Path path;
    static AuthMain instance;
//    private Auth auth;
//    public LoadingCache<PendingConnection, User> users = CacheBuilder
//            .newBuilder()
//            .maximumSize(500)
//            .expireAfterWrite(10, TimeUnit.MINUTES)
//            .build(
//                    new CacheLoader<PendingConnection, User>() {
//                        @Override
//                        public User load(PendingConnection connection) throws Exception {
//                            return loadUser(connection);
//                        }
//                    }
//            );

//    @Nullable
//    public User loadUser(PendingConnection connection){
//        long timings = System.currentTimeMillis();
//        AuthOuterClass.User user = auth.getUser(connection.getName().toLowerCase());
//        if(user == null){
//            return null;
//        }
//        User user1 = new User(connection,
//                user.getId(),
//                user.getIp(),
//                user.getLastServer(),
//                user.getRegisteredIp(),
//                user.getAuth());
//        long finalTimings = System.currentTimeMillis() - timings;
////        log.info(connection.getName() + "[" + user.getId() + "] " + finalTimings + "ms");
//        return user1;
//    }

    @Override
    protected void init() throws Exception {
        if(path.isProxy()){
            getConfig().set("auths", Arrays.asList("AUTH-1", "AUTH-2"));
            getConfig().set("lobby", Arrays.asList("LOBBY-1", "LOBBY-2"));
            getConfig().save();

            path.RegisterCommand(RegisterCommand.class);
            path.RegisterListener(new JoinListener(this));
        }
    }


    @Override
    public void stop() {

    }

    public static AuthMain getInstance() {
        return instance;
    }

//    public Auth getAuth() {
//        return auth;
//    }
//
//    public LoadingCache<PendingConnection, User> getUsers() {
//        return users;
//    }
//    @Extension
//    public static class AuthModule extends Module {
//        //cache users
//
//        @Override
//        public void initBukkit() {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public String getName() {
//            return "Auth";
//        }
//
//        @Override
//        public boolean configExists() {
//            return true;
//        }
//        @Override
//        public void initProxy() {
//            getConfig().set("auths", Arrays.asList("AUTH-1", "AUTH-2"));
//            getConfig().set("lobby", Arrays.asList("LOBBY-1", "LOBBY-2"));
//            getConfig().save();
//
//            RegisterCommand(RegisterCommand.class);
//            RegisterListener(new JoinListener(this));
//        }
//    }
}
