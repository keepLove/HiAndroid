package com.s.android.hiandroid.ui.java.reflect

import android.annotation.SuppressLint
import android.os.Bundle
import com.s.android.hiandroid.R
import com.s.android.hiandroid.ui.common.BaseActivity
import com.s.android.hiandroid.ui.common.startBrowser
import kotlinx.android.synthetic.main.activity_reflect.*

class ReflectActivity : BaseActivity() {

    override fun getLayoutResID(): Int? {
        return R.layout.activity_reflect
    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        bt_reference1.setOnClickListener {
            startBrowser("https://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650825931&idx=1&sn=b61ed2ad2665cbc3a89351b161223769&chksm=80b7b055b7c03943f10f1e8b3697d3d33bbfc98d38e40c9ecbc6a3ab34f700a065858f8c8ce9&scene=38#wechat_redirect")
        }
        bt_reference2.setOnClickListener {
            startBrowser("https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650242963&idx=1&sn=708e4fa01823b7844494ecb276a3733f&chksm=88638efcbf1407eaf0b5438f824a42a1890ab6a623108990cbbc1d424594ee08e9ff973ff70f&scene=38#wechat_redirect")
        }
        tv_content.text = """

    /**
     * 获取 目标类型的Class对象
     */
    public void getRelectClass() {
        // 方式1：Object.getClass()  Object类中的getClass()返回一个Class类型的实例
        Boolean carson = true;
        Class<?> classType = carson.getClass();
        System.out.println(classType);// 输出结果：class java.lang.Boolean

        // 方式2：T.class 语法。 T = 任意Java类型
        Class<?> classType2 = Boolean.class;
        System.out.println(classType2);// 输出结果：class java.lang.Boolean
        // 注：Class对象表示的是一个类型，而这个类型未必一定是类
        // 如，int不是类，但int.class是一个Class类型的对象

        // 方式3：static method Class.forName
        try {
            Class<?> classType3 = Class.forName("java.lang.Boolean");
            System.out.println(classType3);// 输出结果：class java.lang.Boolean
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 方式4：TYPE语法
        Class<?> classType4 = Boolean.TYPE;
        System.out.println(classType4); // 输出结果：boolean
    }

    /**
     * 通过 Class 对象分别获取Constructor类对象、Method类对象 & Field 类对象
     * 特别注意：
     * 1. 不带 "Declared"的方法支持取出包括继承、公有（Public）的构造函数、方法、属性。
     * 2. 带 "Declared"的方法是支持取出包括公共（Public）、保护（Protected）、默认（包）访问和私有（Private），但不包括继承的构造函数、方法、属性。
     */
    public void getConstructor() {
        Class<?> classType = Boolean.class;

        // 获取类的构造函数(传入构造函数的参数类型)
        try {
            // a. 获取指定的构造函数 （公共 / 继承）
            Constructor<?> constructor = classType.getConstructor(String.class);
            // b. 获取所有的构造函数（公共 / 继承）
            Constructor<?>[] constructors = classType.getConstructors();
            // c. 获取指定的构造函数 （不包括继承）
            Constructor<?> declaredConstructor = classType.getDeclaredConstructor(boolean.class);
            // d. 获取所有的构造函数（ 不包括继承）
            Constructor<?>[] declaredConstructors = classType.getDeclaredConstructors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * constructor 使用方法
     */
    public void getConstructorPlay() {
        Class<ReflectClass> classClass = ReflectClass.class;
        try {
            Constructor<ReflectClass> constructor = classClass.getConstructor(String.class);
            // 获取构造器名
            String constructorName = constructor.getName();
            System.out.println("获取构造器名 =====  " + constructorName);
            // 获取一个用于描述类中定义的构造器的Class对象
            Class<ReflectClass> declaringClass = constructor.getDeclaringClass();
            System.out.println("获取一个用于描述类中定义的构造器的Class对象 ====== " + declaringClass);
            // 返回整型数值，用不同的位开关描述访问修饰符的使用状况
            int modifiers = constructor.getModifiers();
            System.out.println("返回整型数值，用不同的位开关描述访问修饰符的使用状况 ===== " + Modifier.toString(modifiers));
            // 获取描述方法抛出的异常类型的Class对象数组
            Class<?>[] exceptionTypes = constructor.getExceptionTypes();
            System.out.println("获取描述方法抛出的异常类型的Class对象数组  ===== " + Arrays.toString(exceptionTypes));
            //  获取一个用于描述参数类型的Class对象数组
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            System.out.println("获取一个用于描述参数类型的Class对象数组 ==== " + Arrays.toString(parameterTypes));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过 Class 对象分别获取Field 类对象
     */
    public void getField() {
        Class<?> classType = ReflectClass.class;
        // 获取类的属性（传入属性名）
        try {
            // a. 获取指定的属性（公共 / 继承）
            Field field = classType.getField("testPublicField");
            // b. 获取所有的属性（公共 / 继承）
            Field[] fields = classType.getFields();
            // c. 获取指定的所有属性 （不包括继承）
            Field testPublicField = classType.getDeclaredField("testPublicField");
            // d. 获取所有的所有属性 （不包括继承）
            Field[] declaredFields = classType.getDeclaredFields();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * field 使用方法
     */
    public void getFieldPlay() {
        Class<ReflectClass> classClass = ReflectClass.class;
        try {
            ReflectClass reflectClass = classClass.newInstance();
            Field testPublicField = classClass.getField("testPublicField");
            // 获取属性名
            String constructorName = testPublicField.getName();
            System.out.println("获取属性名 =====  " + constructorName);
            // 获取属性类型的Class类型对象
            Class<?> declaringClass = testPublicField.getDeclaringClass();
            System.out.println("获取属性类型的Class类型对象 ====== " + declaringClass);
            // 返回整型数值，用不同的位开关描述访问修饰符的使用状况
            int modifiers = testPublicField.getModifiers();
            System.out.println("返回整型数值，用不同的位开关描述访问修饰符的使用状况 ===== " + Modifier.toString(modifiers));
            // 获取属性类型的Class类型对象
            Class<?> type = testPublicField.getType();
            System.out.println("获取属性类型的Class类型对象 ==== " + type);
            // 返回指定对象上 此属性的值
            Object oldGet = testPublicField.get(reflectClass);
            System.out.println("返回指定对象上 此属性的值 ==== " + oldGet);
            // 设置 指定对象上此属性的值为value
            testPublicField.set(reflectClass, "resetting");
            System.out.println("设置 指定对象上此属性的值为value=resetting");
            // 返回指定对象上 此属性的值
            Object get = testPublicField.get(reflectClass);
            System.out.println("返回指定对象上 此属性的值 ==== " + get);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过 Class 对象分别获取 Method 类对象
     */
    public void getMethod() {
        Class<?> classType = ReflectClass.class;
        // 获取类的方法（传入方法名 & 参数类型）
        try {
            // a. 获取指定的方法（公共 / 继承）
            Method testPublicMethod = classType.getMethod("testPublicMethod");
            // b. 获取所有的方法（公共 / 继承）
            Method[] methods = classType.getMethods();
            // c. 获取指定的方法 （ 不包括继承）
            Method testPublicMethod1 = classType.getDeclaredMethod("testPublicMethod");
            // d. 获取所有的方法（ 不包括继承）
            Method[] declaredMethods = classType.getDeclaredMethods();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMethodPlay() {
        Class<ReflectClass> classClass = ReflectClass.class;
        try {
            Method testPublicMethod = classClass.getMethod("testPublicMethod");
            // 获取方法名
            String constructorName = testPublicMethod.getName();
            System.out.println("获取方法名 =====  " + constructorName);
            // 获取方法的Class对象
            Class<?> declaringClass = testPublicMethod.getDeclaringClass();
            System.out.println("获取方法的Class对象  ====== " + declaringClass);
            // 返回整型数值，用不同的位开关描述访问修饰符的使用状况
            int modifiers = testPublicMethod.getModifiers();
            System.out.println("返回整型数值，用不同的位开关描述访问修饰符的使用状况 ===== " + Modifier.toString(modifiers));
            // 获取用于描述方法抛出的异常类型的Class对象数组
            Class<?>[] exceptionTypes = testPublicMethod.getExceptionTypes();
            System.out.println("获取用于描述方法抛出的异常类型的Class对象数组  ===== " + Arrays.toString(exceptionTypes));
            //  获取一个用于描述参数类型的Class对象数组
            Class<?>[] parameterTypes = testPublicMethod.getParameterTypes();
            System.out.println("获取一个用于描述参数类型的Class对象数组 ==== " + Arrays.toString(parameterTypes));
            // 调用方法
            ReflectClass reflectClass = classClass.newInstance();
            Object invoke = testPublicMethod.invoke(reflectClass);
            System.out.println("调用方法 === " + invoke);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用反射调用构造函数
     */
    public void testConstructorGet() {
        try {
            // 1. 获取ReflectClass类的Class对象
            Class<?> reflectClassClass = ReflectClass.class;
            // 2.1 通过Class对象获取Constructor类对象，从而调用无参构造方法
            // 注：构造函数的调用实际上是在newInstance()，而不是在getConstructor()中调用
            Object instance = reflectClassClass.getConstructor().newInstance();
            System.out.println(instance);
            // 2.2 通过Class对象获取Constructor类对象（传入参数类型），从而调用有参构造方法
            Object mObj2 = reflectClassClass.getConstructor(String.class).newInstance("Carson");
            System.out.println(mObj2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取并修改私有的属性
     */
    public void testGetPrivateField() {
        try {
            // 1. 获取ReflectClass类的Class对象
            Class reflectClassClass = ReflectClass.class;
            // 2. 通过Class对象创建ReflectClass类的对象
            Object mReflectClass = reflectClassClass.newInstance();
            // 3. 通过Class对象获取ReflectClass类的testPrivateField属性
            Field f = reflectClassClass.getDeclaredField("testPrivateField");
            // 4. 设置私有访问权限
            f.setAccessible(true);
            // 5. 对新创建的ReflectClass对象设置name值
            f.set(mReflectClass, "Carson_Ho");
            // 6. 获取新创建ReflectClass对象的的name属性 & 输出
            System.out.println(f.get(mReflectClass));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 其它的方法
     */
    public void getOtherMethod() {
        try {
            Class<?> reflectClassClass = ReflectClass.class;
            Object instance = reflectClassClass.newInstance();
            // 获取泛型
            TypeVariable[] typeParameters = reflectClassClass.getTypeParameters();
            for (TypeVariable variable : typeParameters) {
                System.out.println("获取泛型  ===  " + variable.getName());
            }
            // 获取类实现的所有接口
            Type[] genericInterfaces = reflectClassClass.getGenericInterfaces();
            for (Type type : genericInterfaces) {
                System.out.println("接口类型  ==== " + type.toString());
            }
            Class[] interfaces = reflectClassClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                if (anInterface.isAssignableFrom(ReflectInterface.class)) {
                    Method method = anInterface.getDeclaredMethod("testInterface", String.class);
                    System.out.println(method.invoke(instance, "反射调用方法"));
                }
            }
            // 获取注解(只能获取到 RUNTIME 类型的注解)
            Method testInterface = reflectClassClass.getMethod("testInterface", String.class);
            Annotation[] annotations = testInterface.getAnnotations();
            System.out.println("获取注解  === " + Arrays.toString(annotations));
            Annotation[] declaredAnnotations = testInterface.getDeclaredAnnotations();
            System.out.println("获取注解  === " + Arrays.toString(declaredAnnotations));
            // Class.isEnum()
            // Class.getEnumConstants()
            // java.lang.reflect.Field.isEnumConstant()
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        """.trimIndent()
    }
}
