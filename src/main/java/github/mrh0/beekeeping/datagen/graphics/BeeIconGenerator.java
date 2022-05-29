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

    public static void makeAll() throws IOException {
        String rp = Path.of("../src/main/resources/icons").toAbsolutePath().toString();
        File bee = Path.of(rp, "bee.png").toFile();
        File border = Path.of(rp, "border.png").toFile();
        File princess = Path.of(rp, "princess.png").toFile();
        File queen = Path.of(rp, "queen.png").toFile();

        String path = Path.of("../src/main/resources/assets/beekeeping/textures/item/").toAbsolutePath().toString();
        System.out.println(path);

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
                    if(princessBI.getRGB(x, y) != 0x00000000)
                        bee.setRGB(x, y, princessBI.getRGB(x, y));
                }
                else if(type == BeeItem.BeeType.QUEEN) {
                    if(queenBI.getRGB(x, y) != 0x00000000)
                        bee.setRGB(x, y, queenBI.getRGB(x, y));
                }
            }
        }
        File output = Path.of(outpath,  name + "_" + type.name +".png").toFile();
        if(!output.exists())
            output.createNewFile();
        ImageIO.write(bee, "png", output);
        System.out.println("GEN: " + name + " " + type.name);
    }
}
