package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.Storage;
import core.basesyntax.exceptions.InvalidUserException;
import core.basesyntax.model.User;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private static final int HIGHER_AGE = 20;
    private static final int MIN_AGE = 18;
    private static final int INVALID_AGE = 17;
    private static final int INVALID_MINUS_AGE = -17;
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
        user.setAge(MIN_AGE);
    }

    @Test
    void register_userNull_notOk() {
        assertThrows(InvalidUserException.class, () -> registrationService.register(null));
    }

    @Test
    void register_firstUserValid_ok() {
        User actualUser = registrationService.register(user);
        assertEquals(1, people.size());
        assertEquals(people.get(0), user);
        assertEquals(user, actualUser);
    }

    @Test
    void register_secondUserValid_ok() {
        Storage.people.add(user);
        User newUser = new User();
        newUser.setId(888L);
        newUser.setLogin("login2");
        newUser.setPassword("newpassword");
        newUser.setAge(HIGHER_AGE);
        User actualUser = registrationService.register(newUser);
        assertEquals(2, people.size());
        assertEquals(people.get(1), newUser);
        assertEquals(newUser, actualUser);
    }

    @Test
    void register_userAlreadyExists_notOk() {
        Storage.people.add(user);
        User newUser = new User();
        newUser.setId(888L);
        newUser.setPassword("newpassword");
        newUser.setLogin(user.getLogin());
        newUser.setAge(MIN_AGE);
        assertThrows(InvalidUserException.class, () -> registrationService.register(newUser));
    }

    @Test
    void register_loginIsTooShort_notOk() {
        user.setLogin("login");
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));

        user.setLogin("");
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));

        user.setLogin("abc");
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));
    }

    @Test
    void register_loginIsNull_notOk() {
        user.setLogin(null);
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));
    }

    @Test
    void register_passwordIsTooShort_notOk() {
        user.setPassword("passw");
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));

        user.setPassword("");
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));

        user.setPassword("abc");
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));
    }

    @Test
    void register_passwordIsNull_notOk() {
        user.setPassword(null);
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));
    }

    @Test
    void register_ageIsTooSmall_notOk() {
        user.setAge(INVALID_AGE);
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));
        user.setAge(INVALID_MINUS_AGE);
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));
    }

    @Test
    void register_ageIsNull_notOk() {
        user.setAge(null);
        assertThrows(InvalidUserException.class, () -> registrationService.register(user));
    }

    @AfterEach
    void afterEach() {
        people.clear();
    }
}
