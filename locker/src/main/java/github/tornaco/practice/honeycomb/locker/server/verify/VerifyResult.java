package github.tornaco.practice.honeycomb.locker.server.verify;

public interface VerifyResult {
    int PASS = 1;
    int FAIL = -1;

    int REASON_USER_INPUT_CORRECT = 0;
}
