package cn.fanz.chatgpt.data.config;

import com.zhaoyif.chatgpt.session.OpenAiSession;
import com.zhaoyif.chatgpt.session.OpenAiSessionFactory;
import com.zhaoyif.chatgpt.session.defaults.DefaultOpenAiSessionFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yifan
 * @description OpenAiSession 工厂配置开启
 * @create 2025
 */
@Configuration
@EnableConfigurationProperties(ChatGPTSDKConfigProperties.class)
public class ChatGPTSDKConfig {

    @Bean
    public OpenAiSession openAiSession(ChatGPTSDKConfigProperties properties) {
        // 1. 配置文件
        com.zhaoyif.chatgpt.session.Configuration configuration = new com.zhaoyif.chatgpt.session.Configuration();
        configuration.setApiHost(properties.getApiHost());
        configuration.setAuthToken(properties.getAuthToken());
        configuration.setApiKey(properties.getApiKey());

        // 2. 会话工厂
        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);

        // 3. 开启会话
        return factory.openSession();
    }

}
