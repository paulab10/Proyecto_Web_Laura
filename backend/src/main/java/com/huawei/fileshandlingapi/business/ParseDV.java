package com.huawei.fileshandlingapi.business;


import com.huawei.fileshandlingapi.model.ProductsExcel;
import org.apache.poi.ss.usermodel.*;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.huawei.fileshandlingapi.constants.CooperadoresConstants.DETAIL_VIEW;
import static com.huawei.fileshandlingapi.constants.ExcelConstants.*;

public class ParseDV implements Runnable {

    private volatile Map<String, ProductsExcel> resultsMap;

    private volatile Map<Integer, Integer> indexesMap;

    public Map<String, ProductsExcel> getResultsMap() {
        return resultsMap;
    }

    public ParseDV(Map<Integer, Integer> indexesMap) {
        this.indexesMap = indexesMap;
    }

    public void run() {
        parseSupplierFile();
    }

    private void parseSupplierFile() {
        resultsMap = new HashMap<>();

        Workbook workbook;

        try {
            workbook = ExcelParsing.readExcel(DETAIL_VIEW);
        }catch (Exception e) {
            return;
        }

        // =============================================================
        //   Iterating over all the sheets in the workbook
        // =============================================================
        Sheet sheetMacro = workbook.getSheetAt(0);
        System.out.println("Retrieving Sheets using Iterator");

        Iterator<Row> rowIterator = sheetMacro.rowIterator();
        rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            ProductsExcel result = new ProductsExcel();

            result.setPoNumber(row.getCell(indexesMap.get(PO_NUMBER_INDEX)).getStringCellValue());
            result.setDuNumber(row.getCell(indexesMap.get(DU_INDEX)).getStringCellValue());
            result.setActualDate(row.getCell(indexesMap.get(DATE_INDEX)).getDateCellValue()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            result.setDescription(row.getCell(indexesMap.get(DESCRIPTION_INDEX)).getStringCellValue());
            result.setQuantityDV(row.getCell(indexesMap.get(QUANTITY_INDEX)).getNumericCellValue());
            result.setBilledPercent(row.getCell(indexesMap.get(PO_BILL_PERC_INDEX)).getNumericCellValue());

            String key = result.getDuNumber();

            resultsMap.put(key, result);
        }
    }
}
