package pl.lodz.p.sfbgenerator.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {
    public static boolean checkDirectory(File directory) {
        if (directory == null) {
            return false;
        }
        File[] filesInDir = directory.listFiles();
        if(filesInDir.length < 1) {
            return false;
        }
        try {
            for(File file : filesInDir) {
                if(file.isDirectory()) {
                    return false;
                }
                Path path = file.toPath();
                String mimeType = Files.probeContentType(path);
                if(!mimeType.matches("image\\/.*")) {
                    return false;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static int getFilesCount(File directory) {
        return directory.list().length;
    }
}
