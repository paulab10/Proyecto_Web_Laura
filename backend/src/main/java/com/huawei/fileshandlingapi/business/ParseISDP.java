package com.huawei.fileshandlingapi.business;

import com.huawei.fileshandlingapi.model.ProductsExcel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.huawei.fileshandlingapi.constants.CooperadoresConstants.ISDP;
import static com.huawei.fileshandlingapi.constants.ExcelConstants.*;


public class ParseISDP implements Runnable {

    private volatile Map<String, ProductsExcel> resultsMap;

    public Map<String, ProductsExcel> getResultsMap() {
        return resultsMap;
    }

    public ParseISDP() {}

    public void run() {
        parseSupplierFile();
    }

    private void parseSupplierFile() {
        resultsMap = new HashMap<>();

        Workbook workbook;

        try {
            workbook = ExcelParsing.readExcel(ISDP);
        }catch (Exception e) {
            return;
        }

        // =============================================================
        //   Iterating over all the sheets in the workbook
        // =============================================================

        Sheet sheetMacro = workbook.getSheetAt(0);
        System.out.println("Retrieving Sheets using Iterator");

        Iterator<Row> rowIterator = sheetMacro.rowIterator();
        Row headerRow = rowIterator.next();

        Iterator<Cell> cellIterator = headerRow.cellIterator();

        /* Index of columns */
        boolean isCoaxialSet = false;

        int regionIndex = 0;
        int duNumberIndex = 0;
        int actualDateIndex = 0;
        int planDateIndex = 0;
        int beginCoaxialIndex = 0;
        int endCoaxialIndex = 30;

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            if (isCoaxialSet && !cell.getStringCellValue().isEmpty()) {
                isCoaxialSet = false;
                endCoaxialIndex = cell.getColumnIndex();
            }

            if (cell.getStringCellValue().equalsIgnoreCase(DU_ID)) {
                duNumberIndex = cell.getColumnIndex();
                continue;
            }

            if (cell.getStringCellValue().equalsIgnoreCase(REGION)) {
                regionIndex = cell.getColumnIndex();
                continue;
            }

            if (cell.getStringCellValue().equalsIgnoreCase(COAXIAL_LAY)) {
                beginCoaxialIndex = cell.getColumnIndex();
                isCoaxialSet = true;
            }
        }

        Row secondHeader = rowIterator.next();
        cellIterator = secondHeader.cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            if (cell.getStringCellValue().equalsIgnoreCase(PLAN_DATE)) {
                int index = cell.getColumnIndex();
                if (index >= beginCoaxialIndex && index < endCoaxialIndex) {
                    planDateIndex = index;
                }
                continue;
            }

            if (cell.getStringCellValue().equalsIgnoreCase(ACTUAL_DATE)) {
                int index = cell.getColumnIndex();
                if (index >= beginCoaxialIndex && index < endCoaxialIndex) {
                    actualDateIndex = index;
                }
            }
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            ProductsExcel tmpProductsExcel = new ProductsExcel();

            tmpProductsExcel.setRegion(row.getCell(regionIndex).getStringCellValue());
            tmpProductsExcel.setDuNumber(row.getCell(duNumberIndex).getStringCellValue());
            tmpProductsExcel.setActualDate((row.getCell(actualDateIndex).getDateCellValue()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            tmpProductsExcel.setPlanDate(row.getCell(planDateIndex).getDateCellValue()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            String key = tmpProductsExcel.getDuNumber();

            resultsMap.put(key, tmpProductsExcel);
        }
    }
}
