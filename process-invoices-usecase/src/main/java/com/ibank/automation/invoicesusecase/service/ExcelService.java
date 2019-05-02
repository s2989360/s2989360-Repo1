package com.ibank.automation.invoicesusecase.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.ibank.automation.system.invoiceplane.to.ProductTO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelService {

	public static List<ProductTO> parseProducts(byte[] content) throws IOException {
		return readExcel(content);
	}

	private static List<ProductTO> readExcel(byte[] bFile) throws IOException {
		File tempFile = File.createTempFile("test_excel_des", ".xlsx", null);
		FileOutputStream fileOuputStream = new FileOutputStream(tempFile);
		fileOuputStream.write(bFile);
		fileOuputStream.close();
		FileInputStream fis = new FileInputStream(tempFile);
		tempFile.delete();
		return readProductsFromExcel(fis);
	}

	private static List<ProductTO> readProductsFromExcel(FileInputStream file) throws IOException {
		ArrayList<ProductTO> productsList = new ArrayList<>();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			// iterate rows, but skip 1st where header is
			for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) { 
				ProductTO prod = new ProductTO();
				Row ro = sheet.getRow(i);
				// iterate columns, i.e. Product fields
				for (int j = ro.getFirstCellNum(); j <= ro.getLastCellNum(); j++) { 
					Cell ce = ro.getCell(j);
					switch (j) {
					case 0:
						prod.setFamily(ce == null ? "" : ce.getStringCellValue());
						break;
					case 1:
						prod.setSku(ce == null ? "" : String.valueOf(ce.getNumericCellValue()));
						break;
					case 2:
						prod.setProductName(ce == null ? "" : ce.getStringCellValue());
						break;
					case 3:
						prod.setProductDescription(ce == null ? "" : ce.getStringCellValue());
						break;
					case 4:
						prod.setPrice(ce == null ? "" : String.valueOf(ce.getNumericCellValue()));
						break;
					case 5:
						prod.setTaxRate(ce == null ? "" : ce.getStringCellValue());
						break;
					}
				}
				productsList.add(prod);
			}
			workbook.close();
			file.close();
			return productsList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productsList;
	}
	
}