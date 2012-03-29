#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <vector>
#include <limits>
#include <map>
#include <math.h>
#include <iomanip>
#include <time.h>
#include <cstdlib>
#include <ctime>

using namespace std;
using std::cerr;
using std::endl;


//struct Test {
//	int row1, col1, row2, col2;
//	float expectedResult;
//
//	Test(int r1, int c1, int r2, int c2, float er) {
//		row1 = r1;
//		col1 = c1;
//		row2 = r2;
//		col2 = c2;
//		expectedResult = er;
//	}
//};
//
//
//
//float squared_hexa_distance_from_even_row(int colDiff, int rowDiff) {
//	float correctedColDiff = colDiff;
//	if (rowDiff % 2 != 0) {
//		correctedColDiff -= 0.5;
//	}
//
//	return ((correctedColDiff * correctedColDiff) + (0.75 * rowDiff * rowDiff));
//}
//
//float squared_hexa_distance_from_odd_row(int colDiff, int rowDiff) {
//	float correctedColDiff = colDiff;
//	if (rowDiff % 2 != 0) {
//		correctedColDiff += 0.5;
//	}
//
//	return ((correctedColDiff * correctedColDiff) + (0.75 * rowDiff * rowDiff));
//}
//
//float squared_hexa_distance(int row1, int col1, int row2, int col2) {
//	int absoluteRowDiff = abs(row1 - row2);
//	int colDiff = abs(col1 - col2);
//
//	if (col1 % 2 == 0) {
//		return squared_hexa_distance_from_even_row(colDiff, rowDiff);
//	} else {
//		return squared_hexa_distance_from_odd_row(colDiff, rowDiff);
//	}
//}
//
//void verify(Test test) {
//	float result = sqrt(squared_hexa_distance(test.row1, test.col1, test.row2, test.col2));
//
//	if (result == test.expectedResult) {
//		cout << "Correct." << endl;
//	} else {
//		cout << "Test failed.  ";
//		cout << test.row1 << ", " << test.col1;
//		cout << " to ";
//		cout << test.row2 << ", " << test.col2;
//		cout << " gave " << result << " but expected " << test.expectedResult << endl;
//	}
//}

const int g_rows = 20;
const int g_columns = 14;

float squared_hexa_distance_from_even_row(int colDiff, int rowDiff) {
	float correctedColDiff = colDiff;
	if ((rowDiff % 2) != 0) {
		correctedColDiff -= 0.5;
	}

	return ((correctedColDiff * correctedColDiff) + (0.75 * rowDiff * rowDiff));
}

float squared_hexa_distance_from_odd_row(int colDiff, int rowDiff) {
	float correctedColDiff = colDiff;
	if ((rowDiff % 2) != 0) {
		correctedColDiff += 0.5;
	}

	return ((correctedColDiff * correctedColDiff) + (0.75 * rowDiff * rowDiff));
}

const int g_maxRowDelta		= g_rows - 1;
const int g_gaussiansHeight	= (2 * g_maxRowDelta) + 1;
const int g_maxColumnDelta	= g_columns - 1;
const int g_gaussiansWidth	= (2 * g_maxColumnDelta) + 1;
const int g_gaussiansCenter	= (g_maxRowDelta * g_gaussiansWidth) + g_maxColumnDelta;
int gaussiansIndex(int rowDelta, int colDelta) {
	return g_gaussiansCenter + (rowDelta * g_gaussiansWidth) + colDelta;
}

float* g_gaussiansToEvenRow;
float* g_gaussiansToOddRow;
void updateGaussians(float width) {
	for (int rowDelta = 1 - g_rows; rowDelta < g_rows; rowDelta++) {
		for (int colDelta = 1 - g_columns; colDelta < g_columns; colDelta++) {
			int flatIndex =	gaussiansIndex(rowDelta, colDelta);

			g_gaussiansToEvenRow[flatIndex] =
					exp(-squared_hexa_distance_from_even_row(colDelta, rowDelta) / (width * width));
			g_gaussiansToOddRow[flatIndex] =
					exp(-squared_hexa_distance_from_odd_row(colDelta, rowDelta) / (width * width));
		}
	}
}

// . , -  = * @ #
void p(float f) {
	if (f < 1.0/7) {
		cout << " ";
	} else if (f < 2.0/7) {
		cout << ".";
	} else if (f < 3.0/7) {
		cout << ",";
	} else if (f < 4.0/7) {
		cout << "-";
	} else if (f < 5.0/7) {
		cout << "=";
	} else if (f < 6.0/7) {
		cout << "@";
	} else {
		cout << "#";
	}
}

int main(int argc, char *argv[]) {
	float width = 5;

	g_gaussiansToEvenRow = new float[g_gaussiansWidth * g_gaussiansHeight];
	g_gaussiansToOddRow = new float[g_gaussiansWidth * g_gaussiansHeight];
	updateGaussians(width);


	int winnerrow = 9;
	int winnercolumn = 8;

	for (int row = 0; row < g_rows; row++) {
		if (row % 2 == 1) {
			cout << "    ";
		}

		for (int column = 0; column < g_columns; column++) {
			int rowDelta = row - winnerrow;
			int colDelta = column - winnercolumn;
			int flatIndex =	gaussiansIndex(rowDelta, colDelta);

			float gauss;
			if (row % 2 == 0) {
				gauss = g_gaussiansToEvenRow[flatIndex];
			} else {
				gauss = g_gaussiansToOddRow[flatIndex];
			}

//			p(gauss);
//			cout << "     ";
			printf("%.3f    ", gauss);
		}

		cout << endl << endl << endl;
	}

//	verify(Test(0, 0, 0, 0, 0.0));
//	verify(Test(1, 1, 1, 1, 0.0));
//	verify(Test(1, 2, 1, 2, 0.0));
//	verify(Test(3, 0, 3, 0, 0.0));
//	verify(Test(0, 0, 1, 0, 1.0));
//	verify(Test(0, 0, 2, 1, 2.0));
//	verify(Test(0, 0, 1, 1, sqrtf(1.5*1.5 + .75*.75)));
//
////	for (int row1 = 0; row1 < NROWS; row1++) {
////		for (int col1 = 0; col1 < NCOLS; col1++) {
////			for (int row2 = row1; row2 < NROWS; row2++) {
////				for (int col2 = col1; col2 < NCOLS; col2++) {
////					cout << row1 << ", " << col1 << " to " << row2 << ", " << col2 << ": ";
////					cout << sqrt(squared_hexa_distance(row1, col1, row2, col2)) << endl;
////				}
////			}
////		}
////	}

	return 0;
}
