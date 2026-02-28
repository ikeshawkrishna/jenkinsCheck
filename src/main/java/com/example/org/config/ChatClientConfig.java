package com.example.org.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

	@Bean("chatClientWithDefaultMessage")
	ChatClient chatClientWithDefaultMessage(ChatClient.Builder builder) {
		return builder
				.defaultSystem("""
						You are an internal HR assistant. Your role is to help\s
						            employees with questions related to HR policies, such as\s
						            leave policies, working hours, benefits, and code of conduct.
						            If a user asks for help with anything outside of these topics,\s
						            kindly inform them that you can only assist with queries related to\s
						            HR policies.

						            HR Policy Summary:
						• 18 days of paid leave annually
						• Up to 8 unused leave days can be carried over to the next year
						• Standard working hours: 9 AM to 6 PM, Monday to Friday
						• Notice period - 30 days
						• Maternity leaves - 6 months
						• Paternity leaves - 2 weeks
						• National holidays are company-wide off days
						• Benefits include health insurance, provident fund, and annual health checkup
						• Employees must adhere to professional behavior, punctuality, and data confidentiality
								""")
				.defaultAdvisors(new SimpleLoggerAdvisor())
				.build();
	}

	@Bean("chatClient")
	ChatClient chatClient(ChatClient.Builder builder) {

		ChatOptions chatOptions = ChatOptions.builder()
				.model(ChatModel.GPT_4_O_MINI.value)
				.temperature(0.8)
				.maxTokens(200)
				.build();

		return builder
				.defaultOptions(chatOptions)
				.build();
	}

	/**
	 * JdbcChatMemoryRepository bean will be injected automatically
	 */
	@Bean
	ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {
		return MessageWindowChatMemory.builder()
				.chatMemoryRepository(chatMemoryRepository)
				.maxMessages(30) // Last 30 conversation including the user and assistant will only be stored
				.build();
	}

	/**
	 * Not needed as JdbcChatMemoryRepository will created when jdbc is used,
	 * not the InMemoryChatMemory Repository
	 */
	// @Bean
	// ChatMemoryRepository chatMemoryRepository(JdbcTemplate jdbcTemplate) {
	// return JdbcChatMemoryRepository.builder()
	// .jdbcTemplate(jdbcTemplate)
	// .build();
	// }
}
