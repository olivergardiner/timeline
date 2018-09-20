package uk.co.johnlewispartnership.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class SheetHelper {
	
	private SheetHelper() {
	}
	
	public static Row nextRow(Row row) {
		return row.getSheet().getRow(row.getRowNum() + 1);
	}
	
	public static String getCellStringValue(Sheet sheet, int r, int c) {
		return getCellStringValue(sheet.getRow(r), c);
	}

	public static String getCellStringValue(Row row, int c) {
		if (row != null) {
			return getCellStringValue(row.getCell(c));
		}
		
		return "";
	}

	public static String getCellStringValue(Cell cell) {
		if (cell != null) {
			if (cell.getCellType() == CellType.STRING) {
				return cell.getStringCellValue();
			} else if (cell.getCellType() == CellType.NUMERIC) {
				Double d = cell.getNumericCellValue();
				return Integer.toString(d.intValue());
			}
		}
		
		return "";
	}

	public static double getCellNumericValue(Sheet sheet, int r, int c) {
		return getCellNumericValue(sheet.getRow(r), c);
	}

	public static double getCellNumericValue(Row row, int c) {
		if (row != null) {
			return getCellNumericValue(row.getCell(c));
		}
		
		return Double.NaN;
	}

	public static double getCellNumericValue(Cell cell) {
		if (cell != null && cell.getCellType() == CellType.NUMERIC) {
			return cell.getNumericCellValue();
		}
		
		return Double.NaN;
	}
}
