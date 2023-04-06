package ru.practicum.shareit;

import lombok.AllArgsConstructor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Transactional
@Rollback(false)
@AllArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "spring.datasource.url=jdbc:postgresql://localhost:5432/test")
public class IntegrationTest {
    private final EntityManager entityManager;
    private final UserService service;

    @Test
    void saveUser() {
        UserRequestDto userRequestDto = new UserRequestDto("1", "1");
        service.addUser(userRequestDto);
        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.email = ?1", User.class);
        User user = query.setParameter(1, userRequestDto.getEmail()).getSingleResult();

        assertNotEquals(
                user.getId(),
                0
        );
        assertEquals(
                user.getName(),
                userRequestDto.getName()
        );
        assertEquals(
                user.getEmail(),
                userRequestDto.getEmail()
        );
    }
}
