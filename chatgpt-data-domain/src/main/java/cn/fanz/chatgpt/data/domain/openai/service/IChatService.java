package cn.fanz.chatgpt.data.domain.openai.service;

import cn.fanz.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description
 * @create 2023-07-22 20:53
 */
public interface IChatService {

    ResponseBodyEmitter completions(ChatProcessAggregate chatProcess);

}
