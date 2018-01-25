package com.yangbingdong.springbootcommon.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;

/**
 * @author ybd
 */
public class ImageUtil {
	public static List<BufferedImage> cutImage(Image img) throws IOException {
		BufferedImage image = ImageIO.read(img.getInputStream());
		int chunkWidth = image.getWidth() / img.getCols();
		int chunkHeight = image.getHeight() / img.getRows();
		return IntStream.range(0, img.getRows())
						.parallel()
						.mapToObj(x -> IntStream.range(0, img.getCols())
												.parallel()
												.mapToObj(y -> {
													BufferedImage bufferedImage = new BufferedImage(chunkWidth, chunkHeight, image
															.getType());
													Graphics2D gr = bufferedImage.createGraphics();
													gr.drawImage(image, 0, 0,
															chunkWidth, chunkHeight,
															chunkWidth * y, chunkHeight * x,
															chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight,
															null);
													gr.dispose();
													return bufferedImage;
												}))
						.flatMap(identity())
						.collect(toList());
	}

	@Builder
	@Getter
	@Setter
	public static class Image {
		private final InputStream inputStream;
		private final int rows;
		private final int cols;
	}
}
