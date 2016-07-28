package com.chen;

import com.chen.lucene.utils.IndexUtil;
import org.junit.Test;

/**
 * Author ： chen
 * Date ： 16/7/28
 * Time : 下午11:37
 */
public class TestIndex {

    @Test
    public void testAdd(){
        IndexUtil indexUtil = new IndexUtil();
        indexUtil.index();
    }

    @Test
    public void testQuery(){
        IndexUtil indexUtil = new IndexUtil();
        indexUtil.query();
    }


    @Test
    public void testDelete(){
        IndexUtil indexUtil = new IndexUtil();
        indexUtil.delete();
        indexUtil.query();
    }

    @Test
    public void testDeleteNum(){
        IndexUtil indexUtil = new IndexUtil();
        indexUtil.undelete();
    }

    @Test
    public void testForceMergeDelete(){
        IndexUtil indexUtil = new IndexUtil();
        indexUtil.forceDelete();
        indexUtil.query();
    }

    @Test
    public void testForceMerge(){
        IndexUtil indexUtil = new IndexUtil();
        indexUtil.forceMerge();
        indexUtil.query();
    }

    @Test
    public void testDeleteAll(){
        IndexUtil indexUtil = new IndexUtil();
        indexUtil.deleteAll();
        indexUtil.query();
    }


}
