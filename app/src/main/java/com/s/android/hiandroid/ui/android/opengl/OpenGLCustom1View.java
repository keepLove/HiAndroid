package com.s.android.hiandroid.ui.android.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author Administrator
 * @date 2019/10/15
 */
public class OpenGLCustom1View extends GLSurfaceView implements GLSurfaceView.Renderer {

    Triangle mTriangle;

    public OpenGLCustom1View(Context context) {
        this(context, null);
    }

    public OpenGLCustom1View(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        //设置渲染器
        setRenderer(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // rgba
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        mTriangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //GL视口
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除颜色缓存和深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mTriangle.draw();
    }
}
