package files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class StoreFile {
    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir+fileName;
    }

    public List<UploadFileInfo> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFileInfo> storeFileList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()) {
                storeFileList.add(storeFile(multipartFile));
            }
        }
        return storeFileList;
    }

    public UploadFileInfo storeFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()) {
           return null;
        }
        String uploadFilename = multipartFile.getOriginalFilename();
        String serverFileName = createServerFileName(uploadFilename);
        long size = multipartFile.getSize();
        multipartFile.transferTo(new File(getFullPath(serverFileName)));
        return new UploadFileInfo(uploadFilename,serverFileName, size);
    }

    private String createServerFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);

    }
}