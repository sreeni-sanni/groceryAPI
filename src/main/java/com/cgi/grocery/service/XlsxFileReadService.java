package com.cgi.grocery.service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cgi.grocery.model.Grocery;
import com.monitorjbl.xlsx.StreamingReader;

@Service
public class XlsxFileReadService {
	private static final Logger logger = LoggerFactory.getLogger(XlsxFileReadService.class);

	ArrayList<Grocery> data = new ArrayList<>();

	public List<Grocery> getData() {
		return data;
	}

	public void readXlsxFile(File file) throws IOException, ParseException, InvalidFormatException {
		try {
			long start = System.currentTimeMillis();
			Workbook workbook = StreamingReader.builder().rowCacheSize(50000).bufferSize(15360).open(file);
			for (Sheet sheet : workbook) {
				int counter = 0;
				for (Row row : sheet) {
					if (counter > 0) {
						Grocery grocery = new Grocery();
						grocery.setItemName(null == row.getCell(1) ? "" : row.getCell(1).getStringCellValue());
						try {
							DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
							if (row.getCell(2).getCellTypeEnum() == CellType.STRING) {
								grocery.setDate(row.getCell(2).getStringCellValue().toString());
							} else {
								Date date = row.getCell(2).getDateCellValue();
								grocery.setDate(df.format(date));
							}
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
						if (row.getCell(3).getStringCellValue().equalsIgnoreCase("null")) {
							grocery.setPrice(0);
						} else {
							grocery.setPrice(Float.parseFloat(row.getCell(3).getStringCellValue()));
						}
						data.add(grocery);
					}
					counter++;
				}
			}
			long time = System.currentTimeMillis() - start;
       			logger.info("File data reading is completed " + time + " ms and size is "
					+ data.size());

		} catch (Exception e) {
			logger.error("Error occured while reading xlsx file", e);
		}
	}
}
