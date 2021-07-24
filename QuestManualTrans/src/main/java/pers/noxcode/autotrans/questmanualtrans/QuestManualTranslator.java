package pers.noxcode.autotrans.questmanualtrans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * 此类用于实现我的世界任务手册的翻译功能
 *
 * @author van人之雄
 * @version 1.0
 * @date 2021.07.24
 */

public class QuestManualTranslator {

    private static String TARGET_DIR;
    private static String TARGET_FILENAME;

    static{
        Properties properties = new Properties();
        InputStream inputStream = QuestManualTranslator.class.getClassLoader().getResourceAsStream("targets.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        }
        try {
            TARGET_DIR = Optional.of(properties.getProperty("targetDir")).get();
            TARGET_FILENAME = Optional.of(properties.getProperty("targetFilename")).get();
        } catch (NullPointerException e) {
            //TODO
            e.printStackTrace();
        }
    }

    /**
     * 待翻译的文件
     */
    private final List<File> workFiles = new LinkedList<>();

    /**
     * 初始化工作空间，加载翻译文件
     * @param workspace 工作目录，整合包的根目录，注：并不一定是.minecraft文件夹，是整合包的根目录，看看versions文件夹
     */
    public QuestManualTranslator(String workspace) {

        //工作目录，整合包的根目录，注：并不一定是.minecraft文件夹，是整合包的根目录，看看versions文件夹
        loadWorkFiles(new File(workspace + TARGET_DIR));
    }

    /**
     * 递归加载翻译文件
     * @param file 加载的起始目录
     */
    private void loadWorkFiles(File file) {
        if (file.isFile()) {
            if (file.getName().endsWith(TARGET_FILENAME)) {
                workFiles.add(file);
                //test
                System.out.println(file);
            }
            return;
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File child : files) {
                loadWorkFiles(child);
            }
        }
    }
}
