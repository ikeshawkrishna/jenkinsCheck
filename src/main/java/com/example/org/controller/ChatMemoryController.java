package com.example.org.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatMemoryController {

	private final ChatClient chatClient;
	
	public ChatMemoryController(ChatClient.Builder builder, ChatMemory chatMemory) {
		SimpleLoggerAdvisor loggerAdvisor = new SimpleLoggerAdvisor();
		Advisor memoryAdvisor = MessageChatMemoryAdvisor
										.builder(chatMemory)
										.build();
		this.chatClient = builder
				.defaultAdvisors(memoryAdvisor,loggerAdvisor)
				.build();
	}
	
	@GetMapping("/CallAI/{message}")
	public String connectWithAI(@RequestHeader("username") String username, @PathVariable String message) {
		return chatClient
				.prompt()
				.advisors(spec-> spec.param(ChatMemory.CONVERSATION_ID, username))
				.user(message)
				.call()
				.content();
	}
}
