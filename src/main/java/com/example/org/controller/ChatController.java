package com.example.org.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.org.config.TokenAdvisor;
import com.example.org.service.ChatService;

@RestController
@RequestMapping("/api")
public class ChatController {
	
	private final ChatService chatService;
	private final ChatClient chatClientWithDefaultMessage;
	
	public ChatController(ChatService chatService, ChatClient chatClientWithDefaultMessage) {
		this.chatService = chatService;
		this.chatClientWithDefaultMessage = chatClientWithDefaultMessage;
	}

	@GetMapping("/insurance/{message}")
	public ChatResponse checkInsurancePolicy(@PathVariable String message) {
		return chatService.checkInsurancePolicy_v3(message);
	}
	
	@GetMapping("/hr/{message}")
	public String connectWithHRAssistant(String message) {
		return chatClientWithDefaultMessage
				.prompt()
				.advisors(new TokenAdvisor())
				.user(message)
				.call()
				.content();
	}
	
}
