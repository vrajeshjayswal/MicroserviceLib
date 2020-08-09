package com.mids.useraccount.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A UserDetails.
 */
@Entity
@Table(name = "user_details")
public class UserDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "phone")
    private String phone;

    @Column(name = "subscribe_to_offer")
    private Boolean subscribeToOffer;

    @Column(name = "password_changed_on")
    private LocalDate passwordChangedOn;

    @Column(name = "preferred_language")
    private String preferredLanguage;

    @Column(name = "preferred_currency")
    private String preferredCurrency;

    @OneToOne
    @JoinColumn(unique = true, updatable = false, insertable = false)
    private User user;

    @OneToMany(mappedBy = "userDetails")
    private Set<UserAddress> userAddresses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public UserDetails type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public UserDetails gender(String gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public UserDetails birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public UserDetails phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean isSubscribeToOffer() {
        return subscribeToOffer;
    }

    public UserDetails subscribeToOffer(Boolean subscribeToOffer) {
        this.subscribeToOffer = subscribeToOffer;
        return this;
    }

    public void setSubscribeToOffer(Boolean subscribeToOffer) {
        this.subscribeToOffer = subscribeToOffer;
    }

    public LocalDate getPasswordChangedOn() {
        return passwordChangedOn;
    }

    public UserDetails passwordChangedOn(LocalDate passwordChangedOn) {
        this.passwordChangedOn = passwordChangedOn;
        return this;
    }

    public void setPasswordChangedOn(LocalDate passwordChangedOn) {
        this.passwordChangedOn = passwordChangedOn;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public UserDetails preferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
        return this;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getPreferredCurrency() {
        return preferredCurrency;
    }

    public UserDetails preferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
        return this;
    }

    public void setPreferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }

    public User getUser() {
        return user;
    }

    public UserDetails user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<UserAddress> getUserAddresses() {
        return userAddresses;
    }

    public UserDetails userAddresses(Set<UserAddress> userAddresses) {
        this.userAddresses = userAddresses;
        return this;
    }

    public UserDetails addUserAddress(UserAddress userAddress) {
        this.userAddresses.add(userAddress);
        userAddress.setUserDetails(this);
        return this;
    }

    public UserDetails removeUserAddress(UserAddress userAddress) {
        this.userAddresses.remove(userAddress);
        userAddress.setUserDetails(null);
        return this;
    }

    public void setUserAddresses(Set<UserAddress> userAddresses) {
        this.userAddresses = userAddresses;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDetails)) {
            return false;
        }
        return id != null && id.equals(((UserDetails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDetails{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", gender='" + getGender() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", phone='" + getPhone() + "'" +
            ", subscribeToOffer='" + isSubscribeToOffer() + "'" +
            ", passwordChangedOn='" + getPasswordChangedOn() + "'" +
            ", preferredLanguage='" + getPreferredLanguage() + "'" +
            ", preferredCurrency='" + getPreferredCurrency() + "'" +
            "}";
    }
}
