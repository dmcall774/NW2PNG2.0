/*
NW2PNG by Alex (born2kill)
http://forums.graalonline.com/forums/showthread.php?t=134259601

Modifications by Chris Vimes and Dusty
 */
package born2kill.nw2png;

import born2kill.nw2png.exception.NoFilenameCacheFoundException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;


public class NW2PNGHelper implements Runnable {
    private Listener listener;
    
    // options
    private double scale = 1;
    private File sourceFile;
    private File outputFile;
    private File tilesetFile;
    private String graalDir; // should end with File.separator
    private boolean filterOutput = true;
    private boolean splitImages = false;
    private boolean renderNPCs = true;
    private boolean renderChars = true;
    
    // temporary data used for a single render
    private BufferedImage tileset;
    private HashMap<String, String> fileMap;

    public NW2PNGHelper(Listener listener) {
        this.listener = listener;
    }

    public void generate() {
        Thread runner = new Thread(this);
        runner.start();
    }
    
    // this method prepares global variables (tileset, file map) and checks settings, then
    // hands off the rendering to either renderGMAP or renderLevel
    public void run() {
        try {
            // before we start generating, verify that all settings are valid
            if (settingsValid()) {
                listener.sendMessage("Specified options were valid, now reading FILENAMECACHE.txt.");
                
                // read FILENAMECACHE.txt or scan the Graal directory if necessary
                try {
                    fileMap = GraalFormatHelper.parseFilenameCache(graalDir);
                    listener.sendMessage("Loaded " + fileMap.size() + " entries from FILENAMECACHE.txt.");
                } catch (NoFilenameCacheFoundException ex) {
                    listener.sendMessage("Unable to find a FILENAMECACHE.txt file in your Graal directory. Scanning your Graal directory... (this could take a while)");
                    fileMap = GraalFormatHelper.scanDirectory(graalDir);
                    listener.sendMessage("Scanned " + fileMap.size() + " files in the Graal directory.");
                }
                
                // load the tileset into a BufferedImage
                tileset = ImageIO.read(tilesetFile);
                
                // the rest is now handled differently, depending on if it's a level or a map
                if (sourceFile.getName().endsWith(".gmap")) {
                    // GMAP
                    renderGMAP();
                } else {
                    // level
                    renderLevel();
                }
            } else {
                // settings weren't valid
                listener.sendMessage("Unable to continue, please fix your specified options.");
            }
        } catch (Exception ex) { // if an error is ever expected, it should be caught earlier than this
            ex.printStackTrace();
        }
    }
    
    private void renderGMAP() throws Exception {
        throw new Exception("Not yet supported.");
    }
    
    private void renderLevel() throws IOException {
        GraalLevel level = new GraalLevel(sourceFile);
        level.parse();
        BufferedImage image = level.generateImage(tileset);
        ImageIO.write(image, "png", outputFile);
    }
    
    private boolean settingsValid() {
        // only verifying options with no default; if someone wants to put in an invalid scale,
        // for example, no sense in protecting them from whatever happens
        if (sourceFile == null || ! sourceFile.exists()) {
            listener.sendMessage("You need to specify a source file.");
            return false;
        }
        
        if (outputFile == null) {
            listener.sendMessage("You need to specify an output file.");
            return false;
        }
        
        if (tilesetFile == null || ! tilesetFile.exists()) {
            listener.sendMessage("You need to specify a tileset file.");
            return false;
        }
        
        if (graalDir == null) {
            listener.sendMessage("You need to specify your Graal directory.");
            return false;
        }
        
        
        return true;
    }

    // getters and setters
    
    public void setFilterOutput(boolean filterOutput) {
        this.filterOutput = filterOutput;
    }
    
    public String getGraalDir() {
        return graalDir;
    }
    
    public void setGraalDir(String graalDir) {
        this.graalDir = graalDir;
    }
    
    public File getOutputFile() {
        return outputFile;
    }
    
    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public void setRenderChars(boolean renderChars) {
        this.renderChars = renderChars;
    }

    public void setRenderNPCs(boolean renderNPCs) {
        this.renderNPCs = renderNPCs;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
    
    public File getSourceFile() {
        return sourceFile;
    }
    
    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public void setSplitImages(boolean splitImages) {
        this.splitImages = splitImages;
    }
    
    public File getTilesetFile() {
        return tilesetFile;
    }
    
    public void setTilesetFile(File tilesetFile) {
        this.tilesetFile = tilesetFile;
    }
}
