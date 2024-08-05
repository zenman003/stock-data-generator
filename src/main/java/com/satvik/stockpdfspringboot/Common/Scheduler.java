package com.satvik.stockpdfspringboot.Common;

import com.satvik.stockpdfspringboot.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final LinkedHashMap<Long, Date> unVerifiedUsers;

    private final UserRepository userRepository;

    @Scheduled(fixedDelay = 30 * 60 * 1000) // 30 minutes
    public void removeUnverifiedUsers() {
        Iterator<Map.Entry<Long, Date>> iterator = unVerifiedUsers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Date> entry = iterator.next();
            if (new Date().getTime() - entry.getValue().getTime() > 24 * 60 * 60 * 1000) { // 24 hours
                userRepository.deleteById(entry.getKey());
                iterator.remove();
            } else {
                break;
            }
        }
    }
}
