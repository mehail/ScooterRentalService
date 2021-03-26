package com.senla.srs.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.senla.srs.model.UserStatus;
import com.senla.srs.model.User;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
public class SecurityUser implements UserDetails {
    @NonNull
    private final String username;
    @NonNull
    private final String password;
    @NonNull
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean isActive;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromUser(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                user.getStatus().equals(UserStatus.ACTIVE),
                user.getStatus().equals(UserStatus.ACTIVE),
                user.getStatus().equals(UserStatus.ACTIVE),
                user.getStatus().equals(UserStatus.ACTIVE),
                user.getRole().getAuthorities()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecurityUser)) return false;

        SecurityUser that = (SecurityUser) o;

        if (isActive() != that.isActive()) return false;
        if (!getUsername().equals(that.getUsername())) return false;
        if (!getPassword().equals(that.getPassword())) return false;
        return getAuthorities().equals(that.getAuthorities());
    }

    @Override
    public int hashCode() {
        int result = getUsername().hashCode();
        result = 31 * result + getPassword().hashCode();
        result = 31 * result + getAuthorities().hashCode();
        result = 31 * result + (isActive() ? 1 : 0);
        return result;
    }
}
