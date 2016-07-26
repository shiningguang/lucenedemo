package com.chen;

import org.junit.Test;

/**
 * Author ： chen
 * Date ： 16/7/27
 * Time : 上午1:19
 */
public class TestLucene {

    @Test
    public void testIndex(){
        HelloLucene lucene = new HelloLucene();
        lucene.index();
    }

    @Test
    public void testSearch(){
        HelloLucene lucene = new HelloLucene();
        lucene.searcher();
    }
}
