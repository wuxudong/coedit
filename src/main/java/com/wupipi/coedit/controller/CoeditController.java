package com.wupipi.coedit.controller;

import com.wupipi.coedit.service.CoeditService;
import com.wupipi.coedit.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

/**
 * User: xudong
 * Date: 9/5/14
 * Time: 10:15 AM
 */
@Controller
public class CoeditController {

    private static final Logger LOG = LoggerFactory
            .getLogger(CoeditController.class);

    @Autowired
    private CoeditService coeditService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "redirect:default";
    }


    @RequestMapping(value = "/{room}", method = RequestMethod.GET)
    public String room(@PathVariable String room) {
        return "room";
    }


    @RequestMapping(value = "/{room}/join", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public ChatUser join(@PathVariable(value = "room") String roomName, @RequestBody ChatUser chatUser) throws
            UnsupportedEncodingException {
        chatUser.setUid(UUID.randomUUID().toString());

        return chatUser;
    }

    @RequestMapping(value = "/{room}/op", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public OpResult op(@PathVariable String room, @RequestBody Change change) throws UnsupportedEncodingException {
        OpResult opResult = coeditService.applyPaperChange(room, change);

        coeditService.broadcastPaperOp(room, opResult);

        return opResult;
    }

    @RequestMapping(value = "/{room}/resync", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public ResyncResult resync(@PathVariable String room) throws UnsupportedEncodingException {
        ResyncResult resyncResult = coeditService.resync(room);
        return resyncResult;
    }


    @SubscribeMapping("/{room}/participants")
    public List<ChatUser> getParticipants(@PathVariable(value = "room") String roomName) {
        return coeditService.getParticipants(roomName);
    }

    @SubscribeMapping("/{room}/messagehistory")
    public List<ChatMessage> getMessageHistory(@PathVariable(value = "room") String roomName) {
        return coeditService.getMessageHistory(roomName);
    }

    @MessageMapping(value = "/{room}/newmessage")
    public void executeMessage(@PathVariable(value = "room") String roomName, String message, Principal principal) {
        coeditService.executeMessage(roomName, new ChatMessage(principal.getName(), message));
    }
}
