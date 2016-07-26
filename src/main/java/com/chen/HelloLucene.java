package com.chen;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Author ： chen
 * Date ： 16/7/27
 * Time : 上午12:57
 */
public class HelloLucene {

    private static final String FILE_DIR = "/opt/lucene/";


    public void index(){
        //1:创建Directory

//        Directory directory = new RAMDirectory();//1.索引建立在内存中2.建立在硬盘中，写入到硬盘 3.5版本中可以直接new File 但是在5.0版本中只能用JavaNIO的知识中的Path
        Directory directory = null;
        try {
            directory = FSDirectory.open(Paths.get(FILE_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }


        //2:创建IndexWriter

        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());//standardAnalyzer标准分词器
        IndexWriter writer = null;

        //3：创建document对象

        try {
            writer = new IndexWriter(directory,config);


            //4：为document添加field
            Document document = new Document();

            //5: 通过indexwriter添加文档到索引中

            document.add(new StringField("id","I love you,so i married you",Field.Store.YES));
            document.add(new TextField("content", "this is my content,lucene apache", Field.Store.YES));


            writer.addDocument(document);//writer.addDocuments(); 在5.0的基础中可以一次添加一个List的Documents







        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer!=null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }



    public void searcher(){
        //1.创建Directory

        Directory directory = null;
        try {
            directory = FSDirectory.open(Paths.get(FILE_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }


        //2.创建IndexReader
        IndexReader reader = null;
        try {
             reader = DirectoryReader.open(directory); //Lucene 5.0 创建IndexReader是DirectoryReader直接open就可以 ，3.5是IndexReader本身open一个directory
        } catch (IOException e) {
            e.printStackTrace();
        }

        //3.根据indexreader创建IndexSearcher

        IndexSearcher searcher = new IndexSearcher(reader);

        //4.创建搜索的Query
        //创建parser来确定要搜索的文件内容
        QueryParser parser = new QueryParser("content",new StandardAnalyzer());

        //创建Query
        Query query = null;
        try {
            query = parser.parse("lucene");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //5.根据searcher搜索并且返回TopDocs
        TopDocs tds = null;
        try {
            tds = searcher.search(query,10);
        } catch (IOException e) {
            e.printStackTrace();
        }






        //6.根据TopDocs获取ScoreDoc对象

        ScoreDoc[] sds = tds.scoreDocs;

        //7.根据searcher 和 ScoreDoc对象获取具体的Document对象

        for (ScoreDoc sd : sds ){
            Document document = null;
            try {
                document = searcher.doc(sd.doc);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //8.根据Document对象获取需要的值

            String id = document.get("id");
            String content = document.get("content");

            System.err.print("id = "+id+ " ,content = " + content);

        }



        //关闭reader
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
