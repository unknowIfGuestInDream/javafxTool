/*
 * Copyright (c) 2025 unknowIfGuestInDream.
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

package com.tlcsdm.core.util.jaxb.apn;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * @author unknowIfGuestInDream
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Toolchain {
    //Renesas	Renesas toolchain
    //IAR	IAR toolchain
    //LLVM	LLVM toolchain
    //GNU	Arm GNU toolchain
    @XmlElement(name = "brand", required = true)
    protected String brand;
    // Renesas	CCRL 	Renesas CCRL compiler
    //	        CCRX	Renesas CCRX compiler
    //	        CCRH	Renesas CCRH compiler
    // IAR	    ICCRL	IAR Embedded for Renesas RL78
    //	        ICCRX	IAR Embedded for Renesas RX
    // LLVM	    LLVMRL78	LLVM Embedded Toolchain for Renesas RL78
    //	        LLVMRX	LLVM Embedded Toolchain for Renesas RX
    //	        LLVMARM	LLVM Embedded Toolchain for Arm
    //	        LLVMRISCV	LLVM for Renesas RISC-V
    // GNU	    GUNARM	GNU Arm Embedded
    @XmlElement(name = "product", required = true)
    protected String product;
    @XmlElement(name = "version", required = true)
    protected String version;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
