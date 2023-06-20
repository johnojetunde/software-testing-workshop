package com.teamapt.offlinesales.testing.domain.service;

import com.teamapt.offlinesales.testing.domain.exceptions.UserServiceBadRequestException;
import com.teamapt.offlinesales.testing.domain.exceptions.UserServiceNotFoundException;
import com.teamapt.offlinesales.testing.domain.model.UserModel;
import com.teamapt.offlinesales.testing.domain.model.UserRequest;
import com.teamapt.offlinesales.testing.persistence.entity.User;
import com.teamapt.offlinesales.testing.persistence.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Test
     * test null exception is throw when request is null
     * test that exception is thrown when email exists
     * save correctly and maps to user model
     *
     *
     */
    public UserModel create(@NonNull UserRequest request) {
        boolean emailExists = userRepository.existsByEmail(request.getEmail());

        if (emailExists) {
            throw new UserServiceBadRequestException("User with email exists");
        }

        User user = User.builder()
                .gender(request.getGender())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .build();

        user = userRepository.save(user);

        return userMapper.toModel(user);
    }

    public UserModel getById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toModel)
                .orElseThrow(() -> new UserServiceNotFoundException("User with id %s not found", id));
    }

    public Page<UserModel> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toModel);
    }

    public void deleteById(UUID id) {
        getById(id);
        userRepository.deleteById(id);
    }
}
