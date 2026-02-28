package com.example.org.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class StreamController {
	@Value("classpath:prompts/hrSystemPrompt.st")
	private Resource hrSystemPrompt;

	private final ChatClient chatClient;
	
	public StreamController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}
	
	@GetMapping(value = "/hrStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> connectWithHR(@RequestParam String message) {
		return chatClient
				.prompt()
				.system(hrSystemPrompt) // PromptStuffing
				.user(message)
				.stream()
				.content();
	}
}
