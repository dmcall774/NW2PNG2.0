package born2kill.nw2png;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GraalLevel {
    private File file;
    private ArrayList<int[][]> layers = new ArrayList();
    
    public GraalLevel(File file) {
        this.file = file;
    }
    
    public void parse() throws IOException {
        FileReader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);
        
        String line = null;
        int rowIndex = 0;
        
        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith("BOARD ")) {
                String[] lineParts = line.split(" ");
                String row = lineParts[5];
                int layer = Integer.valueOf(lineParts[4]);
                
                if (layers.size() < layer + 1) {
                    layers.add(new int[64][64]);
                }
                
                for (int i = 0; i < 64; i ++) {
                    String tile = row.substring(i * 2, (i * 2) + 2);
                    
                    int tileID = GraalFormatHelper.getTileNumber(tile);
                    layers.get(layer)[rowIndex][i] = tileID;
                }
                
                rowIndex ++;
            }
        }
    }
    
    public BufferedImage generateImage(BufferedImage tileset) {
        BufferedImage image = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_RGB);
        Graphics2D imageGraphics = image.createGraphics();
        
        for (int[][] rows : layers) {
            int y = 0;
            
            for (int[] columns : rows) {
                int x = 0;

                for (int tile : columns) {
                    int[] tileLocation = GraalFormatHelper.getTileLocation(tile);
                    imageGraphics.drawImage(tileset, x * 16, y * 16, (x * 16) + 16, (y * 16) + 16, tileLocation[0], tileLocation[1], tileLocation[0] + 16, tileLocation[1] + 16, null);

                    x ++;
                }

                y ++;
            }

        }
        
        return image;
    }
}
