package edu.fast_track.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl {
    private final JdbcTemplate jdbcTemplate;

    public boolean isAdmin(int id){
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT COUNT(*) > 0 FROM admin WHERE id = ?", Boolean.class, id));
    }
}
