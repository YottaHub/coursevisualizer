package cn.ac.ucas.coursevisualizer;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The ExcelHandler class is used to handle the school timetable Excel file exported from
 * https://jwxk.ucas.ac.cn/course/termSchedule.
 */
public class ExcelHandler {

    /* HashMap to store departments and their courses available in this semester */
    protected Map<String, Set<String>> departmentMap = new HashMap<>();
    /* HashMap to store categories and their courses available in this semester */
    protected Map<String, Set<String>> categoryMap = new HashMap<>();
    /* Count # of courses available in this semester */
    protected int totalCourseCount = 0;
    protected boolean isInitialized = false;

    public ExcelHandler(String excelFilePath) {
        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表

            Set<String> courseNames = new HashSet<>();

            for (Row row : sheet) {
                // do sanity check on the first row
                if (row.getRowNum() == 0) {
                    // sanity check
                    if (sanityCheck(row)) {
                        isInitialized = true;
                        continue;
                    } else {
                        break;
                    }
                }

                Cell departmentCell = row.getCell(1); // "开课院系" at row 1
                Cell courseNameCell = row.getCell(3); // "课程名称" at row 3
                Cell categoryCell = row.getCell(5); // "课程属性" at row 5

                String department = departmentCell.getStringCellValue();
                String category = categoryCell.getStringCellValue();
                String courseName = courseNameCell.getStringCellValue();

                courseNames.add(courseName);

                // add course to department's HashMap
                if (departmentMap.containsKey(department)) {
                    departmentMap.get(department).add(courseName);
                } else {
                    Set<String> courseSet = new HashSet<>();
                    courseSet.add(courseName);
                    departmentMap.put(department, courseSet);
                }

                // add course to category's HashMap
                if (categoryMap.containsKey(category)) {
                    categoryMap.get(category).add(courseName);
                } else {
                    Set<String> courseSet = new HashSet<>();
                    courseSet.add(courseName);
                    categoryMap.put(category, courseSet);
                }
            }

            // get total course count
            totalCourseCount = courseNames.size();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the department map in the following format:
     * Department:
     *     Course Name
     *     Course Name
     *     ...
     *
     * @return the formatted string representation of the department map
     */
    protected String printDepartmentMap() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Set<String>> entry : departmentMap.entrySet()) {
            sb.append(entry.getKey()).append(":\n");
            for (String courseName : entry.getValue()) {
                sb.append("\t").append(courseName).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Prints the category map in the following format:
     * Category:
     *     Course Name
     *     Course Name
     *     ...
     *
     * @return the formatted string representation of the category map
     */
    protected String printCategoryMap() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Set<String>> entry : categoryMap.entrySet()) {
            sb.append(entry.getKey()).append(":\n");
            for (String courseName : entry.getValue()) {
                sb.append("\t").append(courseName).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Sanity check on the first row of the Excel file.
     *
     * @return true if the first row is valid, false otherwise
     */
    private boolean sanityCheck(Row row) {
        if (!row.getCell(1).getStringCellValue().equals("开课院系")) {
            System.out.println("Error: The second cell of the first row is not \"开课院系\".");
            return false;
        }
        if (!row.getCell(3).getStringCellValue().equals("课程名称")) {
            System.out.println("Error: The fourth cell of the first row is not \"课程名称\".");
            return false;
        }
        if (!row.getCell(5).getStringCellValue().equals("课程属性")) {
            System.out.println("Error: The sixth cell of the first row is not \"课程属性\".");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String excelFilePath = "src/main/java/cn/ac/ucas/coursevisualizer/2023年夏季学期课表.xlsx";

        ExcelHandler excelHandler = new ExcelHandler(excelFilePath);

        System.out.println(excelHandler.printDepartmentMap());
        System.out.println(excelHandler.printCategoryMap());
        System.out.println("# of courses: " + excelHandler.totalCourseCount);
    }
}
