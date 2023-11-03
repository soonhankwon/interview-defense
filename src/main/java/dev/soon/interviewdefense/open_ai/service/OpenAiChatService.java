package dev.soon.interviewdefense.open_ai.service;

import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import dev.soon.interviewdefense.chat.domain.Chat;
import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.soon.interviewdefense.chat.util.PromptGenerator.*;
import static dev.soon.interviewdefense.chat.util.PromptGenerator.MENTOR_OPTION_KOREAN;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenAiChatService {

    private final OpenAiService openAiService;

    public Flowable<ChatCompletionChunk> generateStreamResponse(Chat chat, String question) {
        String topic = chat.getTopic().getValue();
        String rolePrompt = MENTOR_ROLE_CHARACTER +
                topic +
                MENTOR_ROLE +
                MENTOR_OPTION_TEACHING_STYLE +
                MENTOR_OPTION_KOREAN;

        ChatCompletionRequest request = requestOpenAI(rolePrompt, question);
        return openAiService.streamChatCompletion(request);
    }

    private ChatCompletionRequest requestOpenAI(String rolePrompt, String userPrompt) {
        return ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(new com.theokanning.openai.completion.chat.ChatMessage("system", rolePrompt),
                        new com.theokanning.openai.completion.chat.ChatMessage("user", userPrompt)))
                .maxTokens(1000)
                .stream(true)
                .build();
    }
}
