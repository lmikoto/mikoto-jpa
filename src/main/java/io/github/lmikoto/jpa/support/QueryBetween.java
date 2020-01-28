package io.github.lmikoto.jpa.support;

import lombok.Data;

@Data
public class QueryBetween<T extends Comparable<?>> {

    private T before;

    private T after;
}
