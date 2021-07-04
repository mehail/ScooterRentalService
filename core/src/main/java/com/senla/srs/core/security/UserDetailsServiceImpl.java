package com.senla.srs.core.security;

import com.senla.srs.core.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository
                .findByAccount_Email(email)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));

        return com.senla.srs.core.security.SecurityUser.fromUser(user);
    }

}
