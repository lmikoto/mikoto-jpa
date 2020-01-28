package io.github.lmikoto.jpa.query;

import io.github.lmikoto.jpa.support.DataQueryObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T,ID>  extends JpaRepository<T,ID>, JpaSpecificationExecutor<T> {

    List<T> findAll(DataQueryObject queryObject);
}
