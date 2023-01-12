package com.tlcsdm.smc.tool;

import org.junit.jupiter.api.Test;

import cn.hutool.core.util.HexUtil;

/**
 * 进制转换测试
 */
public class HexText {

	@Test
	public void hex1() {
		System.out.println(HexUtil.toHex(11));
		System.out.println(HexUtil.hexToLong("A"));
		System.out.println(HexUtil.hexToLong("11"));
		System.out.println("0x" + String.format("%08x", 10).toUpperCase() + "UL");
	}

}
