package cn.ayl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * create by Rock-Ayl 2019-8-13
 * 本项目的目的是为了解决个人小说单章合并成一个完整的精校TXT而准备
 * 主程序
 */
public class start {

    //txt文件编码
    public final static String ENCODING = "UTF-8";

    //小说目录
    public final static String DIRECTORY = "/work/My-Books/仙路浮萍/少年卷/";

    public static void main(String argv[]) {
        getFile(DIRECTORY);
    }

    private static void getFile(String path) {
        File file = new File(path);
        File[] array = file.listFiles();

        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                System.out.println("******************");
                System.out.println(array[i].getName());
                System.out.println(array[i]);
            } else if (array[i].isDirectory()) {
                getFile(array[i].getPath());
            }
        }
    }

    /**
     * 读取txt
     *
     * @param filePath
     */
    public static List<String> readTxtFile(String filePath) {
        //初始化list,准备将文本组装进去
        List<String> textList = new ArrayList<>();
        try {
            //获取文件
            File file = new File(filePath);
            //如果文件存在
            if (file.exists()) {
                //获取文件流
                InputStreamReader stream = new InputStreamReader(new FileInputStream(file), ENCODING);
                BufferedReader buffer = new BufferedReader(stream);
                //读取并组装
                String lineTxt;
                while ((lineTxt = buffer.readLine()) != null) {
                    textList.add(lineTxt);
                }
                //关闭
                stream.close();
            } else {
                System.out.println("文件不存在");
            }
        } catch (Exception e) {
            System.out.println("读写出现错误");
        }
        return textList;
    }

}
