package dev.soon.interviewdefense.log.appender;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.function.Predicate.not;

@Slf4j
public class MDCHandlerCommentAppender {

    private final Set<String> handlerKeys;

    public MDCHandlerCommentAppender(Set<String> handlerKeys) {
        if(CollectionUtils.isEmpty(handlerKeys)) {
            throw new IllegalArgumentException();
        }
        this.handlerKeys = handlerKeys;
    }

    public String appendHandlerName(String sql) {
        final Optional<String> handlerMdcValue = readHandlerMdcValue();
        if(handlerMdcValue.isEmpty()) {
            return sql;
        }

        if(handlerMdcValue.get().isBlank()) {
            return sql;
        }

        return buildSqlWithHandlerInfo(sql, handlerMdcValue.get());
    }

    private Optional<String> readHandlerMdcValue() {
        return handlerKeys.stream()
                .map(MDC::get)
                .filter(Objects::nonNull)
                .filter(not(String::isBlank))
                .findFirst();
    }

    private String buildSqlWithHandlerInfo(String sql, String refinedHandlerMdcValue) {
        return "/* handler: " + refinedHandlerMdcValue + " */" + sql;
    }
}
