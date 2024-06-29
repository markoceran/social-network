package rs.ac.uns.ftn;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
@DependsOn("elasticsearchConfig") // Ensure elasticsearchConfig bean is initialized first
public class ElasticsearchIndexConfig {

    private final RestHighLevelClient client;

    @Autowired
    public ElasticsearchIndexConfig(RestHighLevelClient client) {
        this.client = client;
    }

    @PostConstruct
    public void setUpIndex() {
        try {
            String indexGroupName = "groups";
            if (!indexExists(client, indexGroupName)) {
                createGroupIndexWithMapping(client, indexGroupName);
            }
            String indexPostName = "posts";
            if (!indexExists(client, indexPostName)) {
                createPostIndexWithMapping(client, indexPostName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean indexExists(RestHighLevelClient client, String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }


    private void createGroupIndexWithMapping(RestHighLevelClient client, String indexName) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 1)
        );

        String mapping = "{\n" +
                "  \"properties\": {\n" +
                "    \"id\": { \"type\": \"long\" },\n" +
                "    \"name\": { \"type\": \"text\" },\n" +
                "    \"description\": { \"type\": \"text\" },\n" +
                "    \"pdfDescription\": { \"type\": \"text\" }\n" +
                "  }\n" +
                "}";

        request.mapping(mapping, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    private void createPostIndexWithMapping(RestHighLevelClient client, String indexName) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 1)
        );

        String mapping = "{\n" +
                "  \"properties\": {\n" +
                "    \"id\": { \"type\": \"long\" },\n" +
                "    \"title\": { \"type\": \"text\" },\n" +
                "    \"content\": { \"type\": \"text\" },\n" +
                "    \"pdfContent\": { \"type\": \"text\" }\n" +
                "  }\n" +
                "}";

        request.mapping(mapping, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
    }
}
