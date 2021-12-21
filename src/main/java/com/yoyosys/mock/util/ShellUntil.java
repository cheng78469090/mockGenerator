package com.yoyosys.mock.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShellUntil {

        private static final Logger logger = LoggerFactory.getLogger(ShellUntil.class);

        public static String execShell(String cmd) {
            if(cmd==null || "".equals(cmd)){
                logger.error("cmd为空");
                return null;
            }
            try {
                Process process = null;
                BufferedReader reader = null;
                // Linux下
                String[] nowcmd = new String[]{"/bin/sh","-c", cmd};
                process = Runtime.getRuntime().exec(nowcmd);
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line).append("\n");
                }
                //拼接
                reader.close();
                process.destroy();
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("执行execShell失败："+cmd+">>>>>>>>>>>>>>>>>>"+e);
            }
            return null;
        }

}
