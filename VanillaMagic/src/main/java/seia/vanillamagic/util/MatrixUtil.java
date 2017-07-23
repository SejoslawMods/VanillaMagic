package seia.vanillamagic.util;

import net.minecraft.item.ItemStack;

/**
 * Class which store various methods connected with Matrixes.
 */
public class MatrixUtil 
{
	private MatrixUtil()
	{
	}
	
	/**
	 * It must be a quad matrix. <br>
	 * From:<br>
	 * [1][2][3][4]<br>
	 * [5][6][7][8]<br>
	 * [9][0][1][2]<br>
	 * [3][4][5][6]<br>
	 * To:<br>
	 * [3][9][5][1]<br>
	 * [4][0][6][2]<br>
	 * [5][1][7][3]<br>
	 * [6][2][8][4]<br>
	 */
	public static ItemStack[][] rotateMatrixRight(ItemStack[][] matrix)
	{
		int size = matrix.length;
		ItemStack[][] newMatrix = new ItemStack[size][size];
		for (int i = 0; i < size; ++i) 
			for (int j = 0; j < size; ++j) 
				newMatrix[i][j] = matrix[size - j - 1][i];
		return newMatrix;
	}
}