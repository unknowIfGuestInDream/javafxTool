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

package com.tlcsdm.core.javafx.controller;

import com.tlcsdm.core.javafx.stage.MediaWallpaperStage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * 　@author secret
 */
public class MediaWallpaperController extends BaseController {
    private Dimension screenSize;
    private static MediaWallpaperController instance;
    private Stage stage;
    @FXML
    private FlowPane rootFlowPane;
    @FXML
    private MediaView mediaView;

    public static MediaWallpaperController getInstance() {
        return instance;
    }

    @FXML
    private void initialize() {
        instance = this;
        Platform.runLater(() -> {
            stage = (Stage) rootFlowPane.getScene().getWindow();
        });
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕
        //String mediaWallpaperPathConf = ConfigPropertiesUtil.get(ConfigEnum.MEDIAWALLPAPERPATH.getKey());
        String mediaWallpaperPathConf = "";
        initMediaView();
        if (!StringUtils.isEmpty(mediaWallpaperPathConf)) {
//            setMedia("file:///E:/upload/secretBlog/media/20200520221446179/20200520221446184.mp4");
            setMedia(mediaWallpaperPathConf);
//            setMedia("http://secretopen.gitee.io/secret-performance-desktop/media/test.mp4");
        }
    }

    private void initMediaView() {
        mediaView.setFitWidth(screenSize.getWidth());
        mediaView.setFitHeight(screenSize.getHeight());
        mediaView.setPreserveRatio(false);
    }

    public void setMedia(String path) {
        Media media = new Media(path);
        //创建媒体播放器
        MediaPlayer mPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mPlayer);
        //设置音量
        mPlayer.setVolume(0);
        //自动循环播放
        mPlayer.setAutoPlay(true);
        mPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    @Override
    public void show() {
        if (!stage.isShowing()) {//没有显示
            MediaWallpaperStage.getInstance().show();
            //String mediaWallpaperPathConf = ConfigPropertiesUtil.get(ConfigEnum.MEDIAWALLPAPERPATH.getKey());
            String mediaWallpaperPathConf = "";
            if (!StringUtils.isEmpty(mediaWallpaperPathConf)) {
                setMedia(mediaWallpaperPathConf);
            }
        }
    }

    @Override
    public void close() {
        MediaWallpaperStage.getInstance().close();
        //释放视频资源
        mediaView.getMediaPlayer().dispose();
    }
}
