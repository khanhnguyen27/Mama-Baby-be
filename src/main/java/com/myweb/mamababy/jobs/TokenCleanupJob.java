package com.myweb.mamababy.jobs;
import com.myweb.mamababy.services.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenCleanupJob {
    private final UserService userService;

    @Scheduled(cron = "0 0 0 * * ?")  // Chạy mỗi ngày vào nửa đêm
    public void cleanUpExpiredTokens() {
        userService.cleanupExpiredTokens();
    }
}
