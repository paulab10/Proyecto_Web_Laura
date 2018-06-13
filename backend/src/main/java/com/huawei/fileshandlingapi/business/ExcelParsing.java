package com.huawei.fileshandlingapi.business;

import com.huawei.fileshandlingapi.model.ProductsExcel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.huawei.fileshandlingapi.constants.CooperadoresConstants.*;
import static com.huawei.fileshandlingapi.constants.ExcelConstants.*;
import static java.time.temporal.ChronoUnit.DAYS;

public class ExcelParsing {

    private static final String BASE_PATH = "upload-dir/";

    private static Map<Integer, Integer> indexesMap;

    public static Map<String, List<ProductsExcel>> parseDetailView() {
        Workbook workbook;
        try {
            workbook = readExcel(DETAIL_VIEW);
        } catch (Exception e) {
            return null;
        }

        // =============================================================
        //   Iterating over all the sheets in the workbook (Multiple ways)
        // =============================================================

        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        System.out.println("Retrieving Sheets using Iterator");
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            System.out.println("=> " + sheet.getSheetName());
        }

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        Iterator<Row> rowIterator = sheet.rowIterator();
        Row headerRow = rowIterator.next();

        Iterator<Cell> cellIterator = headerRow.cellIterator();

        /* Index of columns */
        indexesMap = new HashMap<Integer, Integer>();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            if (dataFormatter.formatCellValue(cell).equalsIgnoreCase(IBUY_STATUS)) {
                indexesMap.put(STATUS_INDEX, cell.getColumnIndex());
                continue;
            }

            if (dataFormatter.formatCellValue(cell).equalsIgnoreCase(SUPPLIER)) {
                indexesMap.put(SUPPLIER_INDEX, cell.getColumnIndex());
                continue;
            }

            if (cell.getStringCellValue().equalsIgnoreCase(PO_NUM)) {
                indexesMap.put(PO_NUMBER_INDEX, cell.getColumnIndex());
                continue;
            }

            if (cell.getStringCellValue().equalsIgnoreCase(DU_NUMBER)) {
                indexesMap.put(DU_INDEX, cell.getColumnIndex());
                continue;
            }

            if (cell.getStringCellValue().equalsIgnoreCase(QUANTITY)) {
                indexesMap.put(QUANTITY_INDEX, cell.getColumnIndex());
                continue;
            }

            if (cell.getStringCellValue().equalsIgnoreCase(PR_DESCRIPTION)) {
                indexesMap.put(DESCRIPTION_INDEX, cell.getColumnIndex());
                continue;
            }

            if (cell.getStringCellValue().equalsIgnoreCase(START_DATE)) {
                indexesMap.put(DATE_INDEX, cell.getColumnIndex());
                continue;
            }

