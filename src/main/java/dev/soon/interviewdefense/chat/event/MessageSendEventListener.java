package dev.soon.interviewdefense.chat.event;

import dev.soon.interviewdefense.chat.respository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSendEventListener {

    private final ChatMessageRepository chatMessageRepository;

    @Async
    @Transactional
    @EventListener
    public void handleMessageSendEvent(MessageSendEvent event) {
        chatMessageRepository.save(event.chatMessage());
    }
}