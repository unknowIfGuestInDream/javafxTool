/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static jssc.SerialPort.BAUDRATE_9600;
import static jssc.SerialPort.DATABITS_8;
import static jssc.SerialPort.PARITY_NONE;
import static jssc.SerialPort.STOPBITS_1;

/**
 * @author unknowIfGuestInDream
 */
@Disabled
class JsscTest {

    /**
     * List Ports.
     */
    @Test
    void list() {
        for (String port : SerialPortList.getPortNames()) {
            System.out.println(port);
        }
    }

    /**
     * Write Data.
     */
    @Test
    void write() throws SerialPortException {
        SerialPort port = new SerialPort("COM1");
        port.openPort();
        port.setParams(BAUDRATE_9600, DATABITS_8, STOPBITS_1, PARITY_NONE);
        // port.setParams(9600, 8, 1, 0); // alternate technique
        port.writeBytes("Testing serial from Java".getBytes());
        port.closePort();
    }

    /**
     * Read Data.
     */
    @Test
    void read() throws SerialPortException {
        SerialPort port = new SerialPort("COM1");
        port.openPort();
        port.setParams(BAUDRATE_9600, DATABITS_8, STOPBITS_1, PARITY_NONE);
        // port.setParams(9600, 8, 1, 0); // alternate technique
        byte[] buffer = port.readBytes(10);/* read first 10 bytes */
        System.out.println(Arrays.toString(buffer));
        port.closePort();
    }

    /**
     * Event Listener.
     */
    @Test
    void listener() throws SerialPortException {
        SerialPort port = new SerialPort("COM1");
        port.openPort();
        port.setParams(BAUDRATE_9600, DATABITS_8, STOPBITS_1, PARITY_NONE);
        // port.setParams(9600, 8, 1, 0); // alternate technique
        int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
        port.setEventsMask(mask);
        port.addEventListener(new MyPortListener(port) /* defined below */);
    }

    /**
     * Override Native Library Path.
     */
    @Test
    void jvm() {
        System.out.println(System.getProperty("jssc.boot.library.path"));
        // System.setProperty("jssc.boot.library.path", "/path/to/mylibs/");
    }

    /**
     * In this class must implement the method serialEvent, through it we learn about
     * events that happened to our port. But we will not report on all events but only
     * those that we put in the mask. In this case the arrival of the data and change the
     * status lines CTS and DSR
     */
    class MyPortListener implements SerialPortEventListener {
        SerialPort port;

        public MyPortListener(SerialPort port) {
            this.port = port;
        }

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR()) { // data is available
                // read data, if 10 bytes available
                if (event.getEventValue() == 10) {
                    try {
                        byte[] buffer = port.readBytes(10);
                    } catch (SerialPortException ex) {
                        System.out.println(ex);
                    }
                }
            } else if (event.isCTS()) { // CTS line has changed state
                if (event.getEventValue() == 1) { // line is ON
                    System.out.println("CTS - ON");
                } else {
                    System.out.println("CTS - OFF");
                }
            } else if (event.isDSR()) { // DSR line has changed state
                if (event.getEventValue() == 1) { // line is ON
                    System.out.println("DSR - ON");
                } else {
                    System.out.println("DSR - OFF");
                }
            }
        }
    }
}
