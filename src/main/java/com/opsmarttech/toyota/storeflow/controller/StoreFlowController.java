package com.opsmarttech.toyota.storeflow.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opsmarttech.toyota.storeflow.model.entity.Result1;
import com.opsmarttech.toyota.storeflow.model.entity.sub1.NodeDevice;
import com.opsmarttech.toyota.storeflow.service.INodeDeviceService;
import com.opsmarttech.toyota.storeflow.service.IResult1Service;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(path = "/")
public class StoreFlowController {

    @Autowired
    private IResult1Service result1Service;

    @Autowired
    private INodeDeviceService nodeDeviceService;

    @RequestMapping(value = "/fectchExhv1", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject fetchExhibition_v1(@RequestBody JSONObject jsonParam) {//查 到展和全量
        try {
            int dateBegin = jsonParam.getInteger("begin");
            int dateEnd = jsonParam.getInteger("end");
            int dimid = jsonParam.getInteger("dimid");
            JSONArray jsonArray = jsonParam.getJSONArray("arr");
            if(jsonArray == null || jsonArray.size() == 0) throw new Exception("input device number is empty");
            HashMap<String, HashMap<Integer, List<Result1>>> deviceRefData = new HashMap<>();
            for(int i = 0; i < jsonArray.size(); i ++) {
                String deviceSerial = jsonArray.getString(i);
                List<NodeDevice> list = nodeDeviceService.queryNodeInfo(deviceSerial);
                HashMap<Integer, List<Result1>> innerMap = new HashMap<>();
                for(NodeDevice device : list) {
                    int nodeId = device.getNodeId();
                    List<Result1> dataList = result1Service.queryExchbInfoV1(dateBegin, dateEnd, dimid, nodeId);
                    innerMap.put(device.getNodeId(), dataList);
                }
                deviceRefData.put(deviceSerial, innerMap);
            }

            System.out.println(deviceRefData.toString());

            writeForV1(deviceRefData, dateBegin, dateEnd);

        } catch(Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    @RequestMapping(value = "/queryActive", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject fetchActiveInfo(@RequestBody JSONObject jsonParam) {//查在线率
        try {
            int dateBegin = jsonParam.getInteger("begin");
            int dateEnd = jsonParam.getInteger("end");
            int dimid = jsonParam.getInteger("dimid");
            JSONArray jsonArray = jsonParam.getJSONArray("arr");
            if(jsonArray == null || jsonArray.size() == 0) throw new Exception("input device number is empty");
            HashMap<String, HashMap<Integer, List<Result1>>> deviceRefData = new HashMap<>();
            for(int i = 0; i < jsonArray.size(); i ++) {
                String deviceSerial = jsonArray.getString(i);
                List<NodeDevice> list = nodeDeviceService.queryNodeInfo(deviceSerial);
                HashMap<Integer, List<Result1>> innerMap = new HashMap<>();
                for(NodeDevice device : list) {
                    int nodeId = device.getNodeId();
                    List<Result1> dataList = result1Service.queryExchbInfoV1(dateBegin, dateEnd, dimid, nodeId);
                    innerMap.put(device.getNodeId(), dataList);
                }
                deviceRefData.put(deviceSerial, innerMap);
            }
            System.out.println(deviceRefData.toString());
            writeForV1(deviceRefData, dateBegin, dateEnd);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    @ResponseBody
    @RequestMapping(value = "/fetchStroeFlow/{nodeId}", method = RequestMethod.POST)
    public List<ExcelVo> fetchStoreFlowData(@PathVariable("nodeId")int nodeId) {
        List<ExcelVo> excelVos = new ArrayList<>();
        try {
            excelVos = readFromExcel();
            for(ExcelVo vo : excelVos) {
                List<Result1> list = result1Service.queryStoreFlow(nodeId);
                HashSet<String> dateWith = new HashSet<>();
                for(Result1 result1 : list) {
                    dateWith.add(String.valueOf(result1.getBegindate()));
                }
                vo.setPassageFlows(list);
                vo.setDateWith(dateWith);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {

        }
        return excelVos;
    }

    @ResponseBody
    @RequestMapping(value = "/exportSwatchData/{begin}/{end}", method = RequestMethod.POST)
    public String fetchSwatchList(@PathVariable("begin")int dateBegin, @PathVariable("end")int dateEnd) {
        String ret = "";
        int [] midIds = new int[] {18, 8, 7,3};
        try {
            List<ExcelVo> excelVos = readFromExcelSwatch();
            if(excelVos.size() == 0) return "export failed";

            for(ExcelVo vo : excelVos) {
                for(int midId : midIds) {
                    List<Result1> list = result1Service.querySwatchInfo(dateBegin, dateEnd, midId, vo.getNodeId());
                    int sum = 0;
                    for(Result1 result1 : list) {
                        sum += result1.getValue();
                    }
                    if(midId == 18) {
                        vo.setDeviceCount(sum);
                    } else if(midId == 7) {
                        vo.setActiveGuestNum(sum);
                    } else if(midId == 8) {
                        vo.setActivePassgNum(sum);
                    } else if(midId == 3) {
                        vo.setAuthCount(sum);
                    }
                }
                vo.setConnectCount(vo.getActiveGuestNum() + vo.getActivePassgNum());
            }
            boolean exported = writeExcelSwatch(excelVos);
            ret = exported ? "export success" : "export failed";
        } catch(Exception e) {
            e.printStackTrace();
            ret = "export failed";
        }

        ret = "export success";
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/exportFlowData", method = RequestMethod.POST)
    public String exportStoreFlowData() {
        String ret = "";
        try {
            List<ExcelVo> excelVos = new ArrayList<>();
            excelVos = readFromExcel();
            if(excelVos.size() == 0) return "export failed";
            for(ExcelVo vo : excelVos) {
                List<Result1> list = result1Service.queryStoreFlow(vo.getNodeId());
                HashSet<String> dateWith = new HashSet<>();
                for(Result1 result1 : list) {
                    dateWith.add(String.valueOf(result1.getBegindate()));
                }
                vo.setPassageFlows(list);
                vo.setDateWith(dateWith);
            }
            boolean exported = writeExcel(excelVos);
            ret = exported ? "export success" : "export failed";
        } catch(Exception e) {
            e.printStackTrace();
            ret = "export failed";
        }

        ret = "export success";
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/fetchExcelInfo", method = RequestMethod.POST)
    public List<ExcelVo> fetchExcelInfos() {
        return readFromExcel();
    }

    private List<ExcelVo> readFromExcel() {

        List<ExcelVo> excelInfos = new ArrayList<>();
        try {

            Resource resource = new ClassPathResource("static/nodeids.xlsx");
            File file = resource.getFile();
            Workbook workbook = null;
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheetAt(0);
                int maxRows = sheet.getLastRowNum();
                for(int rowNum = 0; rowNum < maxRows;  rowNum ++) {
                    Row row = sheet.getRow(rowNum);
                    int colCount = row.getLastCellNum();
                    if(rowNum == 0) {

                    } else {
                        ExcelVo excelVo = new ExcelVo();
                        int nodeId = 0;
                        try {
                            nodeId = (int) row.getCell(0).getNumericCellValue();
                        } catch (Exception e) {
                            continue;
                        }
                        CellType cellType = row.getCell(1).getCellType();
                        String code = "";
                        code = cellType == CellType.STRING ? row.getCell(1).getStringCellValue() : String.valueOf((int)row.getCell(1).getNumericCellValue());
                        String storeName = row.getCell(2).getStringCellValue();
                        String city = row.getCell(3).getStringCellValue();
                        String province = row.getCell(4).getStringCellValue();
                        String area = row.getCell(5).getStringCellValue();
                        excelVo.setNodeId(nodeId);
                        excelVo.setCode(code);
                        excelVo.setCity(city);
                        excelVo.setStoreName(storeName);
                        excelVo.setProvince(province);
                        excelVo.setArea(area);
                        excelInfos.add(excelVo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                excelInfos.clear();
            }

        } catch(Exception e) {
            e.printStackTrace();
            excelInfos.clear();
        }

        return excelInfos;
    }

    private List<ExcelVo> readFromExcelSwatch() {

        List<ExcelVo> excelInfos = new ArrayList<>();
        try {

            Resource resource = new ClassPathResource("static/swatch.xlsx");
            File file = resource.getFile();
            Workbook workbook = null;
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheetAt(0);
                int maxRows = sheet.getLastRowNum();
                for(int rowNum = 0; rowNum < maxRows;  rowNum ++) {
                    Row row = sheet.getRow(rowNum);
                    int colCount = row.getLastCellNum();
                    if(rowNum == 0) {

                    } else {
                        ExcelVo excelVo = new ExcelVo();
                        excelVo.setCity(row.getCell(1).getStringCellValue());
                        excelVo.setStoreName(row.getCell(3).getStringCellValue());
                        excelVo.setSerial(row.getCell(4).getStringCellValue());
                        excelVo.setConnectCount((int) row.getCell(5).getNumericCellValue());
                        excelVo.setDeviceCount((int)row.getCell(6).getNumericCellValue());
                        excelVo.setAuthCount((int)row.getCell(7).getNumericCellValue());
                        excelVo.setType(row.getCell(9).getStringCellValue());
                        excelVo.setNodeId((int)row.getCell(10).getNumericCellValue());
                        excelInfos.add(excelVo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                excelInfos.clear();
            }

        } catch(Exception e) {
            e.printStackTrace();
            excelInfos.clear();
        }

        return excelInfos;
    }

    private boolean writeExcel(List<ExcelVo> excelVos) {

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            Row title = sheet.createRow(0);
            List<String> dateTitles = getDateRangeStrings(2019, 5, 11, 2019, 11, 17);
            int cellCount = dateTitles.size() + 6;
            for(int cellIndex = 0; cellIndex < cellCount; cellIndex ++ ) {
                Cell cell = title.createCell(cellIndex);
                if(cellIndex == 0) {
                    cell.setCellValue("NODE_ID");
                } else if(cellIndex == 1) {
                    cell.setCellValue("CODE");
                } else if(cellIndex == 2) {
                    cell.setCellValue("经销店");
                } else if(cellIndex == 3) {
                    cell.setCellValue("大区");
                } else if(cellIndex == 4) {
                    cell.setCellValue("省");
                } else if(cellIndex == 5) {
                    cell.setCellValue("城市");
                } else {
                    String dateTitle = dateTitles.get(cellIndex - 6);
                    cell.setCellValue(dateTitle);
                }
            }

            int dataRowIndex = 1;
            for(ExcelVo vo : excelVos) {
                Row row = sheet.createRow(dataRowIndex ++);
                for(int cellIndex = 0; cellIndex < cellCount; cellIndex ++ ) {
                    Cell cell = row.createCell(cellIndex);
                    if(cellIndex == 0) {
                        cell.setCellValue(vo.getNodeId());
                    } else if(cellIndex == 1) {
                        cell.setCellValue(vo.getCode());
                    } else if(cellIndex == 2) {
                        cell.setCellValue(vo.getStoreName());
                    } else if(cellIndex == 3) {
                        cell.setCellValue(vo.getArea());
                    } else if(cellIndex == 4) {
                        cell.setCellValue(vo.getProvince());
                    } else if(cellIndex == 5) {
                        cell.setCellValue(vo.getCity());
                    } else {
                        String dateTitle = dateTitles.get(cellIndex - 6);
                        boolean isContains = vo.getDateWith().contains(dateTitle);
                        if(isContains) {
                            for(Result1 result1 : vo.getPassageFlows()) {
                                if(dateTitle.equals(String.valueOf(result1.getBegindate()))) {
                                    cell.setCellValue(result1.getValue());
                                    break;
                                }
                            }
                        } else {
                            cell.setCellValue("");
                        }
                    }
                }
            }

            Resource resource = new ClassPathResource("static/export.xlsx");
            File file = resource.getFile();
            file.createNewFile();
            try(FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean writeExcelSwatch(List<ExcelVo> excelVos) {

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            Row title = sheet.createRow(0);
            int cellCount = 9;
            for(int cellIndex = 0; cellIndex < cellCount; cellIndex ++ ) {
                Cell cell = title.createCell(cellIndex);
                if(cellIndex == 0) {
                    cell.setCellValue("序号");
                } else if(cellIndex == 1) {
                    cell.setCellValue("城市");
                } else if(cellIndex == 2) {
                    cell.setCellValue("品牌");
                } else if(cellIndex == 3) {
                    cell.setCellValue("店名");
                } else if(cellIndex == 4) {
                    cell.setCellValue("设备");
                } else if(cellIndex == 5) {
                    cell.setCellValue("终端连接次数");
                } else if(cellIndex == 6) {
                    cell.setCellValue("终端数量");
                } else if(cellIndex == 7) {
                    cell.setCellValue("认证数量");
                } else if(cellIndex == 8) {
                    cell.setCellValue("网络类型");
                }
            }

            int dataRowIndex = 1;
            for(ExcelVo vo : excelVos) {
                Row row = sheet.createRow(dataRowIndex);
                for(int cellIndex = 0; cellIndex < cellCount; cellIndex ++ ) {
                    Cell cell = row.createCell(cellIndex);
                    if(cellIndex == 0) {
                        cell.setCellValue(dataRowIndex);
                    } else if(cellIndex == 1) {
                        cell.setCellValue(vo.getCity());
                    } else if(cellIndex == 2) {
                        cell.setCellValue("swatch");
                    } else if(cellIndex == 3) {
                        cell.setCellValue(vo.getStoreName());
                    } else if(cellIndex == 4) {
                        cell.setCellValue(vo.getSerial());
                    } else if(cellIndex == 5) {
                        cell.setCellValue(vo.getConnectCount());
                    } else if(cellIndex == 6) {
                        cell.setCellValue(vo.getDeviceCount());
                    } else if(cellIndex == 7) {
                        cell.setCellValue(vo.getAuthCount());
                    } else if(cellIndex == 8) {
                        cell.setCellValue(vo.getType());
                    }
                }
                dataRowIndex ++;
            }

            Resource resource = new ClassPathResource("static/export.xlsx");
            File file = resource.getFile();
            file.createNewFile();
            try(FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean writeForV1(HashMap<String, HashMap<Integer, List<Result1>>> resultMap, int begin, int end) {

        try {

            Resource resource = new ClassPathResource("static/export_v1.xlsx");
            File file = resource.getFile();
            file.createNewFile();

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            Row titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("设备序列号");
            titleRow.createCell(1).setCellValue("NODE_ID");

            int by = Integer.parseInt(String.valueOf(begin).substring(0, 4));
            int bm = Integer.parseInt(String.valueOf(begin).substring(4, 6));
            int bd = Integer.parseInt(String.valueOf(begin).substring(6));

            int ey = Integer.parseInt(String.valueOf(end).substring(0, 4));
            int em = Integer.parseInt(String.valueOf(end).substring(4, 6));
            int ed = Integer.parseInt(String.valueOf(end).substring(6));

            List<String> dateList = getDateRangeStrings(by, ey, bm - 1, em - 1, bd + 1, ed - 1);
            System.out.println(dateList.toString());

            for(String dateStr : dateList) {
                int lastIndex = titleRow.getLastCellNum();
                titleRow.createCell(lastIndex).setCellValue(dateStr);
            }

            int rowIndex = 1;
            Set<String> keys = resultMap.keySet();
            for(String key : keys) {
                HashMap<Integer, List<Result1>> innerMap = resultMap.get(key);
                Set<Integer> innerKeys = innerMap.keySet();
                for(Integer innerKey : innerKeys) {
                    Row row = sheet.createRow(rowIndex ++);
                    List<Result1> data = innerMap.get(innerKey);
                    row.createCell(0).setCellValue(key);
                    row.createCell(1).setCellValue(innerKey);
                    for(Result1 result : data) {
                        int cellIndex = row.getLastCellNum();
                        row.createCell(cellIndex).setCellValue(result.getValue());
                    }
                }
            }

            try(FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Data
    private class ExcelVo {

        private int nodeId;
        private String code;
        private String storeName;
        private String city;
        private String province;
        private String area;
        private List<Result1> passageFlows;
        private HashSet<String> dateWith;
        private int deviceCount;
        private int connectCount;
        private int authCount;
        private double data;
        private String type;
        private String serial;
        private int activeGuestNum;
        private int activePassgNum;

    }

    private List<String> getDateRangeStrings(int by, int ey, int bm, int em, int bd, int ed) {

        List<String> list = new ArrayList<>();
        //请注意月份是从0-11,天数是1， 201x-x-x 至 201x-x-x
        Calendar start = Calendar.getInstance();
        start.set(by, bm, bd);
        Calendar end = Calendar.getInstance();
        end.set(ey, em, ed);

        int sumSunday = 0;
        int sumSat = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

        while(start.compareTo(end) <= 0) {
            int w = start.get(Calendar.DAY_OF_WEEK);
            if(w == Calendar.SUNDAY)
                sumSunday ++;
            if(w == Calendar.SATURDAY)
                sumSunday ++;
            //打印每天
            String dateStr = format.format(start.getTime());
            //System.out.println(dateStr);
            list.add(dateStr);
            //循环，每次天数加1
            start.set(Calendar.DATE, start.get(Calendar.DATE) + 1);
        }

        return list;
    }

}
