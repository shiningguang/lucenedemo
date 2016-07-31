package com.chen.lucene.utils;

import com.chen.HelloLucene;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Author ： chen
 * Date ： 16/7/27
 * Time : 下午11:41
 */
public class IndexSearCH {

    private String[] ids = {"1","2","3"};

    private String[] emails = {"aa@itat.org","bb@itat.org","dd@sina.org"};

    private String[] content = {"i love you","i know you ,you live this ten years","this is content"};

    private int[] attaches = {2,3,4};

    private String[] names = {"lili","lilei","lisi"};

    private Directory directory = null;

    private IndexReader reader = null;

    private org.apache.lucene.search.IndexSearcher searcher = null;

    private DirectoryReader directoryReader = null;


    public IndexSearCH(IndexReader reader){
        try {
            directory = FSDirectory.open(Paths.get(HelloLucene.FILE_DIR));

            this.reader = DirectoryReader.open(directory);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void deleteAll(){
        IndexWriter writer = null;
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);


        try {
            writer = new IndexWriter(directory,config);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            writer.deleteAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  void undelete(){
        IndexReader reader = null;
        try {
            reader = DirectoryReader.open(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int deleteNum = reader.numDeletedDocs();
        System.err.println("deleteNum : "+deleteNum);

    }


    public void forceMerge(){
        IndexWriter writer = null;
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);


        try {
            writer = new IndexWriter(directory,config);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            //会将索引合并为2段，这2段中被删除的数据会被清空，
            //在3.5之后不建议使用，会消耗大量开销，Lucene 会自动处理
            writer.forceMerge(2);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void forceDelete(){
        IndexWriter writer = null;
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);


        try {
            writer = new IndexWriter(directory,config);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            writer.forceMergeDeletes();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void delete(){
        IndexWriter writer = null;
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);


        try {
            writer = new IndexWriter(directory,config);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            //删除根据的是Document中具体的Field信息进行删除 只需要new Term()
            //删除的文件信息系统暂时不清楚，3.x系列老师讲的是删除是一个操作并被保存起来，其实document并没有被删除
            writer.deleteDocuments(new Term("id", "00000012"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询
     */
    public void query(){
        IndexReader reader = null;
        try {
            reader = DirectoryReader.open(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int numDocs = reader.numDocs();
        int maxDocs = reader.maxDoc();
        System.err.println("numDocs： " + numDocs);
        System.err.println("maxDosc： " + maxDocs);





    }

    /**
     * Lucene并没有更新操作
     * 先删除再添加
     */
    public void update(){
        IndexWriter writer = null;

        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        try {

            writer = new IndexWriter(directory,config);

            Document document = new Document();
            document.add(new StringField("id","01", Field.Store.YES));
            document.add(new StringField("email","cs@dd", Field.Store.YES));
            document.add(new TextField("content","i love", Field.Store.NO));
            document.add(new IntField("att",1, Field.Store.NO));
            document.add(new StringField("name","lili", Field.Store.YES));

            writer.updateDocument(new Term("id","01"),document);
            writer.commit();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }


    public void index(){
        IndexWriter writer = null;

        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        try {

            //1.创建IndexWriter通过IndexWriterConfig 进行
            writer = new IndexWriter(directory,config);
            //添加document
            for (int i=0;i<ids.length;i++){
                Document document = new Document();
                //document.add(new Field("id",ids[i], Field.Store.YES, Field.Index.NOT_ANALYZED)); 这种3.X系列的方法不建议使用。可以直接查看field的类结构
                document.add(new StringField("id",ids[i], Field.Store.YES));
                Field field = new StringField("email",emails[i], Field.Store.YES);
                field.setBoost(1.5f);
                document.add(field);
                NumericDocValuesField numericDocValuesField = new NumericDocValuesField("data",14878787324343L);
                numericDocValuesField.setBoost(1.2f);
                document.add(numericDocValuesField);
                document.add(new TextField("content",content[i], Field.Store.NO));
                document.add(new IntField("att",attaches[i], Field.Store.NO));
                document.add(new StringField("name",names[i], Field.Store.YES));
                writer.addDocument(document);
                writer.commit();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }




}
