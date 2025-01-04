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

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;
import com.fazecast.jSerialComm.SerialPortPacketListener;

import java.io.InputStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author unknowIfGuestInDream
 */
@Disabled
class JSerialCommTest {

    @Test
    void list() {
        for (SerialPort port : SerialPort.getCommPorts()) {
            System.out.println(port.getSystemPortName());
            System.out.println(port.getDescriptivePortName());
        }
    }

    @Test
    void noBlockRead() {
        SerialPort comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        try {
            while (true) {
                while (comPort.bytesAvailable() == 0)
                    Thread.sleep(20);

                byte[] readBuffer = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
                System.out.println("Read " + numRead + " bytes.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        comPort.closePort();
        //If you were already in a different mode of operation, you can enable non-blocking/polling mode by calling the
        // following method at any point in time:
        //comPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
    }

    @Test
    void blockRead() {
        SerialPort comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
        try {
            while (true) {
                byte[] readBuffer = new byte[1024];
                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
                System.out.println("Read " + numRead + " bytes.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        comPort.closePort();
    }

    @Test
    void streamInterfacing() {
        SerialPort comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        InputStream in = comPort.getInputStream();
        try {
            for (int j = 0; j < 1000; ++j)
                System.out.print((char) in.read());
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        comPort.closePort();
    }

    @Test
    void eventRead() {
        SerialPort comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        //In this case, your callback will be triggered whenever there is any data available to be read over the serial port.
        // Once your callback is triggered, you can optionally call bytesAvailable() to determine how much data is available
        // to read, and you must actually read the data using any of the read() or readBytes() methods.
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] newData = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(newData, newData.length);
                System.out.println("Read " + numRead + " bytes.");
            }
        });

        //In this case, your callback will be triggered whenever all data you have written using any of the write() or
        // writeBytes() methods has actually been transmitted.
        //Note that this mode of operation is only functional on Windows operating systems due to limitations in
        // Linux's handling of serial writes.
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_WRITTEN;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_WRITTEN)
                    System.out.println("All bytes were successfully transmitted!");
            }
        });

        //In this case, your callback will be triggered whenever some amount of data has actually been read from the serial port.
        // This raw data will be returned to you within your own callback, so there is no further need to read directly from the serial port.
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                byte[] newData = event.getReceivedData();
                System.out.println("Received data of size: " + newData.length);
                for (int i = 0; i < newData.length; ++i)
                    System.out.print((char) newData[i]);
                System.out.println("\n");
            }
        });

        //In this case, your callback will be triggered whenever a set fixed amount of data has been read from the serial port.
        // This raw data will be returned to you within your own callback, so there is no further need to read directly
        // from the serial port.
        PacketListener listener = new PacketListener();
        comPort.addDataListener(listener);

        //In this case, your callback will be triggered whenever a message has been received based on a custom delimiter
        // that you specify. This delimiter can contain one or more consecutive bytes and can indicate either the beginning
        // or the end of a data packet. The raw data will be returned to you within your own callback, so there is no further
        // need to read directly from the serial port.
        MessageListener listener1 = new MessageListener();
        comPort.addDataListener(listener1);

        comPort.removeDataListener();
        comPort.closePort();
    }

    private final class PacketListener implements SerialPortPacketListener {
        @Override
        public int getListeningEvents() {
            return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
        }

        @Override
        public int getPacketSize() {
            return 100;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {
            byte[] newData = event.getReceivedData();
            System.out.println("Received data of size: " + newData.length);
            for (int i = 0; i < newData.length; ++i)
                System.out.print((char) newData[i]);
            System.out.println("\n");
        }
    }

    private final class MessageListener implements SerialPortMessageListener {
        @Override
        public int getListeningEvents() {
            return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
        }

        @Override
        public byte[] getMessageDelimiter() {
            return new byte[]{(byte) 0x0B, (byte) 0x65};
        }

        @Override
        public boolean delimiterIndicatesEndOfMessage() {
            return true;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {
            byte[] delimitedMessage = event.getReceivedData();
            System.out.println("Received the following delimited message: " + delimitedMessage);
        }
    }
}
