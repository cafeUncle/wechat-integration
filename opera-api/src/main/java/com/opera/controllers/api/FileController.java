package com.opera.controllers.api;

import com.opera.cron.AuthTokenFetcher;
import com.opera.common.utils.WechatUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.UUID;

@RestController
public class FileController {

    @Autowired
    AuthTokenFetcher authTokenFetcher;

    /**
     * 传统上传方式
     * <p>
     * 需要注意的是，如果配置了CommonsMultipartResolver，传统上传方式就走不通了，因为提前过滤掉了
     */
    @PostMapping("/uploadFile")
    public String uploadFile(HttpServletRequest request) throws Exception {
        System.out.println("文件上传...");
        //上传的位置
        String path = request.getSession().getServletContext().getRealPath("/uploads/");
        //判断该路径是否存在
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        //解析request对象
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> items = upload.parseRequest(request);
        //遍历
        for (FileItem item : items) {
            if (item.isFormField()) {
                //普通输入项 ,得到input中的name属性的值
                String name = item.getFieldName();
                //得到输入项中的值
                String value = item.getString("UTF-8");
                System.out.println("name=" + name + "  value=" + value);
            } else {
                //上传文件项
                String filename = item.getName();
                String uuid = UUID.randomUUID().toString().replace("-", "");
                String saveName = uuid + "_" + filename.substring(filename.lastIndexOf(File.separator) + 1);
                item.write(new File(path, saveName));
                //删除临时文件
                item.delete();

                // InputStream is=item.getInputStream();
                // FileOutputStream fos=new FileOutputStream("D:\\wps\\"+fileName);
                // byte[] buff=new byte[1024];
                // int len=0;
                // while((len=is.read(buff))>0){
                //     fos.write(buff);
                // }
            }
        }

        return "success";
    }

    /**
     * SpringMVC上传方式  传到微信素材
     */
    @RequestMapping("/uploadFile2")
    public String uploadFile2(HttpServletRequest request, MultipartFile upload) throws Exception {
        System.out.println("springmvc文件上传...");
        //上传的位置
        String path = request.getSession().getServletContext().getRealPath("/uploads/");

        String originalFilename = upload.getOriginalFilename();
        String fileName = originalFilename.substring(originalFilename.lastIndexOf(File.separator) + 1);

        File file = new File(path, fileName);
        FileUtils.copyInputStreamToFile(upload.getInputStream(), file);

//        String result = WechatUtil
//            .createMedia("image", file);

        String result = WechatUtil
                .createImage(file);

        if (file.exists()) {
            file.delete();
        }

        return result;
    }

    /**
     * SpringMVC上传方式  存到本地
     */
    @RequestMapping("/uploadFile3")
    public String uploadFile3(HttpServletRequest request, MultipartFile upload) throws Exception {
        //上传的位置
        String path = request.getSession().getServletContext().getRealPath("/uploads/");
        System.out.println("path:" + path);
        //判断该路径是否存在
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        //上传文件项
        String filename = upload.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String saveName = uuid + "_" + filename.substring(filename.lastIndexOf(File.separator) + 1);

        File file = new File(path, saveName);
        upload.transferTo(file);

        File file2 = new File(path, saveName);
        FileUtils.copyInputStreamToFile(upload.getInputStream(), file2);

        return "success";
    }
}
