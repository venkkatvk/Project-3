@Bean
public ChatClient enterpriseChatClient(ChatClient.Builder builder) {
    return builder
        .defaultAdvisors(new SimpleLoggerAdvisor(), new SecurityPassAdvisor())
        .build();
}