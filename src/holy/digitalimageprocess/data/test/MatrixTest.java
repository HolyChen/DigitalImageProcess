package holy.digitalimageprocess.data.test;

import holy.digitalimageprocess.data.ImageChannelMatrix;

public class MatrixTest {
	public static void main(String[] args) {
		ImageChannelMatrix matrix = new ImageChannelMatrix(1024, 768, 1);
		matrix.setEntity(1, 1, -3);
		matrix.setEntity(8, 1, -3);
		ImageChannelMatrix m = matrix.minFitler(15);
		for (int i = 0; i < 10; i++) {
			for (int j  = 0; j < 10; j++) {
				System.out.print(m.getEntity(i, j) + " ");
			}
			System.out.println();
		}
	}
}
