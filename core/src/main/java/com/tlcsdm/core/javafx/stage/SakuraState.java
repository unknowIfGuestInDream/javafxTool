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

package com.tlcsdm.core.javafx.stage;

import com.tlcsdm.core.javafx.factory.SingletonFactory;
import com.tlcsdm.core.javafx.util.OSUtil;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 樱花特效.
 *
 * @author unknowIfGuestInDream
 */
public class SakuraState extends BaseStage {
    private static SakuraState instance = null;
    private static Stage mainStage;

    private static double FPS = 30.0;
    private final String title = "sakura-desktop";
    private Timeline timeLine;
    GraphicsContext gc;
    //x轴
    int[] xx = new int[100];
    //yz轴
    int[] yy = new int[100];
    //大小
    double[] size = new double[100];
    //初始化角度
    int[] r = new int[100];
    //初始化角度增量
    int[] rv = new int[100];
    //x轴移动速度
    int[] vx = new int[100];
    //y轴下落速度
    int[] vy = new int[100];
    //每个樱花图片下标
    int[] imageIndex = new int[100];
    List<Image> imageList = new ArrayList<>();
    private Dimension screenSize;

    //动画事件
    private final EventHandler<ActionEvent> eventHandler = e -> {
        // 刷新操作
        gc.clearRect(0, 0, screenSize.getWidth(), screenSize.getHeight());
        sakura(gc);
    };

    /**
     * 调用单例工厂.
     */
    public static SakuraState getInstance() {
        if (instance == null) {
            instance = SingletonFactory.getWeakInstace(SakuraState.class);
        }
        return instance;
    }

    public void start() {
        Stage stage = getStage();
        mainStage = new Stage();
        mainStage.initOwner(stage);
        mainStage.setTitle(title);
        //透明窗口
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.setX(0);
        mainStage.setY(0);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-fill: null;-fx-background-color: rgba(0,0,0,0)");
        Canvas canvas = new Canvas(screenSize.getWidth(), screenSize.getHeight());
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        scene.setFill(null);
        mainStage.setScene(scene);
        //关闭自由调整大小
        mainStage.setResizable(false);
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> show());
        stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> close());
        //置于图标下层
        OSUtil.setWinIconAfter(title);

        //初始化樱花坐标，大小
        for (int i = 0; i < 100; ++i) {
            this.xx[i] = (int) (Math.random() * screenSize.getWidth());
            this.yy[i] = (int) (Math.random() * screenSize.getHeight());
            this.size[i] = (int) (Math.random() * 10);
            this.vx[i] = 5 - (int) (Math.random() * 10);
            //下落速度 [1,11)
            this.vy[i] = 1 + (int) (Math.random() * 10);
            this.imageIndex[i] = (int) (Math.random() * 3);
            //初始化角度 [-360 ,360)
            this.r[i] = 360 - (int) (Math.random() * 720);
            //角度改变 [-5,5)
            this.rv[i] = 5 - (int) (Math.random() * 10);
        }
        //初始化樱花图片
        for (int i = 1; i < 4; i++) {
            URL sakuraUrl = getClass().getResource("/com/tlcsdm/core/static/graphic/sakura" + i + ".png");
            String sakuraUrlStr = java.net.URLDecoder.decode(String.valueOf(sakuraUrl), StandardCharsets.UTF_8);
            Image image = new Image(sakuraUrlStr);
            imageList.add(image);
        }
        // 获取画板对象
        gc = canvas.getGraphicsContext2D();
        // 创建时间轴
        timeLine = new Timeline();
        // 获取时间轴的帧列表
        ObservableList<KeyFrame> keyFrames = timeLine.getKeyFrames();
        // 添加关键帧
        keyFrames.add(new KeyFrame(Duration.millis(1000 / FPS), eventHandler));
        // 设置时间轴播放次数为无限
        timeLine.setCycleCount(Timeline.INDEFINITE);
        // 播放时间轴
        timeLine.play();
    }

    public void sakura(GraphicsContext gc) {
        // 保存现场
        gc.save();
        for (int i = 0; i < 100; ++i) {
            //x轴随机左右移动
            this.xx[i] = xx[i] + vx[i];
            //y轴下落
            this.yy[i] += vy[i];
            //超过屏幕重新计算部分初始值
            if (this.yy[i] > screenSize.height) {
                this.xx[i] = (int) (Math.random() * screenSize.getWidth());
                this.yy[i] = 0;
                this.vy[i] = 1 + (int) (Math.random() * 10);
                this.r[i] = 360 - (int) (Math.random() * 720);
                this.size[i] = (int) (Math.random() * 10);
            }
            //旋转角度
            r[i] += rv[i];
            //大小
            size[i] += 0.02;

            Rotate rotate = new Rotate(r[i], xx[i] + size[i] / 2.0, yy[i] + size[i] / 2.0);
            gc.setTransform(rotate.getMxx(), rotate.getMyx(), rotate.getMxy(), rotate.getMyy(), rotate.getTx(), rotate.getTy());
            gc.drawImage(imageList.get(imageIndex[i]), xx[i], yy[i], size[i], size[i]);
        }
        // 恢复现场
        gc.restore();
    }

    @Override
    public void close() {
        if (mainStage != null) {
            mainStage.close();
            //隐藏就停止动画，节省性能
            timeLine.stop();
        }
    }

    @Override
    public void setFps(double fps) {
        FPS = fps;
        timeLine.getKeyFrames().set(0, new KeyFrame(Duration.millis(1000 / FPS), eventHandler));
        stopTimer();
        startTimer();
    }

    protected void startTimer() {
        if (timeLine.getStatus() != Animation.Status.RUNNING) {
            timeLine.play();
        }
    }

    protected void stopTimer() {
        if (timeLine.getStatus() != Animation.Status.STOPPED) {
            timeLine.stop();
        }
    }

    @Override
    public void show() {
        init();
        if (!mainStage.isShowing()) {
            mainStage.show();
            timeLine.play();
            OSUtil.setWinIconAfter(title);
        }
    }

    @Override
    public void init() {
        if (mainStage == null) {
            getInstance().start();
        }
    }

    public static double getFPS() {
        return FPS;
    }

    public static void setFPS(double FPS) {
        SakuraState.FPS = FPS;
    }

}
