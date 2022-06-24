package github.mrh0.beekeeping.datagen.graphics;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.bee.item.BeeItem;

import javax.imageio.ImageIO;
import java.awt.*;
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
        File princess = Path.of(rp, "princess.png").toFile();
        File queen = Path.of(rp, "queen.png").toFile();
        File princessDark = Path.of(rp, "princess_dark.png").toFile();
        File queenDark = Path.of(rp, "queen_dark.png").toFile();
        File beehiveSide = Path.of(rp, "beehive_side.png").toFile();
        File beehiveBottom = Path.of(rp, "beehive_bottom.png").toFile();
        File beehiveTop = Path.of(rp, "beehive_top.png").toFile();

        String path = Path.of("../src/main/resources/assets/beekeeping/textures/item/").toAbsolutePath().toString();
        System.out.println(path);
        String pathHive = Path.of("../src/main/resources/assets/beekeeping/textures/block/beehives/").toAbsolutePath().toString();
        System.out.println(path);



        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            File princessCrown = princess;
            File queenCrown = queen;
            if(specie.isDark()) {
                princessCrown = princessDark;
                queenCrown = queenDark;
            }

            makeIcon(bee, princessCrown, queenCrown, path, specie.getName(), BeeItem.BeeType.DRONE, specie.getColor());
            makeIcon(bee, princessCrown, queenCrown, path, specie.getName(), BeeItem.BeeType.PRINCESS, specie.getColor());
            makeIcon(bee, princessCrown, queenCrown, path, specie.getName(), BeeItem.BeeType.QUEEN, specie.getColor());
            if(specie.hasBeehive()) {
                makeBeehive(beehiveSide, specie.getName() + "_side.png", specie.getColor(), pathHive);
                makeBeehive(beehiveBottom, specie.getName() + "_bottom.png", specie.getColor(), pathHive);
                makeBeehive(beehiveTop, specie.getName() + "_top.png", specie.getColor(), pathHive);
            }
        }
    }

    public static void makeBeehive(File hiveFile, String name, int color, String outpath) throws IOException {
        BufferedImage beehive = ImageIO.read(hiveFile);
        int size = 16;

        for(int x = 0; x < size; x++) {
            for(int y = 0; y < size; y++) {
                int argb = beehive.getRGB(x, y);
                Color in = new Color(argb);
                Color swap = new Color(color);

                float m = (((float)in.getRed()/255f) + ((float)in.getGreen()/255f) + ((float)in.getBlue()/255f)) / 3f;
                float r = m * ((float)swap.getRed()/255f);
                float g = m * ((float)swap.getGreen()/255f);
                float b = m * ((float)swap.getBlue()/255f);

                Color out = new Color(r, g, b, 1f);
                beehive.setRGB(x, y, out.getRGB());
            }
        }

        File output = Path.of(outpath,  name).toFile();
        if(!output.exists())
            output.createNewFile();
        ImageIO.write(beehive, "png", output);
    }

    public static void makeIcon(File beeFile, File princessFile, File queenFile, String outpath, String name, BeeItem.BeeType type, int border) throws IOException {
        BufferedImage bee = ImageIO.read(beeFile);
        BufferedImage princessBI = ImageIO.read(princessFile);
        BufferedImage queenBI = ImageIO.read(queenFile);
        int size = 16;

        for(int x = 0; x < size; x++) {
            for(int y = 0; y < size; y++) {
                if(bee.getRGB(x, y) == 0xFFFF00FF)
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
