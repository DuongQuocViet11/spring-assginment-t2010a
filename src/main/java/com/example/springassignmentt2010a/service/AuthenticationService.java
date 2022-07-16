package com.example.springassignmentt2010a.service;

import com.example.springassignmentt2010a.dto.AccountDTO;
import com.example.springassignmentt2010a.dto.RegisterDTO;
import com.example.springassignmentt2010a.entity.Account;
import com.example.springassignmentt2010a.entity.Role;
import com.example.springassignmentt2010a.repository.AccountRepository;
import com.example.springassignmentt2010a.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private static final String USER_ROLE = "user";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        Account account= accountOptional.orElse(null);
        if (account == null){
            throw new UsernameNotFoundException("User not found in database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role: account.getRoles()){
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        UserDetails userDetails
                = new User(account.getUsername(), account.getPassword(), authorities);
        return userDetails;
    }

        public AccountDTO saveAccount(RegisterDTO registerDTO) {
            Set<Role> roles = new HashSet<>();
            for (Role role: registerDTO.getRoles()) {
                Optional<Role> userRoleOptional = roleRepository.findByName(role.getName());
                Role userRole = userRoleOptional.orElse(null);
                if (userRole == null) {
                    return null;
                }
                roles.add(userRoleOptional.get());
            }


            Optional<Account> byUsername = accountRepository.findByUsername(registerDTO.getUsername());
            if (byUsername.isPresent()) {
                return null;
            }
        Account account = new Account();
        account.setUsername(registerDTO.getUsername());
        account.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        account.setCreatedAt(new Date());
        account.setUpdatedAt(new Date());
        account.setStatus(1);
        account.setRoles(roles);
        Account save = accountRepository.save(account);
        return new AccountDTO(save);
    }

    public Account getAccount(String username) {
        Optional<Account> byUsername = accountRepository.findByUsername(username);
        return byUsername.orElse(null);
    }
}
