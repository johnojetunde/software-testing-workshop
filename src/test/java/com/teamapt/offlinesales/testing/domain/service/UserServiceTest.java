package com.teamapt.offlinesales.testing.domain.service;

import com.teamapt.offlinesales.testing.domain.exceptions.UserServiceBadRequestException;
import com.teamapt.offlinesales.testing.domain.exceptions.UserServiceNotFoundException;
import com.teamapt.offlinesales.testing.domain.model.UserModel;
import com.teamapt.offlinesales.testing.domain.model.UserRequest;
import com.teamapt.offlinesales.testing.persistence.entity.User;
import com.teamapt.offlinesales.testing.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, new UserMapperImpl());
    }

    @Test
    void createUserFails_whenRequestIsNull() {
        assertThatThrownBy(() -> userService.create(null))
                .isInstanceOf(NullPointerException.class);

        verify(userRepository, times(0)).existsByEmail(anyString());
    }

    @Test
    void createUserFails_whenEmailExists() {
        //given
        when(userRepository.existsByEmail("admin@moniepoint.com"))
                .thenReturn(true);
        UserRequest request = buildUserRequest();

        //when
        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(UserServiceBadRequestException.class)
                .hasMessage("User with email exists");

        verify(userRepository).existsByEmail("admin@moniepoint.com");
    }

    private UserRequest buildUserRequest() {
        return UserRequest.builder()
                .email("admin@moniepoint.com")
                .firstName("Admin")
                .lastName("Moniepoint")
                .address("Lagos, Nigeria")
                .gender(User.Gender.FEMALE)
                .build();
    }

    @Test
    void createUser_isSuccessful() {
        //given
        when(userRepository.existsByEmail("admin@moniepoint.com"))
                .thenReturn(false);
        UUID id = UUID.randomUUID();
        when(userRepository.save(isA(User.class)))
                .then(i -> {
                    var user = i.getArgument(0, User.class);
                    user.setId(id);
                    user.setCreatedAt(LocalDateTime.now());
                    user.setUpdatedAt(LocalDateTime.now());

                    return user;
                });
        UserRequest request = buildUserRequest();

        //when
        UserModel result = userService.create(request);

        //then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getEmail()).isEqualTo("admin@moniepoint.com");
        assertThat(result.getFirstName()).isEqualTo("Admin");
        assertThat(result.getLastName()).isEqualTo("Moniepoint");
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    void getByIdFails_whenIdDoesNotExist() {
        //given when then
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(id))
                .isInstanceOf(UserServiceNotFoundException.class)
                .hasMessage(String.format("User with id %s not found", id));

        verify(userRepository).findById(id);
    }

    @Test
    void getById_successful() {
        UUID id = UUID.randomUUID();

        User user = User.builder()
                .id(id)
                .address("Lagos, Nigeria")
                .firstName("Admin")
                .lastName("Moniepoint")
                .gender(User.Gender.FEMALE)
                .email("email@moniepoint.com")
                .build();

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        UserModel result = userService.getById(id);

        assertThat(result.getEmail()).isEqualTo("email@moniepoint.com");
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getFirstName()).isEqualTo("Admin");
        assertThat(result.getLastName()).isEqualTo("Moniepoint");
    }
}