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

package com.tlcsdm.core.logging;

import cn.hutool.core.lang.caller.CallerUtil;
import cn.hutool.log.LogFactory;

/**
 * Wrapper for {@link cn.hutool.log.StaticLog}.
 *
 * @author unknowIfGuestInDream
 */
public class StaticLog {

    private static final String FQCN = StaticLog.class.getName();

    /**
     * @see cn.hutool.log.StaticLog#debug(String format, Object... arguments)
     */
    public static void debug(String format, Object... arguments) {
        LogFactory.get(CallerUtil.getCallerCaller()).debug(FQCN, null, format, arguments);
    }

    /**
     * @see cn.hutool.log.StaticLog#info(String format, Object... arguments)
     */
    public static void info(String format, Object... arguments) {
        LogFactory.get(CallerUtil.getCallerCaller()).info(FQCN, null, format, arguments);
    }

    /**
     * @see cn.hutool.log.StaticLog#warn(String format, Object... arguments)
     */
    public static void warn(String format, Object... arguments) {
        LogFactory.get(CallerUtil.getCallerCaller()).warn(FQCN, null, format, arguments);
    }

    /**
     * @see cn.hutool.log.StaticLog#error(String format, Object... arguments)
     */
    public static void error(String format, Object... arguments) {
        LogFactory.get(CallerUtil.getCallerCaller()).error(FQCN, null, format, arguments);
    }

    /**
     * @see cn.hutool.log.StaticLog#error(Throwable e)
     */
    public static void error(Throwable e) {
        LogFactory.get(CallerUtil.getCallerCaller()).error(FQCN, e, e.getMessage());
    }

    /**
     * @see cn.hutool.log.StaticLog#error(Throwable e, String format, Object... arguments)
     */
    public static void error(Throwable e, String format, Object... arguments) {
        LogFactory.get(CallerUtil.getCallerCaller()).error(FQCN, e, format, arguments);
    }
}
