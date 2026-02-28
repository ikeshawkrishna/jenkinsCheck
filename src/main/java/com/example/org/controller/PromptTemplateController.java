package com.example.org.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.org.service.ChatService;

@RestController
@RequestMapping("/api")
public class PromptTemplateController {
	
	@Value("classpath:prompts/hrSystemPrompt.st")
	private Resource hrSystemPrompt;
	
	@Value("classpath:prompts/hrUserPrompt.st")
	private Resource hrUserPrompt;

	private final ChatClient chatClient;
	
	public PromptTemplateController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}
	
	@GetMapping("/hr")
	public String connectWithHR(@RequestParam String employeeName,@RequestParam String employeeId,@RequestParam String purpose) {
		return chatClient
				.prompt()
				.system(hrSystemPrompt) // PromptStuffing
//				.options(OpenAiChatOptions.builder().model(ChatModel.GPT_4_1_MINI).build())
				.user(userprompt -> 
						userprompt.text(hrUserPrompt)
						.param("employee_name", employeeName)
						.param("employee_id", employeeId)
						.param("purpose", purpose))
				.call()
				.content();
	}
}
