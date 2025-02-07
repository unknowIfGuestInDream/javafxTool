/*
 * Copyright (c) 2023 unknowIfGuestInDream.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of unknowIfGuestInDream, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tlcsdm.core.util;

import cn.hutool.core.io.resource.ResourceUtil;
import com.tlcsdm.core.util.jaxb.apn.APN;
import com.tlcsdm.core.util.jaxb.apn.Application;
import com.tlcsdm.core.util.jaxb.apn.Board;
import com.tlcsdm.core.util.jaxb.apn.Circuit;
import com.tlcsdm.core.util.jaxb.apn.Dimming;
import com.tlcsdm.core.util.jaxb.apn.Power;
import com.tlcsdm.core.util.jaxb.board.BoardInfo;
import com.tlcsdm.core.util.jaxb.board.BoardInfos;
import com.tlcsdm.core.util.jaxb.board.Compiler;
import com.tlcsdm.core.util.jaxb.mdf.BoardCircuit;
import com.tlcsdm.core.util.jaxb.mdf.Condition;
import com.tlcsdm.core.util.jaxb.mdf.Device;
import com.tlcsdm.core.util.jaxb.mdf.ParamCodeSetting;
import com.tlcsdm.core.util.jaxb.mdf.ParamUISetting;
import com.tlcsdm.core.util.jaxb.mdf.PowerControlSettings;
import com.tlcsdm.core.util.jaxb.mdf.PowerControlToolchain;
import com.tlcsdm.core.util.jaxb.mdf.PowerControlVariableSetting;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author unknowIfGuestInDream
 */
@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
public class JaxbTest {

