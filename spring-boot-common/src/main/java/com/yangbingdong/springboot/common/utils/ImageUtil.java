package com.yangbingdong.springboot.common.utils;

import lombok.Builder;
import lombok.Data;

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
public final class ImageUtil {

	/**
	 * 图片平等切割
	 * @param img 数据实体 -> 行、列、输入流
	 * @return 平等切割好的图片列表
	 * @throws IOException
	 */
	public static List<BufferedImage> cutImage(Image img) throws IOException {
		BufferedImage image = ImageIO.read(img.getInputStream());
		int chunkWidth = image.getWidth() / img.getCols();
		int chunkHeight = image.getHeight() / img.getRows();
		return IntStream.range(0, img.getRows())
						.parallel()
						.mapToObj(x -> IntStream.range(0, img.getCols())
												.parallel()
												.mapToObj(y -> {
													BufferedImage bufferedImage = new BufferedImage(chunkWidth, chunkHeight, image.getType());
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
	@Data
	public static class Image {
		private final InputStream inputStream;
		private final int rows;
		private final int cols;
	}
}
