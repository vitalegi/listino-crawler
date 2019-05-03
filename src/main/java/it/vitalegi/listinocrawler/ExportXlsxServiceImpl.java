package it.vitalegi.listinocrawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ExportXlsxServiceImpl {

	public void save(File target, List<List<String>> entries) {

		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet sheet = workbook.createSheet("Sheet");

			int rowNum = 0;

			XSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setWrapText(true);
			XSSFRow header = sheet.createRow(0);
			header.createCell(0).setCellValue("Col1");
			header.createCell(1).setCellValue("Col2");
			header.createCell(2).setCellValue("Col3");
			header.createCell(3).setCellValue("Col4");

			for (List<String> entry : entries) {
				Row row = sheet.createRow(++rowNum);
				int colNum = 0;
				for (String value : entry) {
					Cell cell = row.createCell(colNum++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(value);
				}
			}

			try (OutputStream os = new FileOutputStream(target)) {
				workbook.write(os);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
