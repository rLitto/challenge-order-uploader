package com.igindex.challenge.application;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class ConnectionProperties {
    @NotEmpty
    private String brokerUrl;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionProperties that = (ConnectionProperties) o;
        return Objects.equals(brokerUrl, that.brokerUrl) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(brokerUrl, username, password);
    }
}
