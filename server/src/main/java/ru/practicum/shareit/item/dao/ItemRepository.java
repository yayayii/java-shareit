package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(
            String name, String description, Pageable pageable
    );

    List<Item> findByOwner_IdOrderById(Long ownerId, Pageable pageable);
}
