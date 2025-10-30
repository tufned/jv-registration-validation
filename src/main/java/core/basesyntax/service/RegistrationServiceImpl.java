package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.exceptions.InvalidUserException;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private static final int MIN_AGE = 18;
    private static final int MIN_LOGIN_LENGTH = 6;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private final StorageDao storageDao = new StorageDaoImpl();

    @Override
    public User register(User user) throws InvalidUserException {
        if (user == null) {
            throw new InvalidUserException("User must not be null");
        }
        checkUserExists(user);
        checkLogin(user.getLogin());
        checkPassword(user.getPassword());
        checkAge(user.getAge());

        return storageDao.add(user);
    }

    private void checkUserExists(User user) {
        if (Storage.people.isEmpty()) {
            return;
        }
        User existedUser = storageDao.get(user.getLogin());
        if (existedUser != null) {
            throw new InvalidUserException("User already exists");
        }
    }

    private void checkLogin(String login) {
        if (login == null) {
            throw new InvalidUserException("Login can't be null");
        }
        if (login.length() < MIN_LOGIN_LENGTH) {
            throw new InvalidUserException("Login must contain at least 6 characters");
        }
    }

    private void checkPassword(String password) {
        if (password == null) {
            throw new InvalidUserException("Password can't be null");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidUserException("Password must contain at least 6 characters");
        }
    }

    private void checkAge(Integer age) {
        if (age == null) {
            throw new InvalidUserException("Age can't be null");
        }
        if (age < MIN_AGE) {
            throw new InvalidUserException("User must be at least " + MIN_AGE + " years old");
        }
    }
}
