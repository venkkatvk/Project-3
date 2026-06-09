public class SecurityPassAdvisor implements CallAdvisor {
    private final VectorStore vectorStore;

    // The constructor catches the tool here!
    public SecurityPassAdvisor(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public AdvisedRequest adviseCall(AdvisedRequest request) {
        // Our filtering and caching logic will live here
        return request;
    }
}