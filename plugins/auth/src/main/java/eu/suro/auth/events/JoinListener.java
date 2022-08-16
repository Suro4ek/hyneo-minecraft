//package eu.suro.auth.events;
//
//import eu.suro.auth.MainAuth;
//import eu.suro.auth.user.User;
//import eu.suro.bungee.ProxyMain;
//import net.md_5.bungee.api.config.ServerInfo;
//import net.md_5.bungee.api.event.PostLoginEvent;
//import net.md_5.bungee.api.event.PreLoginEvent;
//import net.md_5.bungee.api.event.ServerConnectEvent;
//import net.md_5.bungee.api.plugin.Listener;
//import net.md_5.bungee.event.EventHandler;
//
//import java.util.List;
//import java.util.Random;
//
//public class JoinListener implements Listener {
//
//    public MainAuth.AuthModule authModule;
//
//    public JoinListener(MainAuth.AuthModule auth){
//        authModule = auth;
//    }
//
//    @EventHandler
//    public void PreLogin(PreLoginEvent e){
//        MainAuth.getInstance().getUsers().refresh(e.getConnection());
//    }
//
//    @EventHandler
//    public void PostLogin(PostLoginEvent e){
//
//    }
//    @EventHandler
//    public void ConnectServer(ServerConnectEvent e){
//        User user = MainAuth.getInstance().getUsers().getIfPresent(e.getPlayer().getPendingConnection());
//        if(user == null || !user.isAuth()) {
//            List<String> auth = authModule.getConfig().get("auths");
//            //random server to connect to
//            ServerInfo server = ProxyMain.getInstance().getProxy().getServers().get(auth.get(new Random().nextInt(auth.size())));
//        }
//    }
//}
