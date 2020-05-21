package com.s.android.hiandroid.ui.android.opengl;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author Administrator
 * @date 2019/10/15
 */
public class Triangle {

    // 数组中每个顶点的坐标数
    static final int COORDS_PER_VERTEX = 3;
    static float sCoo[] = {   //以逆时针顺序
            0.0f, 0.0f, 0.0f, // 顶部
            -1.0f, -1.0f, 0.0f, // 左下
            1.0f, -1.0f, 0.0f  // 右下
    };
    private final String vertexShaderCode =//顶点着色代码
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";
    private final String fragmentShaderCode =//片元着色代码
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    private final int mProgram;
    private final int vertexCount = sCoo.length / COORDS_PER_VERTEX;//顶点个数
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 3*4=12
    // 颜色，rgba
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};
    private FloatBuffer vertexBuffer;//顶点缓冲
    private int mPositionHandle;//位置句柄
    private int mColorHandle;//颜色句柄

    public Triangle() {
        //初始化顶点字节缓冲区
        ByteBuffer bb = ByteBuffer.allocateDirect(sCoo.length * 4);//每个浮点数:坐标个数* 4字节
        bb.order(ByteOrder.nativeOrder());//使用本机硬件设备的字节顺序
        vertexBuffer = bb.asFloatBuffer();// 从字节缓冲区创建浮点缓冲区
        vertexBuffer.put(sCoo);// 将坐标添加到FloatBuffer
        vertexBuffer.position(0);//设置缓冲区以读取第一个坐标
        int vertexShader = loadShader(
                GLES20.GL_VERTEX_SHADER,//顶点着色
                vertexShaderCode);
        int fragmentShader = loadShader
                (GLES20.GL_FRAGMENT_SHADER,//片元着色
                        fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();//创建空的OpenGL ES 程序
        GLES20.glAttachShader(mProgram, vertexShader);//加入顶点着色器
        GLES20.glAttachShader(mProgram, fragmentShader);//加入片元着色器
        GLES20.glLinkProgram(mProgram);//创建可执行的OpenGL ES项目
    }

    /**
     * 加载作色器
     *
     * @param type       顶点着色 {@link GLES20#GL_VERTEX_SHADER}
     *                   片元着色 {@link GLES20#GL_FRAGMENT_SHADER}
     * @param shaderCode 着色代码
     * @return 作色器
     */
    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);//创建着色器
        GLES20.glShaderSource(shader, shaderCode);//添加着色器源代码
        GLES20.glCompileShader(shader);//编译
        return shader;
    }

    public void draw() {
        // 将程序添加到OpenGL ES环境中
        GLES20.glUseProgram(mProgram);
        //获取顶点着色器的vPosition成员的句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角坐标数据
        GLES20.glVertexAttribPointer(
                mPositionHandle,//int indx, 索引
                COORDS_PER_VERTEX,//int size,大小
                GLES20.GL_FLOAT,//int type,类型
                false,//boolean normalized,//是否标准化
                vertexStride,// int stride,//跨度
                vertexBuffer);// java.nio.Buffer ptr//缓冲
        // 获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        //为三角形设置颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        //禁用顶点数组:
        //禁用index指定的通用顶点属性数组。
        // 默认情况下，禁用所有客户端功能，包括所有通用顶点属性数组。
        // 如果启用，将访问通用顶点属性数组中的值，
        // 并在调用顶点数组命令（如glDrawArrays或glDrawElements）时用于呈现
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
