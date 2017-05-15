package com.laamware.ejercito.doc.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laamware.ejercito.doc.web.entity.InstanciaBandeja;

public interface InstanciaBandejaRepository extends JpaRepository<InstanciaBandeja, Integer> {

	InstanciaBandeja findByInstanciaIdAndUsuarioLoginAndBandejaAndActivo(String pin, String login, String bandeja,Boolean activo);

}
