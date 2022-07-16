package com.example.springassignmentt2010a.dto;

import com.example.springassignmentt2010a.entity.Account;
import com.example.springassignmentt2010a.entity.Role;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class AccountDTO {
    private long id;
    private String username;
    private Set<Role> roles;
    private int status;
    private Date createdAt;
    private Date updatedAt;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.roles = account.getRoles();
        this.status = account.getStatus();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();
    }
}
