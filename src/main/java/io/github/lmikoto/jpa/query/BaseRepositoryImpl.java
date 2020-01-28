package io.github.lmikoto.jpa.query;

import io.github.lmikoto.jpa.support.DataQueryObject;
import io.github.lmikoto.jpa.support.QueryBetween;
import io.github.lmikoto.jpa.support.QueryField;
import io.github.lmikoto.jpa.support.QueryType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BaseRepositoryImpl <T,ID> extends SimpleJpaRepository<T,ID> implements  BaseRepository<T,ID>, JpaSpecificationExecutor<T> {


    private final EntityManager entityManager;

    private final BaseRepositoryImpl example;

    public BaseRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager em) {
        super(entityInformation, em);
        this.entityManager = em;
        example = this;
    }

    @Override
    public List<T> findAll(final DataQueryObject queryObject) {
        return this.findAll(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return example.getPredocate(root, criteriaQuery, criteriaBuilder, queryObject);
            }
        });
    }

    protected Predicate getPredocate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb,
                                     DataQueryObject dataQueryObject) {
        List<Predicate> predicates = new ArrayList<>();
        Field[] fields = dataQueryObject.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            QueryType queryType = null;
            Object value = null;
            Predicate predicate = null;
            QueryField queryFieldAnnotation = field.getAnnotation(QueryField.class);
            if(queryFieldAnnotation == null) {
                continue;
            }
            String queryFiled = StringUtils.isEmpty(queryFieldAnnotation.name()) ? field.getName() : queryFieldAnnotation.name();
            queryType = queryFieldAnnotation.type();
            try {
                value = field.get(dataQueryObject);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(Objects.isNull(value)){
                continue;
            }

            switch (queryType) {
                case EQUAL:
                    Path<Object> equal = root.get(queryFiled);
                    predicate = cb.equal(equal, value);
                    predicates.add(predicate);
                    break;
                case NOT_EQUAL:
                    Path<Object> notEqual = root.get(queryFiled);
                    predicate = cb.notEqual(notEqual, value);
                    predicates.add(predicate);
                    break;
                case IS_NULL:
                    Path<Object> isNull = root.get(queryFiled);
                    predicate = cb.isNull(isNull);
                    predicates.add(predicate);
                    break;
                case IS_NOT_NULL:
                    Path<Object> isNotNull = root.get(queryFiled);
                    predicate = cb.isNotNull(isNotNull);
                    predicates.add(predicate);
                    break;
                case BETWEEN:
                    Path<Comparable> between = root.get(queryFiled);
                    if(value instanceof QueryBetween){
                        QueryBetween queryBetween = (QueryBetween)value;
                        predicate = cb.between(between,queryBetween.getBefore(),queryBetween.getAfter());
                        predicates.add(predicate);
                    }
                    break;
                case LESS_THAN:
                    Path<Comparable> lessThan = root.get(queryFiled);
                    if(value instanceof QueryBetween){
                        QueryBetween queryBetween = (QueryBetween)value;
                        predicate = cb.lessThan(lessThan,queryBetween.getAfter());
                        predicates.add(predicate);
                    }
                    break;
                case LESS_THAN_EQUAL:
                    Path<Comparable> lessThanEqual = root.get(queryFiled);
                    if(value instanceof QueryBetween){
                        QueryBetween queryBetween = (QueryBetween)value;
                        predicate = cb.lessThanOrEqualTo(lessThanEqual,queryBetween.getAfter());
                        predicates.add(predicate);
                    }
                    break;
                case GREATER_THAN:
                    Path<Comparable> greaterThan = root.get(queryFiled);
                    if(value instanceof QueryBetween){
                        QueryBetween queryBetween = (QueryBetween)value;
                        predicate = cb.lessThan(greaterThan,queryBetween.getBefore());
                        predicates.add(predicate);
                    }
                    break;
                case GREATER_THAN_EQUAL:
                    Path<Comparable> greaterThanEqual = root.get(queryFiled);
                    if(value instanceof QueryBetween){
                        QueryBetween queryBetween = (QueryBetween)value;
                        predicate = cb.greaterThanOrEqualTo(greaterThanEqual,queryBetween.getBefore());
                        predicates.add(predicate);
                    }
                    break;
                case IN:
                    Path<Object> in = root.get(queryFiled);
                    if(value instanceof List){
                        CriteriaBuilder.In ins = cb.in(in);
                        List inList = (List) value;
                        inList.forEach(i->{
                            ins.value(i);
                        });
                        predicates.add(ins);
                    }
                case FULL_LIKE:
                    Path<String> like = root.get(queryFiled);
                    predicate = cb.like(like, "%" + value.toString() + "%");
                    predicates.add(predicate);
                    break;
                case LEFT_LIKE:
                    Path<String> leftLike = root.get(queryFiled);
                    predicate = cb.like(leftLike, "%" + value.toString());
                    predicates.add(predicate);
                    break;
                case RIGHT_LIKE:
                    Path<String> rightLike = root.get(queryFiled);
                    predicate = cb.like(rightLike, value.toString() + "%");
                    predicates.add(predicate);
                    break;
                case NOT_LIKE:
                    Path<String> notLike = root.get(queryFiled);
                    predicate = cb.notLike(notLike, value.toString());
                    predicates.add(predicate);
                    break;
                default:
                    break;
            }
        }

        if(predicates.size() == 0) {
            return cb.and();
        }
        Predicate[] t = new Predicate[predicates.size()];
        Predicate[] result = predicates.toArray(t);
        return cb.and(result);
    }

}
