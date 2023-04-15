package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(readOnly = true)
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Transactional
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
    public ItemRequestFullResponseDto getItemRequest(int userId, int requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User id = " + userId + " doesn't exist.");
        }
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Item Request id = " + requestId + " doesn't exist."));
        return ItemRequestMapper.toFullItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestFullResponseDto> getOtherItemRequests(int userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User id = " + userId + " doesn't exist.");
        }

        return itemRequestRepository.findAllByRequester_IdNot(userId, PageRequest.of(from / size, size, Sort.by("created").descending()))
                .stream().map(ItemRequestMapper::toFullItemRequestDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestFullResponseDto> getOwnItemRequests(int requesterId) {
        if (!userRepository.existsById(requesterId)) {
            throw new NoSuchElementException("User id = " + requesterId + " doesn't exist.");
        }

        return itemRequestRepository.findAllByRequester_Id(requesterId, Sort.by("created").descending())
                .stream().map(ItemRequestMapper::toFullItemRequestDto).collect(Collectors.toList());
    }
}
