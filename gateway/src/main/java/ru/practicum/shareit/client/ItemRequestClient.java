package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.dto.request.ItemRequestRequestDto;

import java.util.Map;


@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";


    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new).build()
        );
    }


    public ResponseEntity<Object> addItemRequest(ItemRequestRequestDto itemRequestDto, Long requesterId) {
        return post("", itemRequestDto, requesterId);
    }

    public ResponseEntity<Object> getItemRequest(Long requestId, Long userId) {
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> getOwnItemRequests(Long requesterId) {
        return get("", requesterId);
    }

    public ResponseEntity<Object> getOtherItemRequests(Long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }
}
