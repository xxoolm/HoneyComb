package github.tornaco.practice.honeycomb.locker.server.verify;

public interface VerifyCallback {
    void onVerifyResult(int verifyResult, int reason);
}
