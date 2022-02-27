package com.exampl.taskstep.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * @date: Feb.22.2022
 * @author: Mihhail Daniljuk
 * @email: daniljukmihhail@gmail.com
 */

@Entity
@Table(name = "proxy")
public class Proxy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @Column(unique = true)
    @Length(max=120, message = "Name is too long")
    @NotBlank
    private String name;


    @Enumerated(EnumType.STRING)
    private ProxyType type;


    @Column(unique = true)
    @Length(max=120)
    @NotBlank
    private String hostname;


    private int port;


    @NotBlank
    @Length(max = 30, message = "Username can't be longer than 30 characters")
    private String username;


    @Length(min=6, max=30, message = "Password length 6...30")
    private String password;

    private boolean active;


    public Proxy() {
    }

    public Proxy(long id, String name, ProxyType type, String hostname, int port, String username, String password, boolean active) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.active = active;
    }




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProxyType getType() {
        return type;
    }

    public void setType(ProxyType type) {
        this.type = type;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", hostname='" + hostname + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                '}';
    }
}
