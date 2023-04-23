package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.dto.request.CommentRequestDto;
import ru.practicum.shareit.dto.request.ItemRequestDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";


    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new).build()
        );
    }


    //create
    public ResponseEntity<Object> addItem(ItemRequestDto itemDto, Long ownerId) {
        return post("", itemDto, ownerId);
    }

    public ResponseEntity<Object> addComment(CommentRequestDto commentDto, Long itemId, Long bookerId) {
        return post("/" + itemId + "/comment", commentDto, bookerId);
    }

    //read
    public ResponseEntity<Object> getItem(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllItems(Long ownerId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", ownerId, parameters);
    }

    public ResponseEntity<Object> getSearchedItems(String searchText, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "searchText", searchText,
                "from", from,
                "size", size
        );
        return get("/search?searchText={searchText}&from={from}&size={size}", parameters);
    }

    //update
    public ResponseEntity<Object> updateItem(Long itemId, ItemRequestDto itemDto, Long ownerId) {
        return patch("/" + itemId, itemDto, ownerId);
    }

    //delete
    public ResponseEntity<Object> deleteItem(Long itemId) {
        return delete("/" + itemId);
    }
}
