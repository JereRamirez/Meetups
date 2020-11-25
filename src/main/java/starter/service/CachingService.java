package starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CachingService {
    @Autowired
    CacheManager cacheManager;

    public void evictCache() {
        cacheManager.getCache("temperatures").clear();
    }
}
