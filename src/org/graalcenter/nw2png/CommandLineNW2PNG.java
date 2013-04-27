package org.graalcenter.nw2png;

import born2kill.nw2png.Listener;
import born2kill.nw2png.NW2PNGHelper;
import java.io.File;
import java.io.IOException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;


public class CommandLineNW2PNG implements Listener {
    private NW2PNGHelper helper;
    
    public CommandLineNW2PNG(String graalDir, File tileset, File input, File output, boolean renderNPCs, boolean filter, boolean split, boolean renderChars, double scale) {
        helper = new NW2PNGHelper(this);
        
        // set options
        helper.setGraalDir(graalDir);
        helper.setTilesetFile(tileset);
        helper.setSourceFile(input);
        helper.setOutputFile(output);
        helper.setRenderNPCs(renderNPCs);
        helper.setFilterOutput(filter);
        helper.setSplitImages(split);
        helper.setRenderChars(renderChars);
        helper.setScale(scale);
        
        // run
        helper.generate();
    }
    
    public static void main(String[] args) throws IOException {
        OptionParser parser = new OptionParser() {
            {
                // required arguments
                accepts("graaldir", "Path to the Graal directory (req)").withRequiredArg().ofType(String.class).describedAs("path to graal dir");
                accepts("tileset", "Path to the tileset file (req)").withRequiredArg().ofType(String.class).describedAs("path to tileset");
                accepts("input", "Path to the input file (GMAP or NW) (req)").withRequiredArg().ofType(String.class).describedAs("path to level");
                accepts("output", "Path to the generated PNG (req)").withRequiredArg().ofType(String.class).describedAs("path to PNG");
                
                // options
                accepts("skip-npcs", "Don't render NPCs");
                accepts("skip-filter", "Don't filter output");
                accepts("skip-chars", "Don't render characters");
                accepts("split", "Split GMAP into levels");
                
                // optional arguments
                accepts("scale", "Scale as a double (e.g. 1, 0.5, 0.25)").withRequiredArg().ofType(Double.class).defaultsTo(1.0).describedAs("scale of output");
            }
        };
        
        if (args.length <= 0 || args[0].equals("help")) {
            parser.printHelpOn(System.out);
        } else {
            OptionSet options = parser.parse(args);
            
            String[] requiredArguments = {"graaldir", "tileset", "input", "output"};
            
            for (String requiredArgument : requiredArguments) {
                if (! options.hasArgument(requiredArgument)) {
                    System.err.println("Missing required argument: " + requiredArgument);
                    System.exit(0);
                }
            }
            
            String graalDir = (String) options.valueOf("graaldir");
            File tileset = new File((String) options.valueOf("tileset"));
            File input = new File((String) options.valueOf("input"));
            File output = new File((String) options.valueOf("output"));
            
            boolean renderNPCs = ! options.has("skip-npcs");
            boolean filter = ! options.has("skip-filter");
            boolean renderChars = ! options.has("skip-chars");
            boolean split = options.has("split");
            
            double scale = Double.valueOf(options.valueOf("scale").toString());
            
            System.out.println("Specified Options:");
            
            Object[][] printedOptions = {
                {"Graal Dir", graalDir},
                {"Tileset", tileset},
                {"Input", input},
                {"Output", output},
                {"Render NPCs", renderNPCs},
                {"Filter", filter},
                {"Split", split},
                {"Render Characters", renderChars},
                {"Scale", scale}
            };
            
            for (Object[] printedOption : printedOptions) {
                System.out.println("\t" + printedOption[0] + ": " + printedOption[1]);
            }
            
            new CommandLineNW2PNG(graalDir, tileset, input, output, renderNPCs, filter, split, renderChars, scale);
        }
        
    }

    public void sendMessage(String message) {
        System.out.println(message);
    }

    public void doneGenerating() {
        System.out.println("Finished generating.");
    }
}
