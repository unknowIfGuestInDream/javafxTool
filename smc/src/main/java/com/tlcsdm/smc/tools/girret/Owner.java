package com.tlcsdm.smc.tools.girret;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * girret user对象.
 * 
 * @author unknowIfGuestInDream
 */
public class Owner implements Serializable {

    private static final long serialVersionUID = -5620740885588088377L;

    @JsonProperty("_account_id")
    private Long accountId;
    private String name;
    private String email;
    private String username;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
