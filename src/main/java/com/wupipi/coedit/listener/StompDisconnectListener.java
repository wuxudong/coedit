package com.wupipi.coedit.listener;

import com.wupipi.coedit.service.CoeditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class StompDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

	private static final Logger log = LoggerFactory.getLogger(StompDisconnectListener.class);
	
	@Autowired
	private CoeditService chatService;
	
	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		log.debug("Disconnect event fired: session="+event);
		String wsSessionId = event.getSessionId();
		chatService.removeParticipant(wsSessionId);
	}

}
