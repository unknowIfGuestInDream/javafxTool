package com.tlcsdm.smc.tool;

import org.junit.jupiter.api.Test;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;

/**
 * @author: unknowIfGuestInDream
 * @date: 2022/12/7 18:56
 */
public class CompareTest {

	@Test
	public void CompareTest1() {
		String startDate = "2022-12-07 00:00:00";
		String submit = "2022-12-15 12:00:00";
		Assert.isTrue(startDate.compareTo(submit) == -1);
	}

	@Test
	public void resource2() {
		System.out.println(ResourceUtil.getResource("u2a_DTS_Transfer_request_Table.xlsx").getPath());
	}
}
