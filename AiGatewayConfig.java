@Configuration
public class AiGatewayConfig {

    @Bean
    public ChatClient enterpriseChatClient(ChatClient.Builder builder) {
        return builder
            .defaultSystem("You are the secure gatekeeper of the Enterprise Castle.")
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();
    }
}
