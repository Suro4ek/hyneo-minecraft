package eu.suro.auth.grpc;

import server.AuthGrpc;
import server.AuthOuterClass;

public class Auth {

    private AuthGrpc.AuthStub stub;
    private AuthGrpc.AuthBlockingStub blockingStub;

    public Auth(AuthGrpc.AuthStub stub, AuthGrpc.AuthBlockingStub blockingStub) {
        this.stub = stub;
        this.blockingStub = blockingStub;
    }

    public AuthOuterClass.User getUser(String username) {
        AuthOuterClass.GetUserResponse user = blockingStub.getUser(
                AuthOuterClass
                        .GetUserRequest
                        .newBuilder()
                        .setUsername(username)
                        .buildPartial());
        return user.getUser();
    }
}
