package com.teamapt.offlinesales.testing.app.controller;

import com.teamapt.offlinesales.testing.domain.model.UserModel;
import com.teamapt.offlinesales.testing.domain.model.UserRequest;
import com.teamapt.offlinesales.testing.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserModel create(@Valid @RequestBody UserRequest request) {
        return userService.create(request);
    }

    @GetMapping
    public Page<UserModel> getAll(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return userService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public UserModel getById(@PathVariable("id") UUID id) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id) {
        userService.deleteById(id);
    }
}
