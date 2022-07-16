package com.example.springassignmentt2010a.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springassignmentt2010a.dto.AccountDTO;
import com.example.springassignmentt2010a.dto.CredentialDTO;
import com.example.springassignmentt2010a.dto.RegisterDTO;
import com.example.springassignmentt2010a.entity.Account;
import com.example.springassignmentt2010a.entity.Role;
import com.example.springassignmentt2010a.service.AuthenticationService;
import com.example.springassignmentt2010a.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestBody RegisterDTO registerDTO){
        AccountDTO account = authenticationService.saveAccount(registerDTO);
        return ResponseEntity.ok().body(account);
    }
    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            return ResponseEntity.badRequest().body("require token in header");
        }
        try {
            String token = authorizationHeader.replace("Bearer", "").trim();
            DecodedJWT decodedJWT = JWTUtil.getDecodedJwt(token);
            String username = decodedJWT.getSubject();

            Account account = authenticationService.getAccount(username);
            if (account == null){
                return ResponseEntity.badRequest().body("Wrong token: Username not exist");
            }
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            List<String> roles = new ArrayList<>();
            for (Role role: account.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
                roles.add(role.getName());
            }
            String accessToken = JWTUtil.generateToken(
                    account.getUsername(),
                    roles,
                    request.getRequestURL().toString(),
                    JWTUtil.ONE_DAY * 7);

            String refreshToken = JWTUtil.generateToken(
                    account.getUsername(),
                    null,
                    request.getRequestURL().toString(),
                    JWTUtil.ONE_DAY * 14);
            CredentialDTO credential = new CredentialDTO(accessToken, refreshToken,roles);
            return ResponseEntity.ok(credential);
        }catch (Exception ex){
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
}
