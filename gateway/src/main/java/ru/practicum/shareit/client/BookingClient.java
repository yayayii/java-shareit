package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.util.RequestState;
import ru.practicum.shareit.dto.request.BookingRequestDto;

import java.util.Map;


@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";


    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new).build()
        );
    }


    //create
    public ResponseEntity<Object> addBooking(BookingRequestDto bookingDto, Long bookerId) {
        return post("", bookingDto, bookerId);
    }

    //read
    public ResponseEntity<Object> getBooking(Long bookingId, Long userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllBookings(Long bookerId, RequestState state, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", bookerId, parameters);
    }

    public ResponseEntity<Object> getAllBookingsFromOwner(Long ownerId, RequestState state, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", ownerId, parameters);
    }

    //update
    public ResponseEntity<Object> updateBooking(Long bookingId, Long ownerId, boolean isApproved) {
        Map<String, Object> parameters = Map.of("isApproved", isApproved);
        return patch("/" + bookingId + "?isApproved={isApproved}", ownerId, parameters);
    }
}
