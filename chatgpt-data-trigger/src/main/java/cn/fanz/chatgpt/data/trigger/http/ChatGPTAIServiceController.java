package cn.fanz.chatgpt.data.trigger.http;

import cn.fanz.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import cn.fanz.chatgpt.data.domain.openai.model.entity.MessageEntity;
import cn.fanz.chatgpt.data.domain.openai.service.IChatService;
import cn.fanz.chatgpt.data.trigger.http.dto.ChatGPTRequestDTO;
import cn.fanz.chatgpt.data.types.exception.ChatGPTException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

/**
 * @author yifan
 * @description
 * @create 2024-10-12
 */
@Slf4j
@RestController()
@CrossOrigin("${app.config.cross-origin}")
//@RequestMapping("/api/${app.config.api-version}/")
@RequestMapping("/api/${app.config.api-version}/")
public class ChatGPTAIServiceController {

    @Resource
    private IChatService chatService;

    /**
     * 流式问题，ChatGPT 请求接口
     * <p>
     * curl -X POST \
     * http://localhost:8090/api/v1/chat/completions \
     * -H 'Content-Type: application/json;charset=utf-8' \
     * -H 'Authorization: b8b6' \
     * -d '{
     * "messages": [
     * {
     * "content": "写一个java冒泡排序",
     * "role": "user"
     * }
     * ],
     * "model": "gpt-3.5-turbo"
     * }'
     */
    @RequestMapping(value = "chat/completions", method = RequestMethod.POST)
    public ResponseBodyEmitter completionsStream(@RequestBody ChatGPTRequestDTO request, @RequestHeader("Authorization") String token, HttpServletResponse response) {
        log.info("流式问答请求开始，使用模型：{} 请求信息：{}", request.getModel(), JSON.toJSONString(request.getMessages()));
        try {
            // 1. 基础配置；流式输出、编码、禁用缓存
            response.setContentType("text/event-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache");

            // 2. 构建参数
            ChatProcessAggregate chatProcessAggregate = ChatProcessAggregate.builder()
                    .token(token)
                    .model(request.getModel())
                    .messages(request.getMessages().stream()
                            .map(entity -> MessageEntity.builder()
                                    .role(entity.getRole())
                                    .content(entity.getContent())
                                    .name(entity.getName())
                                    .build())
                            .collect(Collectors.toList()))      //防御性拷贝，虽然request中的Message已经是MessageEntity对象，这样做可以防止修改ChatProcessAggregate中的MessageEntity
                    .build();

            // 3. 请求结果&返回
            return chatService.completions(chatProcessAggregate);
        } catch (Exception e) {
            log.error("流式应答，请求模型：{} 发生异常", request.getModel(), e);
            throw new ChatGPTException(e.getMessage());
        }
    }

}
