package utils;

import bean.UploadFile;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    /**
     * 存储文件工具方法
     * @param uploadFile 上传你的文件类
     * @param servletContext 上下文
     * @param filePath 相对路径 如 "upload\\avatar\\"
     * @param fileTypeName 文件类型名 如 "avatar"
     * @return
     */
    public static String saveFile(UploadFile uploadFile, ServletContext servletContext, String filePath, String fileTypeName) {
        boolean flag = false;
        byte[] bytes = null;
        try {
            bytes = new BASE64Decoder().decodeBuffer(uploadFile.getFileStr());
            flag = true;
        } catch (IOException e) {
            flag = false;
            e.printStackTrace();
        }
        if (!flag) {
            return null;
        }
        String fileFormat = uploadFile.getFileName().substring(uploadFile.getFileName().indexOf("."));
        File path = new File(servletContext.getRealPath("/") + filePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        String fileName = servletContext.getRealPath("/") + filePath + uploadFile.getAccount() + "_" + fileTypeName + fileFormat;
        File imgFile = new File(fileName);
        flag = true;
        if (!imgFile.exists()) {
            try {
                imgFile.createNewFile();
            } catch (IOException e) {
                flag = false;
                e.printStackTrace();
            }
        }
        if (!flag) {
            return null;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(bytes);
            return  filePath + uploadFile.getAccount() + "_" + fileTypeName + fileFormat;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
