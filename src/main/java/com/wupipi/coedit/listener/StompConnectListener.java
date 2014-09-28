package com.wupipi.coedit.listener;

import com.wupipi.coedit.model.ChatUser;
import com.wupipi.coedit.service.CoeditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.List;
import java.util.Map;

@Component
public class StompConnectListener implements ApplicationListener<SessionConnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(StompConnectListener.class);

    @Autowired
    private CoeditService coeditService;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        log.debug("Connect event fired: session=" + event);
        String wsSessionId = event.getMessage().getHeaders().get(SimpMessageHeaderAccessor.SESSION_ID_HEADER, String.class);

        Map<String, List<String>> nativeHeaders= event.getMessage().getHeaders().get(SimpMessageHeaderAccessor
                        .NATIVE_HEADERS,
                Map.class);
        String login = nativeHeaders.get("login").iterator().next();
        String roomName = nativeHeaders.get("room").iterator().next();
        String userName = nativeHeaders.get("name").iterator().next();

        ChatUser user = new ChatUser(login, userName);
        coeditService.addParticipant(roomName, user, wsSessionId);
    }
}
