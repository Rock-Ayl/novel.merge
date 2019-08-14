package cn.ayl;

import cn.ayl.util.NumberUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * create by Rock-Ayl 2019-8-13
 * 本项目的目的是为了解决个人小说单章合并成一个完整的精校TXT而准备
 * 主程序
 */
public class start {

    //作者
    public final static String Author = "黄枫谷小辈";
    //内容简介
    public final static String ExtraInfo = "内容简介";
    //txt文件编码
    public final static String ENCODING = "UTF-8";
    //小说目录
    public final static String DIRECTORY = "/work/My-Books/仙路浮萍/少年卷/";
    //用来判断小说章节的左右字符  eg:   第一章·序幕
    public final static String LeftString = "第";
    public final static String RightString = "章·";
    //用来存放小说章节(Key)和路径(value)的信息 eg:    <1,"/work/My-Books/仙路浮萍/少年卷/第一章·序幕.txt">
    public static HashMap<Integer, String> novelInfo = new HashMap<>();

    public static void main(String argv[]) {
        //获取目录下所有小说单章的路径
        getFile(DIRECTORY);
        System.out.println(novelInfo);
    }

    /**
     * 获取目录下的所有小说并组装成Map
     *
     * @param path
     */
    private static void getFile(String path) {
        File file = new File(path);
        File[] array = file.listFiles();
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                //获取单章的名字
                String chapterName = array[i].getName();
                //获取单章的路径
                String chapterUrl = array[i].toString();
                //简单的判断是否为单章名 (eg:判断是否存在 "章·" 字段)
                if (chapterName.indexOf(RightString) != -1) {
                    //切出大写数字
                    chapterName = chapterName.substring(chapterName.indexOf(LeftString), chapterName.indexOf(RightString)).substring(LeftString.length());
                    //转化为阿拉伯数字并成为key
                    novelInfo.put(NumberUtil.getNumberByChina(chapterName), chapterUrl);
                }
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
