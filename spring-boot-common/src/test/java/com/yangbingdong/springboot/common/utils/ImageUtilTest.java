package com.yangbingdong.springboot.common.utils;

import com.yangbingdong.springboot.common.utils.ImageUtil.Image;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * @author ybd
 * @date 18-1-25
 * @contact yangbingdong1994@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageUtilTest {

	@Test
	public void cutImage() throws IOException {
		URL url = new URL("http://ojoba1c98.bkt.clouddn.com/img/header/header_background.jpg");
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		InputStream in = conn.getInputStream();
		int rows = 10;
		int cols = 10;
		Image image = Image.builder()
						   .inputStream(in)
						   .rows(rows)
						   .cols(cols)
						   .build();
		List<BufferedImage> bufferedImages = ImageUtil.cutImage(image);
		Assertions.assertThat(bufferedImages)
				  .isNotEmpty()
				  .hasSize(rows * cols);

		/*String[] suffix = "header_background.jpg".split("\\.");
		for (int i = 0; i < bufferedImages.size(); i++) {
			ImageIO.write(bufferedImages.get(i), suffix[1], new File("/home/ybd/data/git-repo/github/ImageCut/img/split/" + suffix[0] + i + "." + suffix[1]));
		}*/
	}
}