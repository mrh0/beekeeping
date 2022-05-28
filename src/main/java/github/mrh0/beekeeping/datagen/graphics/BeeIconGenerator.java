package github.mrh0.beekeeping.datagen.graphics;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.bee.item.BeeItem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BeeIconGenerator {
    public static File getResource(String path) {
        try {
            return Paths.get(Beekeeping.class.getResource(path).toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        File bee = getResource("/icons/bee.png");
        File border = getResource("/icons/border.png");
        File princess = getResource("/icons/princess.png");
        File queen = getResource("/icons/queen.png");

        String path = "C:\\generated\\bees\\";

        if(args.length > 0)
            path = args[0];

        Index.species();
        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            makeIcon(bee, border, princess, queen, path, specie.getName(), BeeItem.BeeType.DRONE, specie.getColor());
            makeIcon(bee, border, princess, queen, path, specie.getName(), BeeItem.BeeType.PRINCESS, specie.getColor());
            makeIcon(bee, border, princess, queen, path, specie.getName(), BeeItem.BeeType.QUEEN, specie.getColor());
        }
    }

    public static void makeIcon(File beeFile, File borderFile, File princessFile, File queenFile, String outpath, String name, BeeItem.BeeType type, int border) throws IOException {
        BufferedImage bee = ImageIO.read(beeFile);
        BufferedImage borderBI = ImageIO.read(borderFile);
        BufferedImage princessBI = ImageIO.read(princessFile);
        BufferedImage queenBI = ImageIO.read(queenFile);
        int size = 16;

        for(int x = 0; x < size; x++) {
            for(int y = 0; y < size; y++) {
                if(borderBI.getRGB(x, y) == 0xFFFF00FF)
                    bee.setRGB(x, y, border);
                if(type == BeeItem.BeeType.PRINCESS) {
                    if(princessBI.getRGB(x, y) != 0xFF000000)
                        bee.setRGB(x, y, princessBI.getRGB(x, y));
                }
                else if(type == BeeItem.BeeType.QUEEN) {
                    if(queenBI.getRGB(x, y) != 0x00000000)
                        bee.setRGB(x, y, queenBI.getRGB(x, y));
                }
            }
        }
        File output = Path.of(outpath,  name + "_" + type.name +".png").toFile();
        ImageIO.write(bee, "png", output);
        System.out.println("GEN: " + name + " " + type.name);
    }
}
