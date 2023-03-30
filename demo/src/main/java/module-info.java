/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
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

import com.tlcsdm.frame.service.CenterPanelService;
import com.tlcsdm.frame.service.FXSamplerProject;
import com.tlcsdm.frame.service.MenubarConfigration;
import com.tlcsdm.login.service.LoginCheck;

module com.tlcsdm.demo {

    requires java.desktop;
    requires org.controlsfx.controls;
    requires com.tlcsdm.frame;
    requires com.tlcsdm.login;
    requires com.tlcsdm.core;

    exports com.tlcsdm.demo.samples to javafx.graphics;
    exports com.tlcsdm.demo.samples.actions to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.button to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.checked to com.tlcsdm.frame, javafx.graphics;
    exports com.tlcsdm.demo.samples.dialogs to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.propertysheet to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.tablefilter to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.tableview to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.tableview2 to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.textfields to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.spreadsheet to com.tlcsdm.frame;

    opens com.tlcsdm.demo.samples;
    opens com.tlcsdm.demo.samples.dialogs;
    opens com.tlcsdm.demo.samples.actions to org.controlsfx.controls, javafx.graphics;
    opens com.tlcsdm.demo.samples.tableview to javafx.base;
    opens com.tlcsdm.demo.samples.spreadsheet to javafx.graphics;
    opens com.tlcsdm.demo.samples.button to javafx.graphics;
    opens com.tlcsdm.demo.samples.textfields to javafx.graphics;
    opens com.tlcsdm.demo to javafx.graphics;

    provides FXSamplerProject with com.tlcsdm.demo.ControlsFXSamplerProject;
    provides MenubarConfigration with com.tlcsdm.demo.FXMenubarConfigration;
    provides CenterPanelService with com.tlcsdm.demo.FXCenterPanelService;
    provides LoginCheck with com.tlcsdm.demo.ControlsLoginCheck;

}