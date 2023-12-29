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

package com.tlcsdm.core.logging.logback;

import ch.qos.logback.classic.spi.ConfiguratorRank;
import ch.qos.logback.classic.util.DefaultJoranConfigurator;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.joran.spi.JoranException;

import java.net.URL;

/**
 * Extended version of the Logback {@link DefaultJoranConfigurator}.
 *
 * @author unknowIfGuestInDream
 */
@ConfiguratorRank(ConfiguratorRank.CUSTOM_HIGH_PRIORITY)
public class CoreConfigurator extends DefaultJoranConfigurator {

    public CoreConfigurator() {
        super();
    }

    @Override
    public void configureByResource(URL url) throws JoranException {
        if (url == null) {
            throw new IllegalArgumentException("URL argument cannot be null");
        } else {
            String urlString = url.toString();
            if (urlString.endsWith("xml")) {
                CoreJoranConfigurator configurator = new CoreJoranConfigurator();
                configurator.setContext(this.context);
                configurator.doConfigure(url);
            } else {
                throw new LogbackException(
                    "Unexpected filename extension of file [" + url.toString() + "]. Should be .xml");
            }
        }
    }
}
