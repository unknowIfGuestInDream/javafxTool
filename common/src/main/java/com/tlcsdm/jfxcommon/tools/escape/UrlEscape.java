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

package com.tlcsdm.jfxcommon.tools.escape;

import com.tlcsdm.core.javafx.helper.ImageViewHelper;
import com.tlcsdm.jfxcommon.util.I18nUtils;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * url 转义工具.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.1
 */
public class UrlEscape extends AbstractEscape {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public Node getPanel(Stage stage) {
        Node node = super.getPanel(stage);
        tipArea.setText("""
            1	(空格)	%20
            2	+	%2B
            3	&	%26
            4	=	%3D
            5	<	%3C
            6	>	%3E
            7	"	%22
            8	#	%23
            9	,	%2C
            10	%	%25
            11	{	%7B
            12	}	%7D
            13	|	%7C
            14	\\	%5C
            15	^	%5E
            16	~	%7E
            17	[	%5B
            18	]	%5D
            19	`	%60
            20	;	%3B
            21	/	%2F
            22	?	%3F
            23	:	%3A
            24	@	%40
            25	$	%24
            """);
        return node;
    }

    @Override
    protected String escape(String original) {
        return URLEncoder.encode(original, StandardCharsets.UTF_8);
    }

    @Override
    protected String unescape(String original) {
        return URLDecoder.decode(original, StandardCharsets.UTF_8);
    }

    @Override
    public ImageView getSampleImageIcon() {
        return ImageViewHelper.get("url");
    }

    @Override
    public String getSampleId() {
        return "urlEscape";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("common.tool.urlEscape.sampleName");
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("common.tool.urlEscape.sampleDesc");
    }

}