    /**
     * Marshalling – 将 Java 对象转换为 XML
     */
    @Test
    void marshal() throws JAXBException {
        Book book = new Book();
        book.setId(1L);
        book.setName("Book2");
        book.setAuthor("Author1");
        book.setDate(new Date());
        JAXBContext context = JAXBContext.newInstance(Book.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.tlcsdm.com/abc.xsd");
        StringWriter stringWriter = new StringWriter();
        mar.marshal(book, stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * Unmarshalling – 将 XML 转换为 Java 对象
     */
    @Test
    void unmarshal() throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(Book.class);
        Book book = (Book) context.createUnmarshaller()
            .unmarshal(new FileReader("E:\\testPlace\\result\\jaxb\\book.xml"));
        System.out.println(book);
    }

    @Test
    void boardInfo() throws JAXBException {
        BoardInfo boardInfo = new BoardInfo();
        boardInfo.setId("RTK7RLG240P00000BJ");
        boardInfo.setName("RTK7RLG240P00000BJ");
        boardInfo.setFamily("RL78G24");
        boardInfo.setFullName("RL78/G24 DC/DC LED Control Evaluation Board");
        List<String> processorList = new ArrayList<>();
        processorList.add("CPU");
        processorList.add("FAA");
        boardInfo.setProcessorList(processorList);
        List<String> programList = new ArrayList<>();
        programList.add("DALI");
        boardInfo.setProgramList(programList);
        Device device = new Device();
        device.setName("RL78/G24 (R7F101GLG)");
        device.setLights("3");
        device.setClock("Internal osc. - 8MHz");
        device.setSampleVersion("1.00");
        boardInfo.setDevice(device);
        List<Compiler> compilerList = new ArrayList<>();
        compilerList.add(new Compiler("ccrl", "Renesas CC-RL"));
        compilerList.add(new Compiler("iar", "IAR RL78"));
        boardInfo.setCompilerList(compilerList);

        BoardInfos boardInfos = new BoardInfos();
        List<BoardInfo> boardInfoList = new ArrayList<>();
        boardInfoList.add(boardInfo);
        boardInfos.setBoardInfoList(boardInfoList);

        JAXBContext context = JAXBContext.newInstance(BoardInfos.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter stringWriter = new StringWriter();
        mar.marshal(boardInfos, stringWriter);
        System.out.println(stringWriter);
    }

    @Test
    void boardInfoRead() throws JAXBException, FileNotFoundException {
        BoardInfos boardInfos;
        FileReader fileReader = new FileReader(ResourceUtil.getResource("jaxb/boardinfo.xml").getFile());
        JAXBContext context = JAXBContext.newInstance(BoardInfos.class);
        boardInfos = (BoardInfos) context.createUnmarshaller()
            .unmarshal(fileReader);
        Assertions.assertEquals(1, boardInfos.getBoardInfoList().size());
        BoardInfo boardInfo = boardInfos.getBoardInfoList().get(0);
        Assertions.assertEquals("RTK7RLG240P00000BJ", boardInfo.getId());
        Assertions.assertEquals("RL78G24", boardInfo.getFamily());
    }

    @Test
    void powerControl() throws JAXBException {
        List<BoardCircuit> boardCircuitList = new ArrayList<>();
        BoardCircuit boardCircuit1 = new BoardCircuit();
        boardCircuit1.setBoardCircuitURL("boardCircuit1.png");
        boardCircuit1.setTooltip("LED Back converter circuit for constant current control 1");
        boardCircuit1.setJptooltip("LED Back converter circuit for constant current control 1");
        BoardCircuit boardCircuit2 = new BoardCircuit();
        boardCircuit2.setBoardCircuitURL("boardCircuit2.png");
        boardCircuit2.setTooltip("LED Back converter circuit for constant current control 2");
        boardCircuit2.setJptooltip("LED Back converter circuit for constant current control 2");
        boardCircuitList.add(boardCircuit1);
        boardCircuitList.add(boardCircuit2);

        List<ParamUISetting.Group> groupList = new ArrayList<>();
        ParamUISetting.Group group1 = new ParamUISetting.Group();
        group1.setId("common");
        group1.setName("Power Control Evaluation Parameters");
        group1.setJpName("Power Control Evaluation Parameters");
        ParamUISetting.Group group2 = new ParamUISetting.Group();
        group2.setId("simulation");
        group2.setName("Power Control Simulation Parameters");
        group2.setJpName("Power Control Simulation Parameters");
        groupList.add(group1);
        groupList.add(group2);

        List<ParamUISetting.Property> propertyList = new ArrayList<>();
        ParamUISetting.Property a1 = new ParamUISetting.Property();
        a1.setId("a1");
        a1.setDefaultS("65");
        a1.setType("textbox");
        a1.setGroupId("simulation");
        a1.setName("A1");
        a1.setJpName("A1");
        a1.setTooltipImageURL("a1.png");
        ParamUISetting.Property a2 = new ParamUISetting.Property();
        a2.setId("a2");
        a2.setDefaultS("33");
        a2.setType("textbox");
        a2.setGroupId("simulation");
        a2.setName("A2");
        a2.setJpName("A2");
        a2.setTooltipImageURL("a2.png");
        ParamUISetting.Property kp = new ParamUISetting.Property();
        kp.setId("kp");
        kp.setDefaultS("33");
        kp.setType("textbox");
        kp.setGroupId("simulation");
        kp.setName("Kp");
        kp.setJpName("Kp");
        kp.setTooltip("Kp tooltip");
        kp.setJptooltip("Kp jp tooltip");
        propertyList.add(a1);
        propertyList.add(a2);
        propertyList.add(kp);

        ParamUISetting paramUISetting = new ParamUISetting();
        paramUISetting.setSectionId("PI_RL78G24");
        paramUISetting.setGroupList(groupList);
        paramUISetting.setPropertyList(propertyList);

        List<ParamCodeSetting.Dir> dirList = new ArrayList<>();
        ParamCodeSetting.Dir dir1 = new ParamCodeSetting.Dir();
        dir1.setIncdir("CPU/FAA/APP");
        dir1.setOutput("APP");
        Condition condition3 = new Condition();
        condition3.setFormula("xx == CPU && xxx == FAA");
        dir1.setCondition(condition3);
        ParamCodeSetting.Dir dir2 = new ParamCodeSetting.Dir();
        dir2.setIncdir("doc");
        dirList.add(dir1);
        dirList.add(dir2);

        List<ParamCodeSetting.File> fileList = new ArrayList<>();
        ParamCodeSetting.File file1 = new ParamCodeSetting.File();
        file1.setIncfile("doc/rl78g24.md");
        file1.setOutput("rl78g24.md");
        ParamCodeSetting.File file2 = new ParamCodeSetting.File();
        file2.setIncfile("CPU/FAA/APP/comm.c");
        file2.setOutput("APP/comm.c");
        Condition condition4 = new Condition();
        condition4.setFormula("xx == CPU && xxx == FAA");
        file2.setCondition(condition4);
        fileList.add(file1);
        fileList.add(file2);

        ParamCodeSetting paramCodeSetting = new ParamCodeSetting();
        paramCodeSetting.setZipsource("RL78G24.zip");
        paramCodeSetting.setDirList(dirList);
        paramCodeSetting.setFileList(fileList);

        List<PowerControlVariableSetting.Variable> variableList1 = new ArrayList<>();
        PowerControlVariableSetting.Variable variable1 = new PowerControlVariableSetting.Variable();
        variable1.setName("_g_led1_duty");
        variable1.setBitSize(32);
        variable1.setDescription("variable1 desc");
        variable1.setRwPermission("rw");
        variable1.setDisplayName("_g_led1_duty");
        Condition condition5 = new Condition();
        condition5.setFormula("xx == CPU ");
        variable1.setCondition(condition5);
        PowerControlVariableSetting.Variable variable2 = new PowerControlVariableSetting.Variable();
        variable2.setName("_g_led2_duty");
        variable2.setBitSize(32);
        variable2.setDescription("variable2 desc");
        variable2.setRwPermission("rw");
        variable2.setDisplayName("_g_led2_duty");
        Condition condition6 = new Condition();
        condition6.setFormula("xx == CPU");
        variable2.setCondition(condition6);
        variableList1.add(variable1);
        variableList1.add(variable2);

        List<PowerControlVariableSetting.Variable> variableList2 = new ArrayList<>();
        PowerControlVariableSetting.Variable variable3 = new PowerControlVariableSetting.Variable();
        variable3.setName("_V_LEDControl_Ch1.[0]");
        variable3.setBitSize(32);
        variable3.setDescription("[Channel 1] Last A/D result for LED volume");
        variable3.setRwPermission("r");
        variable3.setDisplayName("_V_LEDControl_Ch1._N_LEDControl_TargetAd");
        Condition condition7 = new Condition();
        condition7.setFormula("xx == FAA");
        variable3.setCondition(condition7);
        variableList2.add(variable3);

        List<PowerControlVariableSetting.Tab> tabList = new ArrayList<>();
        PowerControlVariableSetting.Tab tab1 = new PowerControlVariableSetting.Tab();
        tab1.setName("Variable List");
        tab1.setJpName("Variable List");
        tab1.setVariableList(variableList1);
        PowerControlVariableSetting.Tab tab2 = new PowerControlVariableSetting.Tab();
        tab2.setName("SFR List");
        tab2.setJpName("SFR List");
        tab2.setVariableList(variableList2);
        Condition condition8 = new Condition();
        condition8.setFormula("xx == FAA");
        tab2.setCondition(condition8);
        tabList.add(tab1);
        tabList.add(tab2);

        PowerControlVariableSetting variableSetting = new PowerControlVariableSetting();
        variableSetting.setTabList(tabList);

        List<PowerControlToolchain.CommandParam> commandParamList = new ArrayList<>();
        PowerControlToolchain.CommandParam commandParam1 = new PowerControlToolchain.CommandParam();
        commandParam1.setFileType("c");
        List<String> paramList1 = new ArrayList<>();
        paramList1.add("-0");
        paramList1.add("-l");
        commandParam1.setParamList(paramList1);
        PowerControlToolchain.CommandParam commandParam2 = new PowerControlToolchain.CommandParam();
        commandParam2.setFileType("dsp");
        List<String> paramList2 = new ArrayList<>();
        paramList2.add("-0");
        paramList2.add("-l");
        commandParam2.setParamList(paramList2);
        Condition condition1 = new Condition();
        condition1.setAction("disable");
        condition1.setFormula("xxx = PI_MODE");
        commandParam2.setCondition(condition1);
        commandParamList.add(commandParam1);
        commandParamList.add(commandParam2);

        List<PowerControlToolchain.LinkParam> linkParamList = new ArrayList<>();
        PowerControlToolchain.LinkParam linkParam1 = new PowerControlToolchain.LinkParam();
        List<String> paramList3 = new ArrayList<>();
        paramList3.add("-ds");
        paramList3.add("-area");
        linkParam1.setParamList(paramList3);
        PowerControlToolchain.LinkParam linkParam2 = new PowerControlToolchain.LinkParam();
        List<String> paramList4 = new ArrayList<>();
        paramList4.add("-ds2");
        paramList4.add("-area");
        linkParam2.setParamList(paramList4);
        Condition condition2 = new Condition();
        condition2.setAction("disable");
        condition2.setFormula("xx = FAA");
        linkParam2.setCondition(condition2);
        linkParamList.add(linkParam1);
        linkParamList.add(linkParam2);

        PowerControlToolchain.Param param1 = new PowerControlToolchain.Param();
        param1.setCommandParamList(commandParamList);
        param1.setLinkParamList(linkParamList);

        List<PowerControlToolchain> toolchainList = new ArrayList<>();
        PowerControlToolchain toolchain1 = new PowerControlToolchain();
        toolchain1.setId("ccrl");
        toolchain1.setCompilerOpt("-o -cpu");
        toolchain1.setParam(param1);
        toolchainList.add(toolchain1);

        PowerControlSettings powerControlSettings = new PowerControlSettings();
        powerControlSettings.setName("name");
        powerControlSettings.setVersion("1.0.0");
        powerControlSettings.setRequireVersion("1.2.0");
        powerControlSettings.setBoardId("RTK7RLG240P00000BJ");
        powerControlSettings.setBoardName("RTK7RLG240P00000BJ");
        powerControlSettings.setBoardImageURL("board.png");
        powerControlSettings.setBoardCircuitList(boardCircuitList);
        powerControlSettings.setParamUISetting(paramUISetting);
        powerControlSettings.setParamCodeSetting(paramCodeSetting);
        powerControlSettings.setToolchain(toolchainList);
        powerControlSettings.setVariableSetting(variableSetting);

        JAXBContext context = JAXBContext.newInstance(PowerControlSettings.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter stringWriter = new StringWriter();
        mar.marshal(powerControlSettings, stringWriter);
        System.out.println(stringWriter);
    }

    @Test
    void apn() throws JAXBException {
        Board board = new Board();
        board.setId("RTK7RLG240P00000BJ");
        board.setName("RTK7RLG240P00000BJ");
        board.setJpName("RTK7RLG240P00000BJ");
        board.setPicture("APN_extend/board.png");
        com.tlcsdm.core.util.jaxb.apn.Device device = new com.tlcsdm.core.util.jaxb.apn.Device();
        device.setName("RL78/G24 (R7F101GLG)");
        device.setLights("3");
        device.setClock("Internal osc. - 8MHz");
        device.setSampleVersion("1.00");
        board.setDevice(device);

        List<Dimming> dimmingList1 = new ArrayList<>();
        Dimming dimming1 = new Dimming();
        dimming1.setId("dali");
        dimming1.setLights(3);
        dimming1.setProtocol("102+207+209");
        dimming1.setConfig("APN_extend/config_dali.mdf");
        Dimming dimming2 = new Dimming();
        dimming2.setId("dali");
        dimming2.setLights(3);
        dimming2.setProtocol("102+207");
        dimming2.setConfig("APN_extend/config_dali.mdf");
        dimmingList1.add(dimming2);

        List<Circuit> circuitList = new ArrayList<>();
        Circuit circuit1 = new Circuit();
        circuit1.setId("boardId");
        circuit1.setConfig("APN_extend/config_boardId.mdf");
        circuit1.setMonitor("APN_extend/monitor_boardId.xml");
        circuit1.setPicture("APN_extend/boardId.png");
        circuitList.add(circuit1);

        Power power1 = new Power();
        power1.setCircuitList(circuitList);

        List<Application> applicationList = new ArrayList<>();
        Application a1 = new Application();
        a1.setSrc("102+207+209/CCRL/CPU/Application");
        a1.setCompiler("CCRL");
        a1.setType("CPU");
        a1.setDimmingList(dimmingList1);
        a1.setPower(power1);

        applicationList.add(a1);

        APN apn = new APN();
        apn.setBoard(board);
        apn.setApplicationList(applicationList);

        JAXBContext context = JAXBContext.newInstance(APN.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter stringWriter = new StringWriter();
        mar.marshal(apn, stringWriter);
        System.out.println(stringWriter);
    }

}
