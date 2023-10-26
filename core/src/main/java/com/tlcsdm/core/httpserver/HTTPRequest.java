/**
 *  Copyright 2006-2012 Michael Vorburger (http://www.vorburger.ch)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*******************************************************************************
 * Copyright (c) 2006-2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package com.tlcsdm.core.httpserver;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP Request.
 * @author vorburger
 */
class HTTPRequest {

    String method;
    String URI;
    String HTTPVersion;
    Map<String, String> headers;
    String body;

    public HTTPRequest() {
        headers = new HashMap<String, String>();
    }

    public String getMethod() {
        return method;
    }

    public String getURI() {
        return URI;
    }

    /**
     * Version of HTTP that the client making this request can understand
     * @return HTTP Version, including "HTTP" prefix, so e.g. "HTTP/1.1" 
     */
    public String getHTTPVersion() {
        return HTTPVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    enum Method {
        OPTIONS("OPTIONS"), GET("GET"), HEAD("HEAD"), POST("POST"), PUT("PUT"), DELETE("DELETE"), TRACE("TRACE"),
        CONNECT("CONNECT");

        private final String method;

        Method(String method) {
            this.method = method;
        }

        public String toString() {
            return method;
        }
    }

    enum Version {
        HTTP10("HTTP/1.0"), HTTP11("HTTP/1.1");

        private final String version;

        Version(String version) {
            this.version = version;
        }

        public String toString() {
            return version;
        }
    }

    enum Header {
        Accept("Accept"), AcceptCharset("Accept-Charset"), AcceptEncoding("Accept-Encoding"),
        AcceptLanguage("Accept-Language"), Authorization("Authorization"), Expect("Expect"), From("From"),
        IfMatch("If-Match"), IfModifiedSince("If-Modified-Since"), IfNoneMatch("If-None-Match"), IfRange("If-Range"),
        IfUnmodifiedSince("If-Unmodified-Since"), MaxForwards("Max-Forwards"),
        ProxyAuthorization("Proxy-Authorization"), Range("Range"), Referer("Referer"), TE("TE"),
        UserAgent("User-Agent");

        private final String header;

        Header(String header) {
            this.header = header;
        }

        public String toString() {
            return header;
        }

    }
}
