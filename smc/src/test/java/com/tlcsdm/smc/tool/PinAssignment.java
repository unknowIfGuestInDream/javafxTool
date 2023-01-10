package com.tlcsdm.smc.tool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.cell.CellLocation;
import cn.hutool.poi.excel.cell.CellUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * pin数据整理
 */
public class PinAssignment {

	// excel的父级目录路径
	//private final String parentDirectoryPath = "C:\\workspace\\test";
	private final String parentDirectoryPath = "E:\\testPlace\\spec";
	// excel文件名称
	private final String excelName = "E02_01_List_of_Pin_Assignment.xlsx";

	private static int startX;
	private static int startY;
	private static int endX;
	private static int endY;

	private static int pinStartX;
	private static int pinStartY;
	private static int pinEndX;
	private static int pinEndY;

	private static final int deviceNum = 11;

	@BeforeAll
	public static void init() {
		CellLocation cellLocation = ExcelUtil.toLocation("AR6");
		startX = cellLocation.getX();
		startY = cellLocation.getY();
		CellLocation cellLocation1 = ExcelUtil.toLocation("BW773");
		endX = cellLocation1.getX();
		endY = cellLocation1.getY();

		CellLocation cellLocation2 = ExcelUtil.toLocation("T6");
		pinStartX = cellLocation2.getX();
		pinStartY = cellLocation2.getY();
		CellLocation cellLocation3 = ExcelUtil.toLocation("AD773");
		pinEndX = cellLocation3.getX();
		pinEndY = cellLocation3.getY();
	}

	@Test
	public void deal() {
		File file = FileUtil.file(parentDirectoryPath, excelName);
		ExcelReader reader = ExcelUtil.getReader(file, "data");

		List<List<String>> putList = new ArrayList<>();
		for (int i = startY; i <= endY; i++) {
			List<String> l = new ArrayList<>();
			for (int j = startX; j <= endX; j++) {
				l.add(valueOf(CellUtil.getCellValue(reader.getCell(j, i))));
			}
			putList.add(l);
		}

		List<List<String>> pinList = new ArrayList<>();
		for (int i = pinStartY; i <= pinEndY; i++) {
			List<String> l = new ArrayList<>();
			for (int j = pinStartX; j <= pinEndX; j++) {
				l.add(valueOf(CellUtil.getCellValue(reader.getCell(j, i))));
			}
			pinList.add(l);
		}
		reader.close();

		ExcelWriter excelWriter = ExcelUtil.getWriter(file, "result");

		for (int i = 0; i < putList.size(); i++) {
			List<String> l = putList.get(i);
			for (int j = 0; j < l.size(); j++) {
				if ("TAUD0I0".equals(l.get(j))) {
					List<String> p = pinList.get(i);
					for (int k = 0; k < p.size(); k++) {
						if (StrUtil.isNotEmpty(p.get(k))) {
							excelWriter.getCell("B3").setCellValue(p.get(k));
						}
					}
				}
			}
		}
		excelWriter.flush();
		excelWriter.close();

	}

	private static String valueOf(Object obj) {
		return (obj == null) ? "" : obj.toString();
	}

}
