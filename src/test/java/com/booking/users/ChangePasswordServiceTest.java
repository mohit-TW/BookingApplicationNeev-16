package com.booking.users;

import com.booking.exceptions.ChangePasswordException;
import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import com.booking.users.view.model.ChangePasswordRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static com.booking.shows.respository.Constants.TOTAL_NO_OF_SEATS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChangePasswordServiceTest {
    @Test
    void shouldFailWhenNewPasswordDoesNotMatchConfirmNewPassword() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Password", "NewPassword", "DifferentPassword");
        ChangePasswordService changePasswordService = new ChangePasswordService(mockUserRepository);
        String expectedExceptionMessage = "New password does not match confirm new password";

        String username = "test-user";
        ChangePasswordException changePasswordException = assertThrows(ChangePasswordException.class, () -> changePasswordService.execute(changePasswordRequest, username));
        assertEquals(changePasswordException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenPasswordPatternIsNotFollowed() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Password", "NewPassword", "NewPassword");
        ChangePasswordService changePasswordService = new ChangePasswordService(mockUserRepository);
        String expectedExceptionMessage = "New password does not match the pattern";

        String username = "test-user";
        ChangePasswordException changePasswordException = assertThrows(ChangePasswordException.class, () -> changePasswordService.execute(changePasswordRequest, username));
        assertEquals(changePasswordException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenOldPasswordIsWrong() {
        String username = "test-user-name";
        User user = new User(username, "correct-password");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("incorrect-password", "newPassword@1", "newPassword@1");
        ChangePasswordService changePasswordService = new ChangePasswordService(mockUserRepository);
        String expectedExceptionMessage = "Old password is wrong";

        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        ChangePasswordException changePasswordException = assertThrows(ChangePasswordException.class, () -> changePasswordService.execute(changePasswordRequest, username));
        assertEquals(changePasswordException.getMessage(), expectedExceptionMessage);
        verify(mockUserRepository).findByUsername(username);
    }

    @Test
    void shouldFailWhenOldPasswordIsSameAsNewPassword() {
        String username = "test-user-name";
        User user = new User(username, "Correct-password@1");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Correct-password@1", "Correct-password@1", "Correct-password@1");
        ChangePasswordService changePasswordService = new ChangePasswordService(mockUserRepository);
        String expectedExceptionMessage = "New password is same as old password";

        ChangePasswordException changePasswordException = assertThrows(ChangePasswordException.class, () -> changePasswordService.execute(changePasswordRequest, username));
        assertEquals(changePasswordException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldUpdatePasswordForSuccessfulCase() {
        String username = "test-user-name";
        User user = new User(username, "password@1");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        String newPassword = "New-password@1";
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("password@1", newPassword, newPassword);
        ChangePasswordService changePasswordService = new ChangePasswordService(mockUserRepository);

        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        assertDoesNotThrow(() -> changePasswordService.execute(changePasswordRequest, username));
        verify(mockUserRepository).findByUsername(username);
        verify(mockUserRepository).save(new User(username,newPassword));
    }
}