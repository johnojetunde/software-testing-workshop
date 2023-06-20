package com.teamapt.offlinesales.testing.domain.service;

import com.teamapt.offlinesales.testing.domain.model.UserModel;
import com.teamapt.offlinesales.testing.persistence.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserModel toModel(User user);
}
