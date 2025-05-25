package com.br.projetoyaskara.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Checkins extends JpaRepository<Checkins, Long> {
}
