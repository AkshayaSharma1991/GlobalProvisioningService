package com.marvel.gps.repository;

import com.marvel.gps.model.GPSUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
public interface GPSUserRepository extends CrudRepository<GPSUser, Long> {
    GPSUser findByEmail(String email);

    GPSUser findByMobileNo(String mobileNo);

    GPSUser findByUserName(String username);

    GPSUser findByUserId(Long userId);


}
