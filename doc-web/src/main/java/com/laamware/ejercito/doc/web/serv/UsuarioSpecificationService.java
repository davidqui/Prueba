package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.UsuarioSpecificationRepository;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 *
 * @author jgarcia
 */
@Service
public class UsuarioSpecificationService {

    @Autowired
    private UsuarioSpecificationRepository repository;

    public Page<Usuario> buscar(String criteria) {
        Specification<Usuario> specification = new Specification<Usuario>() {
            @Override
            public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                return cb.conjunction();
            }
        };

        PageRequest page = new PageRequest(0, 10);    
        return repository.findAll(specification, page);
    }
}
