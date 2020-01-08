package com.marvel.gps.repository;

import com.marvel.gps.model.UserVMProvison;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UserVMProvisionRepository extends PagingAndSortingRepository<UserVMProvison, Long> {
    public List<UserVMProvison> findByGPSUser_userId(Long userId);

    @Query(nativeQuery=true, value="SELECT provision_id, created_at, updated_at, cpu_cores, hard_disk, os, ram, user_id\n" +
            "\tFROM uservmprovision where user_id = :userId order by ram desc limit :count")
    public List<UserVMProvison> getVMListForUser_RAMDESC(Long userId, int count);

    @Query(nativeQuery=true, value="SELECT provision_id, created_at, updated_at, cpu_cores, hard_disk, os, ram, user_id\n" +
            "\tFROM uservmprovision order by ram desc limit :count")
    public List<UserVMProvison> getVMList_RAMDESC(int count);

    public void deleteByGPSUser_userId(Long userId);
}
