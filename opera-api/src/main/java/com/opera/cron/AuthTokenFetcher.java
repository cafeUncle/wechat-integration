package com.opera.cron;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.opera.util.HttpUtil;

import java.io.IOException;

@Component
@EnableScheduling
public class AuthTokenFetcher implements InitializingBean {

    private static final String URL = "https://api.weixin.qq.com/cgi-bin/token?" +
            "grant_type=client_credential&appid=wx788be908e58775d6&secret=e5b014acb2da9d2f6bf3ee639f656abf";

    /**
     * authToken 默认 7200s = 2h 过期，可以 60 ~ 90min 获取一次，日上限 2000 次
     * 单机部署时，authToken 获取后直接放在了内存中。
     * 如果是集群部署，由于 authToken 每次获取都会更新，上次获取的值会在 5 分钟后失效，
     * 所以需要用分布式定时任务等方案处理，同时也可以做一个内存缓存。
     */
    private static String authToken = "empty";

    public String getAuthToken() {
        return authToken;
    }

//    @Scheduled(com.opera.cron = "${schedule.task.authtoken}")
    @Scheduled(fixedDelay = 3600000)
    public void fetch() {
        try {
            authToken = HttpUtil.get(URL);
            System.out.println(authToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void afterPropertiesSet() {
        System.out.println("AuthTokenFetcher Init");
    }
}
