package com.gp.webdriverapi.system.service.file;

import com.gp.webdriverapi.common.utils.DateUtils;
import com.gp.webdriverapi.system.service.file.pojo.WdFile;
import com.gp.webdriverapi.system.service.file.pojo.WdUploadFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;

@Component
public class FileTransmitter {

    private static final int CHUNK_SIZE = 100 * 1024 * 1024;

    @PostConstruct
    public void init() {
        // 判断文件夹是否存在，不存在则创建文件夹
        File file = new File("./upload");
        if (!file.exists() || !file.isDirectory()) {
            boolean mkdir = file.mkdir();
            if (!mkdir) {
                throw new RuntimeException("文件夹创建失败，请检查是否给予权限！");
            }
        }
    }

    private String pathGenerator(String base, Date date) {
        return base + DateUtils.getDate(date);
    }


    public void upload(String base, WdUploadFile wdUploadFile) {
        String path = pathGenerator(base, wdUploadFile.getLastUpdateTime());
        // 检查路径文件夹是否存在
        File folder = new File(path);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdir();
        }

        List<MultipartFile> files = wdUploadFile.getFiles();
        String fileName = wdUploadFile.getNewName();
        String suffix = wdUploadFile.getFileSuffix();
        int current = wdUploadFile.getCurrent();
        int total = wdUploadFile.getTotal();
        String realPath;

        // 根据上传的形式构建文件的全路径
        StringBuilder builder = new StringBuilder();
        builder.append(path)
                .append("/")
                .append(fileName)
                .append(suffix);
        if (total > 1) {
            builder.append("-")
                    .append(current)
                    .append(".chunk");
        }
        realPath = builder.toString();

        // 将文件存储到本地
        files.forEach(file -> {
            if (!file.isEmpty()) {
                outputFile(file, new File(realPath));
            }
        });
    }

    public String merge(WdUploadFile wdUploadFile) {
        String path = "./upload/" + DateUtils.getDate(wdUploadFile.getLastUpdateTime());
        File folder = new File(path);
        String[] fileChunks = folder.list(
                (dir, name) -> name.matches(wdUploadFile.getId() + ".\\w+-\\d+.chunk"));

        String mergeName = path +
                "/" +
                wdUploadFile.getNewName() +
                wdUploadFile.getFileSuffix();
        // 合并文件碎片
        if (fileChunks != null && wdUploadFile.getTotal() > 1) {
            FileChannel out = null;
            try {
                out = new FileOutputStream(mergeName).getChannel();
                for (String fileChunk : fileChunks) {
                    String fileChunkPath = path + "/" + fileChunk;
                    FileChannel in = new FileInputStream(fileChunkPath).getChannel();
                    ByteBuffer buffer = ByteBuffer.allocate(CHUNK_SIZE);
                    while (in.read(buffer) != -1) {
                        buffer.flip();
                        out.write(buffer);
                        buffer.clear();
                    }
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException ignore) {
                }
            }
            // 上传成功清理
            for (String fileChunk : fileChunks) {
                File f = new File(path + "/" + fileChunk);
                f.delete();
            }
        }
        return mergeName;
    }

    public void download(HttpServletResponse response, WdFile wdFile) {
        // 从文件信息中获取路径
        String file = wdFile.getOriginalName();
        // 输出文件
        try {
            String fileName = URLEncoder.encode(file, "UTF-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + fileName);
            InputStream in = new FileInputStream(wdFile.getRealPath());
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[in.available()];
            in.read(buff);
            out.write(buff);
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void deleteFile(WdFile wdFile) {
        File file = new File(wdFile.getRealPath());
        if(file.exists()) {
            int i = 1;
            while(!file.delete()) {
                if(i > 3) {
                    break;
                }
                i++;
            }
        }
    }

    private void outputFile(MultipartFile file, File newFile) {
        BufferedOutputStream stream = null;
        try {
            byte[] bytes = file.getBytes();
            stream = new BufferedOutputStream(new FileOutputStream(newFile));
            stream.write(bytes);
        } catch (IOException e) {
            stream = null;
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
