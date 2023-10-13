package dev.soon.interviewdefense.log.interceptor;

import dev.soon.interviewdefense.log.appender.MDCHandlerCommentAppender;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Slf4j
public class MDCRequestHandlerCommentInterceptor extends EmptyInterceptor {

    private final transient MDCHandlerCommentAppender mdcHandlerCommentAppender;

    public MDCRequestHandlerCommentInterceptor() {
        this(Set.of("handler"));
    }

    public MDCRequestHandlerCommentInterceptor(Set<String> handlerKeys) {
        if(CollectionUtils.isEmpty(handlerKeys)) {
            throw new IllegalArgumentException();
        }
        this.mdcHandlerCommentAppender = new MDCHandlerCommentAppender(handlerKeys);
    }

    @Override
    public String onPrepareStatement(String sql) {
        String logResult = mdcHandlerCommentAppender.appendHandlerName(sql);
        log.info("Query : {}", logResult);
        return logResult;
    }
}
