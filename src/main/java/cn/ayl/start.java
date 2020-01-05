package cn.ayl;

import cn.ayl.util.NumberUtil;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * create by Rock-Ayl 2019-8-13
 * 本项目的目的是为了解决个人小说单章合并成一个完整的精校TXT而准备
 * 主程序
 */
public class start {

    //小说名
    public final static String novel = "仙路浮萍";
    //小说目录
    public final static String Directory = "/Users/ayl/work/My-Books/仙路浮萍/";
    //小说分卷
    public final static String[] volumeArr = new String[]{"少年卷", "练气卷"};
    //要生成的全章节文件名
    public final static String NewNovel = "/Users/ayl/书/" + novel + ".txt";

    //小说总字数
    public static int allAmount = 0;
    //作者
    public final static String Author = "黄枫谷小辈";
    //内容简介
    public final static String ExtraInfo = "内容简介.............";
    //用来判断小说章节的左右字符  eg:   第一章·序幕
    public final static String LeftString = "第";
    public final static String RightString = "章·";

    //是否自动添加章节名(如果每一章小说第一行已有章名的话就选 false ,如果没有就选 true)
    public final static boolean AddChapterName = false;
    //章节名是否需要从大写转阿拉伯(eg: 第1章 选 false 第一章 选 true)
    public final static boolean SwitchNumber = true;

    //txt文件编码
    public final static String Encoding = "UTF-8";

    //取章名的调优值
    public final static int compatibleName = 2;
    //用来存放小说章节(Key)和路径(value)的信息 eg:    <1,"/work/My-Books/仙路浮萍/少年卷/第一章·序幕.txt">
    public static HashMap<Integer, String> novelInfo = new HashMap<>();
    public static HashMap<Integer, String> novelName = new HashMap<>();

    public static void main(String argv[]) throws IOException {

        //准备要生成的文件
        File newNovel = new File(NewNovel);
        //创建
        newNovel.createNewFile();
        //文件写入
        FileWriter fileWriter = new FileWriter(newNovel);
        //缓存
        BufferedWriter writer = new BufferedWriter(fileWriter);
        //添加前言(作者+简介)
        writer.append("书名:");
        writer.append("  " + novel);
        writer.write("\r\n");
        writer.write("\r\n");
        writer.append("作者:");
        writer.append("  " + Author);
        writer.write("\r\n");
        writer.write("\r\n");
        writer.append("内容简介:");
        writer.append("  " + ExtraInfo);
        writer.write("\r\n");
        writer.write("\r\n");
        writer.write("\r\n");

        //根据分卷组装成对应路径
        for (String volume : volumeArr) {
            //分卷路径
            String path = FilenameUtils.normalize(Directory + volume + "/");
            //获取该分卷目录下所有小说单章的路径
            getFile(path);
        }


        //遍历info中的key(不需要排序,组装的过程中已经自动排序了)
        novelInfo.keySet().forEach(key -> {

            //获取对应文件的文本
            List<String> contents = readTxtFile(novelInfo.get(key));

            //获取当前文本数量
            int thisAmount = getChineseCharacters(contents);
            //输出本章数量
            System.out.println("第" + key + "章字数:" + thisAmount);
            //累加
            allAmount = allAmount + thisAmount;

            //组装至文件
            for (int i = 0; i < contents.size(); i++) {
                //添加标题
                if (i == 0) {
                    try {
                        writer.append(LeftString + key + RightString + novelName.get(key));
                        writer.write("\r\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        //写入
                        writer.append(contents.get(i));
                        //每章的每一行换行
                        writer.write("\r\n");
                    } catch (IOException e) {
                        System.out.println("写入失败.");
                    }
                }
            }
            //每一章额外换几行
            try {
                writer.write("\r\n");
                writer.write("\r\n");
                writer.write("\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //缓存写入文件
        writer.flush();
        //输出总文字
        System.out.println("总汉字数:" + allAmount);
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

                    int key;
                    int keyName;
                    try {
                        //判断是否为数字
                        key = Integer.parseInt(chapterName);
                        keyName = Integer.parseInt(chapterName);
                    } catch (Exception e) {
                        //转化为阿拉伯数字
                        key = NumberUtil.getNumberByChina(chapterName);
                        keyName = NumberUtil.getNumberByChina(chapterName);
                    }

                    //同时组装完整路径和章节名
                    novelInfo.put(key, chapterUrl);

                    //获取书名并组装
                    String fileName = FilenameUtils.getBaseName(array[i].getName());

                    //切出书名
                    String name = fileName.substring(fileName.indexOf(RightString) + compatibleName);

                    //组装
                    novelName.put(keyName, name);
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
                //获取文件字节并解码成字符
                InputStreamReader stream = new InputStreamReader(new FileInputStream(file), Encoding);
                //缓冲
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

    /**
     * 获得一组文件汉字数量
     *
     * @param contents
     * @return
     */
    public static int getChineseCharacters(List<String> contents) {
        int amount = 0;
        if (contents != null && contents.size() > 0) {
            for (String content : contents) {
                for (int i = 0; i < content.length(); i++) {
                    String regex = "^[\u4E00-\u9FA5]{0,}$";
                    boolean matches = Pattern.matches(regex, content.charAt(i) + "");
                    if (matches) {
                        amount++;
                    }
                }
            }
        }
        return amount;
    }

}
