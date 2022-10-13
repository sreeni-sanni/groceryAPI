package com.cgi.grocery.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cgi.grocery.model.Grocery;

@Service
public class XlsxFileWriteService {

	public File writeToXlsxFile(Map<String, List<Grocery>> map) throws FileNotFoundException, IOException {
		File file = new File("Grocery.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		map.forEach((k, v) -> {
			String sheetName = k;
			if (StringUtils.hasLength(k)) {
				if (k.length() > 30) {
					sheetName = sheetName.substring(0, 30);
				}
				XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(sheetName));
				int rowCount = 0;
				for (String[] grocery : getReportData(v)) {
					Row row = sheet.createRow(++rowCount);
					int columnCount = 0;
					for (String str : grocery) {
						Cell cell = row.createCell(++columnCount);
						cell.setCellValue(str);
					}
				}
			}
		});
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			workbook.write(outputStream);
		}catch(Exception e) {
			System.out.println(e);
		}
		return file;
	}

	public List<String[]> getReportData(List<Grocery> data) {
		List<String[]> list = new ArrayList<>();
		String[] headers = { "itemName", "price", "date" };
		list.add(headers);
		data.forEach(item -> {
			list.add(new String[] { item.getItemName(), String.valueOf(item.getPrice()), item.getDate() });
		});
		return list;
	}

}
