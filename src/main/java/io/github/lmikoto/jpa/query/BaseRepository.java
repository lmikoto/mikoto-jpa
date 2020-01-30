package io.github.lmikoto.jpa.query;

import io.github.lmikoto.jpa.support.DataQueryObject;
import io.github.lmikoto.jpa.support.PageDateQueryObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T,ID>  extends JpaRepository<T,ID>, JpaSpecificationExecutor<T> {

    List<T> findAll(DataQueryObject queryObject);

    List<T> findAll(DataQueryObject queryObject, Sort sort);

    Page<T> findAll(PageDateQueryObject queryObject);

    Page<T> findAll(PageDateQueryObject queryObject, Sort sort);
}
