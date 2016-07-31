package com.chen.lucene.utils;


import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

/**
 * Author ： chen
 * Date ： 16/7/30
 * Time : 下午12:37
 */
public class SearcherUtil {

    private Directory directory;

    private IndexReader reader;

    public SearcherUtil(){
        directory = new RAMDirectory();

    }

    public IndexSearcher getSearcher(){
        try {
            if(reader == null){
                reader = DirectoryReader.open(directory);
            }else {
                IndexReader tr;
                //noinspection ConstantConditions
                tr = DirectoryReader.openIfChanged(DirectoryReader.open(directory));
                if(tr != null){
                    reader.close();
                    reader = tr;

                }
            }
            return new IndexSearcher(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 精确匹配
     * @param field 域
     * @param name 输入查询词
     * @param num 分页数
     */
    public void searchByTerm(String field ,String name ,int num){
        IndexSearcher searcher = getSearcher();
        Query query = new TermQuery(new Term(field,name));
        TopDocs topDocs = null;
        try {
            topDocs = searcher.search(query,num);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc s:scoreDocs){
            Document document = null;
            try {
                document = searcher.doc(s.doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            document.get("id");
        }



    }


    /**
     * 根据数值空间查询
     * @param field
     * @param start
     * @param end
     * @param num
     */
    public void searchByNumrange(String field,int start ,int end,int num){
        IndexSearcher searcher = getSearcher();
        Query query = NumericRangeQuery.newIntRange(field,start,end,true,true);
        TopDocs topDocs = null;
        try {
            topDocs = searcher.search(query,num);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc s:scoreDocs){
            Document document = null;
            try {
                document = searcher.doc(s.doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            document.get("id");
        }


    }





}
