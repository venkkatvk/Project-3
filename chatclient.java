@Bean
public ChatClient enterpriseChatClient(ChatClient.Builder builder, VectorStore redisVectorStore) {
    return builder
        .defaultAdvisors(new SimpleLoggerAdvisor(), new SecurityPassAdvisor(redisVectorStore))
        .build();
}