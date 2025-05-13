package com.jbl.t24.rest.api.service.base;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jbl.t24.rest.api.audit.Auditable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
@Transactional(readOnly = true)
@NoArgsConstructor
@Setter
@Getter
public class BaseService<T extends Auditable> {


    private JpaRepository<T, Integer> repository;

    public BaseService (JpaRepository<T, Integer> repo) {
        this.repository = repo;
    }
    public List<T> findAll() {
        return repository.findAll();
    }

    public Optional <T> findOne(Integer id) {
        return Optional.of(repository.getById(id));
    }



    @Transactional(readOnly = false)
    public T save(T entity) {
        return repository.save(entity);
    }

    @Transactional(readOnly = false)
    public void delete(T entity) {
        repository.delete(entity);
    }

}
