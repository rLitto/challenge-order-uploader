package com.igindex.challenge.application;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class DestinationProperties {
    @NotNull
    @Valid
    private ConnectionProperties connection;
    @NotNull
    private DestinationType type = DestinationType.Queue;
    @NotEmpty
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DestinationType getType() {
        return type;
    }

    public void setType(DestinationType type) {
        this.type = type;
    }

    public ConnectionProperties getConnection() {
        return connection;
    }

    public void setConnection(ConnectionProperties connection) {
        this.connection = connection;
    }

    public boolean isTopic() {
        return DestinationType.Topic.equals(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DestinationProperties that = (DestinationProperties) o;
        return Objects.equals(connection, that.connection) &&
                type == that.type &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(connection, type, name);
    }
}
