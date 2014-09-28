package com.wupipi.coedit.service;

import com.wupipi.coedit.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: xudong
 * Date: 9/6/14
 * Time: 7:58 AM
 */
@Service
public class CoeditService {
    private ConcurrentHashMap<String, Room> storage = new ConcurrentHashMap<String, Room>();

    private ConcurrentHashMap<String, String> wsUserMapping = new ConcurrentHashMap<String, String>();

    private ConcurrentHashMap<String, String> userRoomMapping = new ConcurrentHashMap<String, String>();


    private static final String PARTICIPANT_UPDATES_DEST = "/topic/%s/participant-updates";
    private static final String MESSAGE_UPDATE_DEST = "/topic/%s/message-updates";
    private static final String PAPER_UPDATE_DEST = "/topic/%s/paper-updates";


    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public void executeMessage(String roomName, ChatMessage message) {
        ChatMessage cm = new ChatMessage(message.getName(), message.getMessage());
        getRoom(roomName).getMessageHistory().add(cm);
        this.messagingTemplate.convertAndSend(MESSAGE_UPDATE_DEST, cm);
    }

    public void broadcastPaperOp(String roomName, OpResult opResult) {
        this.messagingTemplate.convertAndSend(String.format(PAPER_UPDATE_DEST, roomName), opResult);
    }

    public List<ChatUser> getParticipants(String roomName) {
        List<ChatUser> list = new ArrayList<ChatUser>(getRoom(roomName).getUsers());
        Collections.sort(list);
        return list;
    }

    public void addParticipant(String roomName, ChatUser user, String sessionId) {
        Room room = getRoom(roomName);
        if (room.addUser(user) == null) {
            this.messagingTemplate.convertAndSend(String.format(PARTICIPANT_UPDATES_DEST, roomName),
                    getParticipants(roomName));
        }
        wsUserMapping.put(sessionId, user.getUid());
        userRoomMapping.put(user.getUid(), roomName);
    }


    public void removeParticipant(String wsSessionId) {
        String uid = wsUserMapping.remove(wsSessionId);
        if (uid == null) {
            return;
        }

        String roomName = userRoomMapping.remove(uid);
        if (roomName == null) {
            return;
        }

        Room room = storage.get(roomName);

        if (room.removeUser(uid) != null) {
            this.messagingTemplate.convertAndSend(PARTICIPANT_UPDATES_DEST, getParticipants(roomName));
        }
    }

    public List<ChatMessage> getMessageHistory(String roomName) {
        return getRoom(roomName).getMessageHistory();
    }

    public Room getRoom(String roomName) {
        Room room = new Room();
        Room result = storage.putIfAbsent(roomName, room);

        return result == null ? room : result;
    }

    public OpResult applyPaperChange(String room, Change change) {
        return getRoom(room).getPaper().applyChange(change);
    }

    public ResyncResult resync(String roomName) {
        Room room = getRoom(roomName);
        ResyncResult resyncResult = new ResyncResult();
        resyncResult.setContent(room.getPaper().content());
        resyncResult.setMessageHistory(room.getMessageHistory());
        resyncResult.setNextRevision(room.getPaper().getRevision() + 1);
        return resyncResult;
    }
}
