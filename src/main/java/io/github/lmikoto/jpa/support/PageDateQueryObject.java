package io.github.lmikoto.jpa.support;

import lombok.Data;

@Data
public class PageDateQueryObject extends DataQueryObject {

    protected Integer page = 0;

    protected Integer size = 10;
}
