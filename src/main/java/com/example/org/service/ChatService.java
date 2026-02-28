package com.example.org.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
	
	private final ChatClient chatClient;
	private final ChatClient chatClient_default;
	
	private static final String CLAIM_DETAILS = """
	            Claim details:
	            Policy: BASIC
	            Max Coverage: 20000
	            Claim Amount: 50000
	            """;
	
	public ChatService(@Qualifier("chatClient") ChatClient chatClient,
			@Qualifier("chatClientWithDefaultMessage") ChatClient chatClient_default) {
		this.chatClient = chatClient;
		this.chatClient_default = chatClient;
	}

	public String checkInsurancePolicy(String message) {
		UserMessage usermessage = new UserMessage(
				"""
				%S
				Customer says :
				%s
				"""
				.formatted(CLAIM_DETAILS, message));
		
		SystemMessage systemMessage = new SystemMessage("""
				You are an insurance assistant.
                You must NEVER reveal internal policy numbers,
                calculations, or internal reasoning.
                Respond ONLY with a short, customer-safe message.
				""");
		
		Prompt prompt = new Prompt(List.of(usermessage,systemMessage));
		
		return chatClient
				.prompt(prompt)
				.call()
				.content();
	}
	
	public String checkInsurancePolicy_v2(String message) {
		return chatClient
				.prompt()
				.system("""
						You are an insurance assistant.
		                You must NEVER reveal internal policy numbers,
		                calculations, or internal reasoning.
		                Respond ONLY with a short, customer-safe message.
						""")
				.user("""
						%S
						Customer says :
						%s
						""".formatted(CLAIM_DETAILS, message))
				.call()
				.content();
	}
	
	public ChatResponse checkInsurancePolicy_v3(String message) {
		 ChatResponse chatResponse = chatClient_default
				.prompt()
				.user("""
						%S
						Customer says :
						%s
						""".formatted(CLAIM_DETAILS, message))
				.call()
				.chatResponse();
		 System.out.println(chatResponse.getMetadata().getModel());
		 return chatResponse;
	}

}