            if (cell.getStringCellValue().equalsIgnoreCase(PO_BILL_PERC)) {
                indexesMap.put(PO_BILL_PERC_INDEX, cell.getColumnIndex());
            }

        }

        List<Row> validRows = determineValidRows(rowIterator, indexesMap.get(STATUS_INDEX));

        return determineSuppliers(validRows);
    }

    public static Workbook readExcel(String dirName) throws IOException, InvalidFormatException {
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        File folder = new File(BASE_PATH + dirName + "/");
        File[] files = folder.listFiles();
        FileInputStream fileInputStream = null;
        Workbook workbook = null;

        try {
            fileInputStream = new FileInputStream(files[0]);
            workbook = WorkbookFactory.create(fileInputStream);
            //workbook = WorkbookFactory.create(new File(ExcelParsing.class.getClassLoader().
                    //getResource("files/"+dirName).getFile()));

            // Retrieving the number of sheets in the Workbook
            System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("[ERROR] ExcelParsing: Null Pointer error. empty directory");
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }

        return workbook;
    }

    public static List<Row> determineValidRows(Iterator<Row> rowIterator, int statusCellIndex) {
        List<Row> validRows = new ArrayList<>();

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            Cell cell =  row.getCell(statusCellIndex);

            if (dataFormatter.formatCellValue(cell).
                    equalsIgnoreCase("IN PROCESS")) {
                validRows.add(row);
            }
        }
        return validRows;
    }

    public static Map<String, List<ProductsExcel>> determineSuppliers(List<Row> rows) {
        Map<String, List<ProductsExcel>> suppliersMap = new HashMap<>();
        List<ProductsExcel> dicoList = new ArrayList<>();
        List<ProductsExcel> eneconList = new ArrayList<>();
        List<ProductsExcel> fscrList = new ArrayList<>();
        List<ProductsExcel> applusList = new ArrayList<>();
        List<ProductsExcel> sicteList = new ArrayList<>();
        List<ProductsExcel> conectarList = new ArrayList<>();

        int supplierCellIndex = indexesMap.get(SUPPLIER_INDEX);

        for (Row row: rows)
        {
            Cell cell = row.getCell(supplierCellIndex);

            if (cell == null) {
                continue;
            }

            if (cell.getStringCellValue().equalsIgnoreCase(DICO)) {
                dicoList.add(parseSupplierRow(row));
                continue;
            }

            if (cell.getStringCellValue().equalsIgnoreCase(ENECON)) {
                eneconList.add(parseSupplierRow(row));
                continue;
            }

            if (cell.getStringCellValue().equalsIgnoreCase(FSCR)) {
                fscrList.add(parseSupplierRow(row));
                continue;
            }

            if (cell.getStringCellValue().equalsIgnoreCase(APPLUS)) {
                applusList.add(parseSupplierRow(row));
                continue;
            }

            if (cell.getStringCellValue().equalsIgnoreCase(SICTE)) {
                sicteList.add(parseSupplierRow(row));
                continue;
            }

            if (cell.getStringCellValue().equalsIgnoreCase(CONECTAR)) {
                conectarList.add(parseSupplierRow(row));
            }
        }

        suppliersMap.put(DICO_KEY, dicoList);
        suppliersMap.put(FSCR_KEY, fscrList);
        suppliersMap.put(SICTE_KEY, sicteList);
        suppliersMap.put(APPLUS_KEY, applusList);
        suppliersMap.put(ENECON_KEY, eneconList);
        suppliersMap.put(CONECTAR_KEY, conectarList);

        System.out.println("DICO count: " + dicoList.size());
        System.out.println("FSCR count: " + fscrList.size());
        System.out.println("SICTE count: " + sicteList.size());
        System.out.println("APPLUS count: " + applusList.size());
        System.out.println("ENECON count: " + eneconList.size());
        System.out.println("CONECTAR count: " + conectarList.size());

        return suppliersMap;
    }

    private static ProductsExcel parseSupplierRow(Row row) {
        ProductsExcel result = new ProductsExcel();

        result.setPoNumber(row.getCell(indexesMap.get(PO_NUMBER_INDEX)).getStringCellValue());
        result.setDuNumber(row.getCell(indexesMap.get(DU_INDEX)).getStringCellValue());
        result.setActualDate(row.getCell(indexesMap.get(DATE_INDEX)).getDateCellValue()
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        result.setDescription(row.getCell(indexesMap.get(DESCRIPTION_INDEX)).getStringCellValue());
        result.setQuantityDV(row.getCell(indexesMap.get(QUANTITY_INDEX)).getNumericCellValue());
        result.setBilledPercent(row.getCell(indexesMap.get(PO_BILL_PERC_INDEX)).getNumericCellValue());

        return result;
    }


    public static Map<String, List<ProductsExcel>> processDvIsdp()
            throws IOException, InvalidFormatException {

        Map<String, List<ProductsExcel>> resultsMap = new HashMap<>();

        ParseDV parseDV = new ParseDV(indexesMap);
        ParseISDP parseISDP = new ParseISDP();

        Thread t1 = new Thread(parseDV);
        Thread t2 = new Thread(parseISDP);

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        Map<String, ProductsExcel> supplierDV = parseDV.getResultsMap();
        Map<String, ProductsExcel> isdpMap = parseISDP.getResultsMap();

        List<ProductsExcel> nearToCreateList = new ArrayList<>();
        List<ProductsExcel> noPoList = new ArrayList<>();

        for (ProductsExcel rowIsdp: isdpMap.values()) {
            String keyValue = rowIsdp.getDuNumber();

            try {
                ProductsExcel tmp = supplierDV.get(keyValue);
                if (tmp != null) {
                    LocalDate isdpDate = rowIsdp.getActualDate();

                    if (isdpDate.compareTo(tmp.getActualDate()) < 0) {
                        noPoList.add(rowIsdp);
                    }

                } else {
                    LocalDate currentDate = LocalDate.now();
                    long days = DAYS.between(currentDate, rowIsdp.getPlanDate());

                    if (days <= 8) {
                        nearToCreateList.add(rowIsdp);
                    }
                }
            } catch (Exception e) {
                nearToCreateList.add(rowIsdp);
                continue;
            }
        }

        resultsMap.put(NO_PO_KEY, noPoList);
        resultsMap.put(NEAR_TO_CREATE_KEY, nearToCreateList);

        return resultsMap;
    }
}