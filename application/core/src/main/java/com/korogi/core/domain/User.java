package com.korogi.core.domain;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity class representing a User in the database.
 *
 * @author Daan Peelman
 * @see BaseEntity
 * @see UserBuilder
 */
@Data
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PUBLIC)
@Builder(builderMethodName = "newUser")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

@Entity
@Table(name = "USERS")
public class User extends BaseEntity {
    private static final long serialVersionUID = - 7094382652553816643L;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "biography")
    private String biography;

    @Column(name = "salt")
    private byte[] salt;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "activated")
    private Boolean activated;
}