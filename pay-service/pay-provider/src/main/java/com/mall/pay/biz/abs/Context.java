package com.mall.pay.biz.abs;

import java.io.Serializable;
import java.util.SortedMap;

public class Context implements Serializable {

    private static final long serialVersionUID = -2590624518629143666L;

    public Context() {
        super();
    }

    SortedMap<String, Object> sParaTemp;

    public SortedMap<String, Object> getsParaTemp() {
        return sParaTemp;
    }

    public void setsParaTemp(SortedMap<String, Object> sParaTemp) {
        this.sParaTemp = sParaTemp;
    }
}
