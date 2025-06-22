package com.lucky.infrastructure.repository.tripartite;

import cn.hutool.core.util.IdUtil;
import com.lucky.domain.repository.tripartite.HuaWeiOssRepository;
import com.lucky.infrastructure.repository.config.HuWeiObsConfig;
import com.obs.services.model.PutObjectRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.obs.services.ObsClient;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: 榴莲豆包
 * @Date: 2022年7月2日15:19:49
 * @Description: java实现华为云文件上传（COSClient）
 * @REGIONID 区域
 * @KEY 上传上云之后的名字
 */
@Component
public class HuaWeiOssRepositoryImpl implements HuaWeiOssRepository {
    private final HuWeiObsConfig huWeiObsConfig;

    public HuaWeiOssRepositoryImpl(HuWeiObsConfig huWeiObsConfig) {
        this.huWeiObsConfig = huWeiObsConfig;
    }

    // 创建 TransferManager 实例，这个实例用来后续调用高级接口
    public ObsClient createTransferManager() {
        // 创建一个 COSClient 实例，这是访问 COS 服务的基础实例。
        // 详细代码参见本页: 简单操作 -> 创建 COSClient
        ObsClient obsClient = new ObsClient(huWeiObsConfig.getAk(), huWeiObsConfig.getSk(), huWeiObsConfig.getEndpoint());
        return obsClient;
    }

    static void shutdownTransferManager(ObsClient obsClient) {
        try {
            obsClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传文件---imgFile类型
     *
     * @return
     */
    @Override
    public String upload(MultipartFile imgFile) {
        File file1 = multipartFile2File(imgFile);
        return this.uploadFile3(file1);
    }


    /**
     * 上传文件---直接传file
     *
     * @return
     */
    public String uploadFile3(File file) {

        ObsClient obsClient = this.createTransferManager();

        try {

            PutObjectRequest request = new PutObjectRequest();
            request.setBucketName("lucky-wxcd");
            String name = file.getName();
            String prefix = name.substring(name.lastIndexOf(".") + 1);
            String filename = IdUtil.fastUUID() + "." + prefix;
            request.setObjectKey(filename);
            request.setFile(file);
            obsClient.putObject(request);

            return huWeiObsConfig.getBASEURL1() + filename;

        } catch (Exception e) {
            System.out.println("putObject failed");
            // 其他异常信息打印
            e.printStackTrace();
        } finally {
            shutdownTransferManager(obsClient);
        }
        return "";
    }


    /**
     * 删除文件
     *
     * @param path
     */
    public void deleteFile(String path) {

        ObsClient obsClient = createTransferManager();
        try {
            File file = new File(path);
            // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
            // 对象键(Key)是对象在存储桶中的唯一标识。详情请参见 [对象键](https://cloud.tencent.com/document/product/436/13324)
            String key = file.getName();
            obsClient.deleteObject(huWeiObsConfig.getBucketname(), "uploadfile/" + key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shutdownTransferManager(obsClient);
        }

    }


    public static void dfsdelete(String path) {
        File file = new File(path);
        if (file.isFile()) {//如果此file对象是文件的话，直接删除
            file.delete();
            return;
        }
        //当 file是文件夹的话，先得到文件夹下对应文件的string数组 ，递归调用本身，实现深度优先删除
        String[] list = file.list();
        for (int i = 0; i < list.length; i++) {
            dfsdelete(path + File.separator + list[i]);

        }//当把文件夹内所有文件删完后，此文件夹已然是一个空文件夹，可以使用delete()直接删除
        file.delete();
        return;
    }


    /**
     * 得到文件流
     *
     * @param url 网络图片URL地址
     * @return
     */
    public static byte[] getFileStream(String url) {
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * file转byte
     */
    public static byte[] file2byte(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * byte 转file
     */
    public static File byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * multipartFile转File
     **/
    public static File multipartFile2File(MultipartFile multipartFile) {
        String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));

        File file = null;
        if (multipartFile != null) {
            try {
                file = File.createTempFile(suffix, suffix);
                multipartFile.transferTo(file);
                System.gc();
                file.deleteOnExit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }


}