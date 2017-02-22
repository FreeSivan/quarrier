package sivan.yue.quarrier.build;

import sivan.yue.quarrier.build.creator.CreatorBunch;
import sivan.yue.quarrier.build.merger.MergerBunch;
import sivan.yue.quarrier.build.writer.WriterBunch;
import sivan.yue.quarrier.build.writer.WriterTask;
import sivan.yue.quarrier.common.data.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * 一层creator， 100个文档
 * 一层merger 10个
 * 一层writer 2个  一共建库2000个指纹
 *
 * 从一个指定目录下面不断的读二进制文件
 * 为每个文件创建一个
 * Created by xiwen.yxw on 2017/2/21.
 */
public class BuildTest {
    private CreatorBunch creatorBunch;
    private MergerBunch mergerBunch;
    private WriterBunch writerBunch;

    public BuildTest() {
    }

    public CreatorBunch getCreatorBunch() {
        return creatorBunch;
    }

    public void setCreatorBunch(CreatorBunch creatorBunch) {
        this.creatorBunch = creatorBunch;
    }

    public MergerBunch getMergerBunch() {
        return mergerBunch;
    }

    public void setMergerBunch(MergerBunch mergerBunch) {
        this.mergerBunch = mergerBunch;
    }

    public WriterBunch getWriterBunch() {
        return writerBunch;
    }

    public void setWriterBunch(WriterBunch writerBunch) {
        this.writerBunch = writerBunch;
    }

    public void build(String path) {
        File dir = new File(path);
        if (!dir.isDirectory()) {
            return;
        }
        int index = 0;
        for (File file : dir.listFiles()) {
            try {
                RandomAccessFile rdRFile = new RandomAccessFile(file, "rw");
                Document doc = new Document();
                doc.docId = index++;
                doc.content = new byte[(int) rdRFile.length()];
                rdRFile.read(doc.content);
                String fileName = file.getName();
                String[] strArr = fileName.split("\\.");
                if (strArr.length != 2) {
                    continue;
                }
                doc.orgId = Integer.parseInt(strArr[0]);
                creatorBunch.addItem(doc);
                rdRFile.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //creatorBunch.flush();
    }

    public static void main(String[] args) {
        CreatorBunch creatorBunch = new CreatorBunch(100);
        MergerBunch mergerBunch = new MergerBunch(5);
        WriterBunch writerBunch = new WriterBunch(1);
        creatorBunch.setMergerBunch(mergerBunch);
        mergerBunch.setBunchSegment(writerBunch);

        WriterTask.indexDir = "D:\\work\\data\\afp\\index\\";
        BuildTest buildTest = new BuildTest();
        buildTest.setCreatorBunch(creatorBunch);
        buildTest.setMergerBunch(mergerBunch);
        buildTest.setWriterBunch(writerBunch);
        buildTest.build("D:\\work\\data\\afp\\afp");
    }
}
