package edu.fast_track.repository;

import edu.fast_track.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,Integer> {
    CustomerEntity findByEmailAndDelete(String email, int delete);

}
