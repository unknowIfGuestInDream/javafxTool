/**
 * Copyright (c) 2013, 2022, ControlsFX
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.tlcsdm.smc;

import com.tlcsdm.frame.FXSampler;

public class SmcSampler {

    public static void main(String[] args) {
//        System.out.println(System.getProperty("user.dir"));
//        String relativelyPath = System.getProperty("user.dir");
//        File file = new File(relativelyPath + "\\data\\demo");
//        File txt = new File("F:\\TM\\test.txt");
//
//        try {
//
//
//        FileWriter fw = new FileWriter(txt, true);
//        PrintWriter pw = new PrintWriter(fw);
//        pw.println(relativelyPath);
//        pw.flush();
//
//        fw.flush();
//        pw.close();
//        fw.close();
//        }catch (Exception e){
//
//        }
//
//        if (!file.exists()) {
//            try {
//                file.mkdirs();
//                new File(relativelyPath + "\\data\\demo\\setting.xml").createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        //直接启动应用
        FXSampler.main(args);

        //带有登录校验的启动
        //LoginFrame.main(args);
    }
}
