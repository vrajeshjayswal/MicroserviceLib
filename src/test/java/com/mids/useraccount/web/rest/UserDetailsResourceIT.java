package com.mids.useraccount.web.rest;

import com.mids.useraccount.UserAccountApp;
import com.mids.useraccount.config.TestSecurityConfiguration;
import com.mids.useraccount.domain.UserDetails;
import com.mids.useraccount.repository.UserDetailsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserDetailsResource} REST controller.
 */
@SpringBootTest(classes = { UserAccountApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class UserDetailsResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SUBSCRIBE_TO_OFFER = false;
    private static final Boolean UPDATED_SUBSCRIBE_TO_OFFER = true;

    private static final LocalDate DEFAULT_PASSWORD_CHANGED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PASSWORD_CHANGED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PREFERRED_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_PREFERRED_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_PREFERRED_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_PREFERRED_CURRENCY = "BBBBBBBBBB";

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserDetailsMockMvc;

    private UserDetails userDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDetails createEntity(EntityManager em) {
        UserDetails userDetails = new UserDetails()
            .type(DEFAULT_TYPE)
            .gender(DEFAULT_GENDER)
            .birthDate(DEFAULT_BIRTH_DATE)
            .phone(DEFAULT_PHONE)
            .subscribeToOffer(DEFAULT_SUBSCRIBE_TO_OFFER)
            .passwordChangedOn(DEFAULT_PASSWORD_CHANGED_ON)
            .preferredLanguage(DEFAULT_PREFERRED_LANGUAGE)
            .preferredCurrency(DEFAULT_PREFERRED_CURRENCY);
        return userDetails;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDetails createUpdatedEntity(EntityManager em) {
        UserDetails userDetails = new UserDetails()
            .type(UPDATED_TYPE)
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE)
            .phone(UPDATED_PHONE)
            .subscribeToOffer(UPDATED_SUBSCRIBE_TO_OFFER)
            .passwordChangedOn(UPDATED_PASSWORD_CHANGED_ON)
            .preferredLanguage(UPDATED_PREFERRED_LANGUAGE)
            .preferredCurrency(UPDATED_PREFERRED_CURRENCY);
        return userDetails;
    }

    @BeforeEach
    public void initTest() {
        userDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserDetails() throws Exception {
        int databaseSizeBeforeCreate = userDetailsRepository.findAll().size();
        // Create the UserDetails
        restUserDetailsMockMvc.perform(post("/api/user-details").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userDetails)))
            .andExpect(status().isCreated());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        UserDetails testUserDetails = userDetailsList.get(userDetailsList.size() - 1);
        assertThat(testUserDetails.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testUserDetails.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testUserDetails.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testUserDetails.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserDetails.isSubscribeToOffer()).isEqualTo(DEFAULT_SUBSCRIBE_TO_OFFER);
        assertThat(testUserDetails.getPasswordChangedOn()).isEqualTo(DEFAULT_PASSWORD_CHANGED_ON);
        assertThat(testUserDetails.getPreferredLanguage()).isEqualTo(DEFAULT_PREFERRED_LANGUAGE);
        assertThat(testUserDetails.getPreferredCurrency()).isEqualTo(DEFAULT_PREFERRED_CURRENCY);
    }

    @Test
    @Transactional
    public void createUserDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userDetailsRepository.findAll().size();

        // Create the UserDetails with an existing ID
        userDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserDetailsMockMvc.perform(post("/api/user-details").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userDetails)))
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList
        restUserDetailsMockMvc.perform(get("/api/user-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].subscribeToOffer").value(hasItem(DEFAULT_SUBSCRIBE_TO_OFFER.booleanValue())))
            .andExpect(jsonPath("$.[*].passwordChangedOn").value(hasItem(DEFAULT_PASSWORD_CHANGED_ON.toString())))
            .andExpect(jsonPath("$.[*].preferredLanguage").value(hasItem(DEFAULT_PREFERRED_LANGUAGE)))
            .andExpect(jsonPath("$.[*].preferredCurrency").value(hasItem(DEFAULT_PREFERRED_CURRENCY)));
    }
    
    @Test
    @Transactional
    public void getUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get the userDetails
        restUserDetailsMockMvc.perform(get("/api/user-details/{id}", userDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userDetails.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.subscribeToOffer").value(DEFAULT_SUBSCRIBE_TO_OFFER.booleanValue()))
            .andExpect(jsonPath("$.passwordChangedOn").value(DEFAULT_PASSWORD_CHANGED_ON.toString()))
            .andExpect(jsonPath("$.preferredLanguage").value(DEFAULT_PREFERRED_LANGUAGE))
            .andExpect(jsonPath("$.preferredCurrency").value(DEFAULT_PREFERRED_CURRENCY));
    }
    @Test
    @Transactional
    public void getNonExistingUserDetails() throws Exception {
        // Get the userDetails
        restUserDetailsMockMvc.perform(get("/api/user-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();

        // Update the userDetails
        UserDetails updatedUserDetails = userDetailsRepository.findById(userDetails.getId()).get();
        // Disconnect from session so that the updates on updatedUserDetails are not directly saved in db
        em.detach(updatedUserDetails);
        updatedUserDetails
            .type(UPDATED_TYPE)
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE)
            .phone(UPDATED_PHONE)
            .subscribeToOffer(UPDATED_SUBSCRIBE_TO_OFFER)
            .passwordChangedOn(UPDATED_PASSWORD_CHANGED_ON)
            .preferredLanguage(UPDATED_PREFERRED_LANGUAGE)
            .preferredCurrency(UPDATED_PREFERRED_CURRENCY);

        restUserDetailsMockMvc.perform(put("/api/user-details").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserDetails)))
            .andExpect(status().isOk());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
        UserDetails testUserDetails = userDetailsList.get(userDetailsList.size() - 1);
        assertThat(testUserDetails.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testUserDetails.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testUserDetails.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testUserDetails.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserDetails.isSubscribeToOffer()).isEqualTo(UPDATED_SUBSCRIBE_TO_OFFER);
        assertThat(testUserDetails.getPasswordChangedOn()).isEqualTo(UPDATED_PASSWORD_CHANGED_ON);
        assertThat(testUserDetails.getPreferredLanguage()).isEqualTo(UPDATED_PREFERRED_LANGUAGE);
        assertThat(testUserDetails.getPreferredCurrency()).isEqualTo(UPDATED_PREFERRED_CURRENCY);
    }

    @Test
    @Transactional
    public void updateNonExistingUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc.perform(put("/api/user-details").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userDetails)))
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        int databaseSizeBeforeDelete = userDetailsRepository.findAll().size();

        // Delete the userDetails
        restUserDetailsMockMvc.perform(delete("/api/user-details/{id}", userDetails.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
