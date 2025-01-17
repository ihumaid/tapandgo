package mv.port.harbour_tapngo;/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class SamplesCommon {

    public enum sampleImages {
        logo("mpl_logo.png");
        public String imageName;
        sampleImages(String imageName){
            this.imageName = imageName;
        }
    }


    public static BufferedImage getImage(sampleImages image) throws IOException {
        URL url = getURL(image.imageName);
        return ImageIO.read(url);

    }


    private static URL getURL(String imageName){
        String strPath =  "images/" +  imageName;
        return SamplesCommon.class
                .getClassLoader()
                .getResource(strPath);
    }

}