package born2kill.nw2png;

import born2kill.nw2png.exception.NoFilenameCacheFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class GraalFormatHelper {
    public static int getTileNumber(String tileString) {
        String base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        return base64.indexOf(tileString.substring(0, 1)) * 64 + base64.indexOf(tileString.substring(1, 2));
    }

    public static int[] getTileLocation(int tileNumber) {
        int[] tile_xy = {(tileNumber % 16 + tileNumber / 512 * 16) * 16, (tileNumber / 16 % 32) * 16};
        return tile_xy;
    }
    
    // parse FILENAMECACHE.txt into a HashMap
    public static HashMap<String, String> parseFilenameCache(String graalDir) throws NoFilenameCacheFoundException, IOException {
        File filenameCache = new File(graalDir + "FILENAMECACHE.txt");
        HashMap<String, String> fileMap = new HashMap();
        
        try {
            FileReader filenameCacheReader = new FileReader(filenameCache);
            BufferedReader filenameCacheBufferedReader = new BufferedReader(filenameCacheReader);
            
            String line = null;
            
            while ((line = filenameCacheBufferedReader.readLine()) != null) {
                String path = line.substring(0, line.indexOf(","));
                String fileName = getFileNameFromPath(path);
                
                fileMap.put(fileName, graalDir + path);
            }
        } catch (FileNotFoundException ex) {
            throw new NoFilenameCacheFoundException();
        }
        
        return fileMap;
    }
    
    private static String getFileNameFromPath(String path) {
        if (path.contains(File.separator)) {
            return path.substring(path.lastIndexOf(File.separator));
        }
        
        return path;
    }
    
    // scan a Graal directory and generate a HashMap, similar to #parseFilenameCache
    public static HashMap<String, String> scanDirectory(String dir) {
        HashMap<String, String> fileMap = new HashMap();
        
        scanDirectoryLevel(fileMap, new File(dir));
        
        return fileMap;
    }
    
    private static void scanDirectoryLevel(HashMap<String, String> fileMap, File dir) {
        if (dir.exists() && dir.isDirectory()) {
            for (File child : dir.listFiles()) {
                if (child.isFile()) {
                    // add the file to the file map
                    String path = child.getAbsolutePath();
                    fileMap.put(getFileNameFromPath(path), path);
                } else if (child.isDirectory()) {
                    scanDirectoryLevel(fileMap, child); // recurse to child directory
                }
            }
        }
    }
}