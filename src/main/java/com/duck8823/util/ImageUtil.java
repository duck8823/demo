package com.duck8823.util;

import lombok.extern.log4j.Log4j;
import org.bytedeco.javacpp.Loader;
import org.springframework.util.StopWatch;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

import static org.bytedeco.javacpp.opencv_core.Mat;
import static org.bytedeco.javacpp.opencv_core.RectVector;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;

/**
 * Created by maeda on 2015/12/13.
 */
@Log4j
public class ImageUtil {

	private CascadeClassifier faceDetector;

	private static ImageUtil IMAGE_UTIL = new ImageUtil();
	private ImageUtil() {
		/* 検出器の初期化 */
		File file;
		try {
			URL url = new URL("https://raw.githubusercontent.com/Itseez/opencv/3.1.0/data/haarcascades/haarcascade_frontalface_alt.xml");
			file = Loader.extractResource(url, null, "classifier", ".xml");
		} catch (IOException e) {
			throw new ExceptionInInitializerError("分類器の初期化に失敗しました.");
		}
		file.deleteOnExit();
		this.faceDetector = new CascadeClassifier(file.getAbsolutePath());
	}
	public static ImageUtil getInstance() {
		return IMAGE_UTIL;
	}


	/**
	 * URLから画像を読み込みバイナリ配列を取得する
	 * @param url 画像URL
	 * @return byte[] 画像バイナリデータ
	 * @throws IOException
	 */
	public static byte[] readFromURL(String url) throws IOException {
		BufferedImage readImage = ImageIO.read(new URL(url));
		ByteArrayOutputStream outPutStream = new ByteArrayOutputStream();
		ImageIO.write(readImage, "JPEG", outPutStream);
		return outPutStream.toByteArray();
	}

	/**
	 * バイナリデータから顔領域を検出する
	 * @param bytes 変換前の画像バイナリデータ
	 * @return boolean 顔領域が検出されたかどうか
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public boolean detectFaces(byte[] bytes) throws URISyntaxException, IOException {

		File tmpDir = new File("./tmp");
		if(!tmpDir.exists()){
			tmpDir.mkdir();
		}

		String tmpPathname = tmpDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
		write(bytes, tmpPathname);

		Mat source = imread(tmpPathname);
		new File(tmpPathname).deleteOnExit();

		cvtColor(source, source, COLOR_BGR2GRAY);

		RectVector detections = new RectVector();
		faceDetector.detectMultiScale(source, detections);

		return detections.size() > 0;
	}

	/**
	 * バイナリファイルを一時ファイルに出力する
	 * @param bytes 変換前の画像バイナリデータ
	 * @param pathname 一時ファイルパス
	 * @throws IOException
	 */
	public static void write(byte[] bytes, String pathname) throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		FileOutputStream outputStream = new FileOutputStream(pathname);

		BufferedImage image = ImageIO.read(inputStream);
		ImageIO.write(image, "JPEG", outputStream);

		outputStream.flush();
		outputStream.close();
	}

	/**
	 * 画像を90度回転する
	 *
	 * @param bytes 変換前の画像バイナリデータ
	 * @return byte[] 変換後の画像バイナリ
	 * @throws Exception
	 */
	public static byte[] rotate(byte[] bytes) throws IOException {

		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		BufferedImage src = ImageIO.read(inputStream);

		int height = src.getHeight();
		AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(90), height/2, height/2);
		AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);

		BufferedImage dst = new BufferedImage(src.getHeight(), src.getWidth(), src.getType());
		transformOp.filter(src, dst);

		BufferedOutputStream stream = new BufferedOutputStream(outputStream);
		dst.flush();
		ImageIO.write(dst, "JPEG", outputStream);
		stream.flush();
		stream.close();
		return outputStream.toByteArray();
	}

	/**
	 * 画像のサイズを変更する
	 *
	 * @param bytes 変換前の画像バイナリデータ
	 * @param length 長さ指定 : px
	 * @return byte[] 変換後の画像バイナリ
	 * @throws Exception
	 */
	public static byte[] resize(byte[] bytes, int length, ResizeMode mode) throws IOException {
		return resize(bytes, length, mode, false);
	}

	/**
	 * 画像のサイズを変更する
	 *
	 * @param bytes 変換前の画像バイナリデータ
	 * @param length 長さ指定 : px
	 * @param createThumbnail サムネイルにするかどうか
	 * @return byte[] 変換後の画像バイナリ
	 * @throws Exception
	 */
	public static byte[] resize(byte[] bytes, int length, ResizeMode mode, boolean createThumbnail) throws IOException {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		log.debug("画像のリサイズ: 開始");

		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		BufferedImage image;
		if(createThumbnail) {
			image = trim(ImageIO.read(inputStream));
		} else {
			image = ImageIO.read(inputStream);
		}

		double scale = mode.getScale(length, image);
		if(scale >= 1){
			return bytes;
		}

		AffineTransformOp atOp = new AffineTransformOp( AffineTransform.getScaleInstance(scale, scale), null);
		BufferedImage newImage = new BufferedImage( (int) (image.getWidth() * scale), (int) (image.getHeight() * scale), image.getType());

		int size = (int) (1 / scale);
		float[] kernelData = new float[size * size];
		for (int i = 0; i < size * size; i++) {
			kernelData[i] = 1.0f / size / size;
		}
		Kernel kernel = new Kernel(size, size, kernelData);
		ConvolveOp coOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		BufferedImage inter = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		coOp.filter(image, inter);
		atOp.filter(inter, newImage);

		BufferedOutputStream stream = new BufferedOutputStream(outputStream);
		newImage.flush();
		ImageIO.write(newImage, "JPEG", outputStream);
		stream.flush();
		stream.close();

		stopWatch.stop();


		log.debug("画像のリサイズ: 完了(" + stopWatch.getTotalTimeMillis() + "ms)");

		return outputStream.toByteArray();
	}

	/**
	 * 画像を正方形に切り取る
	 *
	 * @param image トリミング前の画像
	 * @return BufferedImage トリミング後の画像
	 */
	private static BufferedImage trim(BufferedImage image){
		int len;
		int x;
		int y;
		if(image.getWidth() > image.getHeight()){
			len = image.getHeight();
			x = (image.getWidth() - len) / 2;
			y = 0;
		} else {
			len = image.getWidth();
			x = 0;
			y = (image.getHeight() - len) / 2;
		}
		return image.getSubimage(x, y, len, len);
	}

	public enum ResizeMode {
		SHORT{
			@Override
			double getScale(int length, BufferedImage image) {
				if(image.getWidth() > image.getHeight()){
					return HEIGHT.getScale(length, image);
				} else {
					return WIDTH.getScale(length, image);
				}
			}
		},
		LONG{
			@Override
			double getScale(int length, BufferedImage image) {
				if(image.getWidth() > image.getHeight()){
					return WIDTH.getScale(length, image);
				} else {
					return HEIGHT.getScale(length, image);
				}
			}
		},
		HEIGHT{
			@Override
			double getScale(int length, BufferedImage image) {
				return (double) length / image.getHeight();
			}
		},
		WIDTH{
			@Override
			double getScale(int length, BufferedImage image) {
				return (double) length / image.getWidth();
			}
		},
		;

		abstract double getScale(int length, BufferedImage image);
	}

}
