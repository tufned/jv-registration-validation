package core.basesyntax.service;

import core.basesyntax.db.Storage;
import core.basesyntax.exceptions.InvalidUserException;
import core.basesyntax.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceImplTest {
    private static RegistrationServiceImpl registrationService;
    private static List<User> people;
    private User user;

    @BeforeAll
    static void beforeAll() {
        registrationService = new RegistrationServiceImpl();
        people = Storage.people;
    }

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1234L);
        user.setLogin("login1");
        user.setPassword("password");
        user.setAge(18);
    }

    @Test
    void register_userNull_notOk() {
        assertThrows(InvalidUserException.class, () -> registrationService.register(null));
    }

    @Test
    void register_userValid_ok() {
        User actualUser = registrationService.register(user);
        assertEquals(1, people.size());
        assertEquals(people.get(0), user);
        assertEquals(user, actualUser);

        User newUser = new User();
        newUser.setId(888L);
        newUser.setLogin("login2");
        newUser.setPassword("newpassword");
        newUser.setAge(20);
        actualUser = registrationService.register(newUser);
        assertEquals(2, people.size());
        assertEquals(people.get(1), newUser);
        assertEquals(newUser, actualUser);
    }

    @Test
    void register_userAlreadyExists_notOk() {
        registrationService.register(user);
        assertThrows(InvalidUserException.class, () -> {
            User newUser = new User();
            user.setId(888L);
            user.setPassword("newpassword");
            user.setPassword("login1");
            user.setAge(18);
            registrationService.register(newUser);
        });
    }

    @Test
    void register_checkLogin_notOk() {
        user.setLogin("login");
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));
        user.setLogin(null);
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));
    }

    @Test
    void register_checkPassword_notOk() {
        user.setPassword("passw");
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));

        user.setPassword("");
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));

        user.setPassword("abc");
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));

        user.setPassword(null);
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));
    }

    @Test
    void register_checkAge_notOk() {
        user.setAge(17);
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));
        user.setAge(-17);
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));
        user.setAge(null);
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));
    }

    @AfterEach
    void afterEach() {
        people.clear();
    }
}