package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.Checkins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckinsRepository extends JpaRepository<Checkins, Long> {
}
