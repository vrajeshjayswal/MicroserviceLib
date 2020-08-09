package com.mids.useraccount.web.rest;

import com.mids.useraccount.domain.UserAddress;
import com.mids.useraccount.repository.UserAddressRepository;
import com.mids.useraccount.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mids.useraccount.domain.UserAddress}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserAddressResource {

    private final Logger log = LoggerFactory.getLogger(UserAddressResource.class);

    private static final String ENTITY_NAME = "userAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAddressRepository userAddressRepository;

    public UserAddressResource(UserAddressRepository userAddressRepository) {
        this.userAddressRepository = userAddressRepository;
    }

    /**
     * {@code POST  /user-addresses} : Create a new userAddress.
     *
     * @param userAddress the userAddress to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAddress, or with status {@code 400 (Bad Request)} if the userAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-addresses")
    public ResponseEntity<UserAddress> createUserAddress(@RequestBody UserAddress userAddress) throws URISyntaxException {
        log.debug("REST request to save UserAddress : {}", userAddress);
        if (userAddress.getId() != null) {
            throw new BadRequestAlertException("A new userAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAddress result = userAddressRepository.save(userAddress);
        return ResponseEntity.created(new URI("/api/user-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-addresses} : Updates an existing userAddress.
     *
     * @param userAddress the userAddress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAddress,
     * or with status {@code 400 (Bad Request)} if the userAddress is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAddress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-addresses")
    public ResponseEntity<UserAddress> updateUserAddress(@RequestBody UserAddress userAddress) throws URISyntaxException {
        log.debug("REST request to update UserAddress : {}", userAddress);
        if (userAddress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserAddress result = userAddressRepository.save(userAddress);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userAddress.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-addresses} : get all the userAddresses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAddresses in body.
     */
    @GetMapping("/user-addresses")
    public List<UserAddress> getAllUserAddresses() {
        log.debug("REST request to get all UserAddresses");
        return userAddressRepository.findAll();
    }

    /**
     * {@code GET  /user-addresses/:id} : get the "id" userAddress.
     *
     * @param id the id of the userAddress to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAddress, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-addresses/{id}")
    public ResponseEntity<UserAddress> getUserAddress(@PathVariable Long id) {
        log.debug("REST request to get UserAddress : {}", id);
        Optional<UserAddress> userAddress = userAddressRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userAddress);
    }

    /**
     * {@code DELETE  /user-addresses/:id} : delete the "id" userAddress.
     *
     * @param id the id of the userAddress to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-addresses/{id}")
    public ResponseEntity<Void> deleteUserAddress(@PathVariable Long id) {
        log.debug("REST request to delete UserAddress : {}", id);
        userAddressRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
