package nullguo.init;

import nullguo.p2pclient.P2PClient;
import nullguo.p2pserver.P2PServer;
import nullguo.p2pserver.P2PServerHandler;

public class Init {
public static void main(String [] args) throws Exception {

	P2PServer.initP2PServer(8033,14568,"localhost:14568");
}
}
