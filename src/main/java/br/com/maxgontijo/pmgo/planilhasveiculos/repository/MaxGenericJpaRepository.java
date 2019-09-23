package br.com.maxgontijo.pmgo.planilhasveiculos.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Model;

@NoRepositoryBean
public interface MaxGenericJpaRepository<MODEL extends Model<ID>, ID extends Serializable> extends JpaRepository<MODEL, ID>, JpaSpecificationExecutor<MODEL>, PagingAndSortingRepository<MODEL, ID> {
}
