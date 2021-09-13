package com.yoyosys.mock.pojo;

/**
 * @Author: yjj
 * Date: 2021/9/3
 */
public class DsConfig {
    private String loadScene;

    private String FILE_ENCODING;

    public String getFILE_ENCODING() {
        return FILE_ENCODING;
    }

    public void setFILE_ENCODING(String FILE_ENCODING) {
        this.FILE_ENCODING = FILE_ENCODING;
    }

    public String getLoadScene() {
        return loadScene;
    }

    public void setLoadScene(String loadScene) {
        this.loadScene = loadScene;
    }
}
