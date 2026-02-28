package com.example.org.config;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenAdvisor implements CallAdvisor{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.getClass().getSimpleName();
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
		// TODO Auto-generated method stub
		ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
		ChatResponse chatResponse = chatClientResponse.chatResponse();
		if(chatResponse != null) {
			Usage usage = chatResponse.getMetadata().getUsage();
			log.info("Request Token >> " + usage.getPromptTokens() + " Response Token >> " + usage.getCompletionTokens() + " Total Token >> " + usage.getTotalTokens());
		}
		return chatClientResponse;
	}

}
