package com.hackathon.wespeakright.repository;

import com.hackathon.wespeakright.entity.Person;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    
}
