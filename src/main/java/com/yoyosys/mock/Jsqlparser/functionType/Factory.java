package com.yoyosys.mock.Jsqlparser.functionType;

/**
 * @Author: yjj
 * Date: 2021/10/29
 */
public abstract class Factory {
    public static Factory getFactory(String classname) {
        Factory factory = null;
        try {
            factory = (Factory) Class.forName(classname).newInstance();
         } catch (ClassNotFoundException e) {
            System.out.println("没有找到"+ classname+"类");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return factory;
    }
}
