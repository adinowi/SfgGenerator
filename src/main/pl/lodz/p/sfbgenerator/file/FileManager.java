package pl.lodz.p.sfbgenerator.file;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileManager {
    public static final String MESH_ROOM_BIN = "alicevision-bin";
    public static final String RUN_OPTION = "runall";
    public static final String CLI_EXE = "alice-vision-cli/run_alicevision.exe";
    public static final String GENERATED_OBJ = "temp/11_Texturing/texturedMesh.obj";
    public static final Set<String> objExtensions = new HashSet(Arrays.asList("obj", "fbx", "gltf"));

    public static boolean checkDirectory(File directory) {
        if (directory == null) {
            return false;
        }
        File[] filesInDir = directory.listFiles();
        if (filesInDir.length < 1) {
            return false;
        }
        try {
            for (File file : filesInDir) {
                if (file.isDirectory()) {
                    return false;
                }
                Path path = file.toPath();
                String mimeType = Files.probeContentType(path);
                if (!mimeType.matches("image\\/.*")) {
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

    public static File createTempDir() {
        File tempDir = new File("temp");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return tempDir;
    }

    public static boolean checkGeneratingObj(){
        File file = new File(GENERATED_OBJ);
        if(file.exists()){
            return true;
        }

        return false;
    }

    public static boolean checkObjValidation(File file) {
        if(objExtensions.contains(file.getName().split("[.]")[1])) {
            return true;
        } else {
            return false;
        }
    }
}

