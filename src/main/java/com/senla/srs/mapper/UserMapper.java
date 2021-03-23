package com.senla.srs.mapper;

import com.senla.srs.dto.UserDto;
import com.senla.srs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractMapper<User, UserDto> {

    @Autowired
    public UserMapper() {
        super(User.class, UserDto.class);
    }

}
