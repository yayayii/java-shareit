package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestFullResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestResponseDto addItemRequest(ItemRequestRequestDto itemRequestDto, int requesterId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new NoSuchElementException("User id = " + requesterId + " doesn't exist."));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(requester);

        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestFullResponseDto> getItemRequests(int requesterId) {
        if (!userRepository.existsById(requesterId)) {
            throw new NoSuchElementException("User id = " + requesterId + " doesn't exist.");
        }

        return itemRequestRepository.findAllByRequester_Id(requesterId, Sort.by(Sort.Order.desc("created")))
                .stream().map(ItemRequestMapper::toFullItemRequestDto).collect(Collectors.toList());
    }

    @Override
    public ItemRequestFullResponseDto getItemRequest(int requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Item Request id = " + requestId + " doesn't exist."));
        return ItemRequestMapper.toFullItemRequestDto(itemRequest);
    }
}
