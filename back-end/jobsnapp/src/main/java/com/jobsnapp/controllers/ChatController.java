package com.jobsnapp.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jobsnapp.exceptions.UserNotFoundException;
import com.jobsnapp.model.*;
import com.jobsnapp.repositories.ChatRepository;
import com.jobsnapp.repositories.MessageRepository;
import com.jobsnapp.repositories.RoleRepository;
import com.jobsnapp.repositories.UserRepository;
import com.jobsnapp.services.UserService;

import static com.jobsnapp.utils.PictureSave.decompressBytes;

import java.util.HashSet;
import java.util.Set;

@RestController
@AllArgsConstructor
public class ChatController {

    @Autowired
    UserService userService;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @CrossOrigin(origins = "*")
    @GetMapping("/in/{id}/chats")
    public Set<Chat> getAllChats(@PathVariable Long id) {
        System.out.println("\n\n\n");
        User currentUser = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User with "+id+" not found"));
        for(Chat c: currentUser.getChats())
            System.out.println(c);

        for(Chat c: currentUser.getChats()) {
            System.out.println(c);
            for(User u: c.getUsers()){
                Picture pic = u.getProfilePicture();
                if(pic != null){
                    if(pic.isCompressed()){
                        Picture tempPicture = new Picture(pic.getId(),pic.getName(),pic.getType(),decompressBytes(pic.getBytes()));
                        pic.setCompressed(false);
                        u.setProfilePicture(tempPicture);
                    }
                }

            }

            for(Message m: c.getMessages()){
                User u = m.getUserMadeBy();
                Picture pic = u.getProfilePicture();
                if(pic != null){
                    if(pic.isCompressed()){
                        Picture tempPicture = new Picture(pic.getId(),pic.getName(),pic.getType(),decompressBytes(pic.getBytes()));
                        pic.setCompressed(false);
                        u.setProfilePicture(tempPicture);
                    }
                }
            }
        }
        return currentUser.getChats();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/in/{id}/chat/{chatId}")
    public Chat getChat(@PathVariable Long id, @PathVariable Long chatId) {
        User currentUser = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User with "+id+" not found"));
        Chat chat = chatRepository.findById(chatId).orElseThrow(()->new UserNotFoundException("Chat with "+id+" not found"));
        return chat;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/in/{id}/chat/{chatId}/new-message")
    public ResponseEntity<?> newMessage(@PathVariable Long id, @PathVariable Long chatId, @RequestBody Message message) {
        User currentUser = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User with "+id+" not found"));
        Chat chat = chatRepository.findById(chatId).orElseThrow(()->new UserNotFoundException("Chat with "+id+" not found"));
        message.setUserMadeBy(currentUser);
        message.setChat(chat);

        messageRepository.save(message);

        return ResponseEntity.ok(message);
    }

}


