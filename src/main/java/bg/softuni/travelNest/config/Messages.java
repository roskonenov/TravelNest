package bg.softuni.travelNest.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Messages {

    private final I18NConfig i18NConfig;

    private MessageSourceAccessor accessor;

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(i18NConfig.messageSource());
    }

    public String get(String code) {
        return accessor.getMessage(code);
    }
}
