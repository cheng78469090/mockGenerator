package com.yoyosys.mock.Jsqlparser.dataType;
import com.apifan.common.random.source.DateTimeSource;
import com.yoyosys.mock.Jsqlparser.util.IsDateFormat;
import com.yoyosys.mock.Jsqlparser.util.JudgeString;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @Author: yjj
 * Date: 2021/10/27
 */
public class CompareData implements Data{

    private String greaterThan;

    private String greaterThanEquals;

    private String MirrorThan;

    private String MirrorThanEquals;

    public String getGreaterThan() {
        return greaterThan;
    }

    public void setGreaterThan(String greaterThan) {
        this.greaterThan = greaterThan;
    }

    public String getGreaterThanEquals() {
        return greaterThanEquals;
    }

    public void setGreaterThanEquals(String greaterThanEquals) {
        this.greaterThanEquals = greaterThanEquals;
    }

    public String getMirrorThan() {
        return MirrorThan;
    }

    public void setMirrorThan(String mirrorThan) {
        MirrorThan = mirrorThan;
    }

    public String getMirrorThanEquals() {
        return MirrorThanEquals;
    }

    public void setMirrorThanEquals(String mirrorThanEquals) {
        MirrorThanEquals = mirrorThanEquals;
    }

    /**
     * data类，构建完成后调用方法返回一个值
     *
     * @return
     */
    @Override
    // 还需要完善区间
    public String inputValue() {
//       if(JudgeString.isDouble(greaterThan)||JudgeString.isDouble(greaterThanEquals)||JudgeString.isDouble(MirrorThan)||JudgeString.isDouble(MirrorThanEquals)){
        if(StringUtils.isNotBlank(MirrorThanEquals) || StringUtils.isNotBlank(MirrorThan)){
            String endS = null;
            Float end = null;
            Integer endNum = null;
            if(StringUtils.isNotBlank(MirrorThanEquals)){
                endS = MirrorThanEquals;
            }else {
                endS = MirrorThan;
            }
            if(StringUtils.isNotBlank(greaterThanEquals) || StringUtils.isNotBlank(greaterThan)){
//                   小于/=某值 且 大于某值
                if(StringUtils.isNotBlank(greaterThan)){
                    String beginS = greaterThan;
                    if(JudgeString.isDouble(beginS)  || JudgeString.isDouble(endS)){
                        Float begin = Float.valueOf(beginS);
                        end = Float.valueOf(endS);
                        return ((float)(Math.random() * (end - begin) + begin))+"";
                    }else if (IsDateFormat.isRqFormat(beginS)||IsDateFormat.isRqFormat(endS)){
                        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                        ZoneId zoneId = ZoneId.systemDefault();
                        LocalDate st = null;
                        LocalDate ed = null;
                        try {
                            st = yyyyMMdd.parse(beginS).toInstant().atZone(zoneId).toLocalDate();
                            ed = yyyyMMdd.parse(endS).toInstant().atZone(zoneId).toLocalDate();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return DateTimeSource.getInstance().randomDate(st,ed,"yyyyMMdd");
                    }else {
                        Integer begin = Integer.valueOf(beginS);
                        endNum = Integer.valueOf(endS);
                        int rz =(int)(Math.random() * (endNum - begin) + begin);
                        while (rz == begin){
                            rz = ((int)(Math.random() * (endNum - begin) + begin));
                        }
                        return rz+"";
                    }

                }else {
//                  小于/=某值且大于等于某值
                    String beginS = greaterThanEquals;
                    if(JudgeString.isDouble(beginS)  || JudgeString.isDouble(endS)){
                        Float begin = Float.valueOf(beginS);
                        end = Float.valueOf(endS);
                        return ((float)(Math.random() * (end - begin) + begin))+"";
                    }else if(IsDateFormat.isRqFormat(beginS)||IsDateFormat.isRqFormat(endS)){
                        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                        ZoneId zoneId = ZoneId.systemDefault();
                        LocalDate st = null;
                        LocalDate ed = null;
                        String yyyyMMdd1 = null;
                        try {
                            st = yyyyMMdd.parse(beginS).toInstant().atZone(zoneId).toLocalDate().minusDays(1);
                            ed = yyyyMMdd.parse(endS).toInstant().atZone(zoneId).toLocalDate();
                            if (st.isAfter(ed)){
                                yyyyMMdd1 = DateTimeSource.getInstance().randomDate(ed, st, "yyyyMMdd");
                            } else {
                                yyyyMMdd1 = DateTimeSource.getInstance().randomDate(st, ed, "yyyyMMdd");
                            }
                        } catch (Exception e) {
                            System.out.println(st+"_____"+ed);
                            e.printStackTrace();
                        }
                        return yyyyMMdd1;
                    }else {
                        Integer begin = Integer.valueOf(beginS);
                        endNum = Integer.valueOf(endS);
                        return ((int)(Math.random() * (endNum - begin) + begin))+"";
                    }
                }
            }else {
//                   小于某值或小于等于(待考究)
                if(JudgeString.isDouble(endS)){
                    end = Float.valueOf(endS);
                    return ((float)Math.random() * end)+"";
                }else if(IsDateFormat.isRqFormat(endS)){
                    SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                    ZoneId zoneId = ZoneId.systemDefault();
                    LocalDate ed = null;
                    try {
                        ed = yyyyMMdd.parse(endS).toInstant().atZone(zoneId).toLocalDate();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return DateTimeSource.getInstance().randomPastDate(ed,"yyyyMMdd");
                }
                else {
                    endNum = Integer.valueOf(endS);
                    return ((int)(Math.random() * endNum))+"";
                }
            }
        }else {
            if(StringUtils.isNotBlank(greaterThan) || StringUtils.isNotBlank(greaterThanEquals)){
                String beginS = null;
                if(StringUtils.isNotBlank(greaterThan)){
//                       大于 某值
                    beginS = greaterThan;
                    if(JudgeString.isDouble(beginS)){
                        Float begin = Float.valueOf(beginS);
                        Float end = 10*begin;
                        return ((float)(Math.random() * (end - begin) + begin))+"";
                    }else if(IsDateFormat.isRqFormat(beginS)){
                        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                        ZoneId zoneId = ZoneId.systemDefault();
                        LocalDate st = null;
                        try {
                            st = yyyyMMdd.parse(beginS).toInstant().atZone(zoneId).toLocalDate();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return DateTimeSource.getInstance().randomFutureDate(st,"yyyyMMdd");
                    }else {
                        Integer begin = Integer.valueOf(beginS);
                        Integer endNum = 10*begin;
                        int rz =(int)(Math.random() * (endNum - begin) + begin);
                        while (rz == begin){
                            rz = ((int)(Math.random() * (endNum - begin) + begin));
                        }
                        return rz+"";
                    }

                }else {
//                       大于等于某值
                    beginS = greaterThanEquals;
                    if(JudgeString.isDouble(beginS)){
                        Float begin = Float.valueOf(beginS);
                        Float end = 10*begin;
                        return ((float)(Math.random() * (end - begin) + begin))+"";
                    }else if (IsDateFormat.isRqFormat(beginS)){
                        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                        ZoneId zoneId = ZoneId.systemDefault();
                        LocalDate st = null;
                        try {
                            st = yyyyMMdd.parse(beginS).toInstant().atZone(zoneId).toLocalDate().minusDays(1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return DateTimeSource.getInstance().randomFutureDate(st,"yyyyMMdd");
                    }
                    else {
                        Integer begin = Integer.valueOf(beginS);
                        Integer endNum = 10*begin;
                        return ((int)(Math.random() * (endNum - begin) + begin+1))+"";
                    }
                }
            }else {
//                   全都无值
                return "";
            }
        }
    }

    /**
     * 返回反例
     *
     * @return
     */
    @Override
    public String inputCounterexample() {

        String tempNotEqual = greaterThan;
        greaterThan = MirrorThan;
        MirrorThan =tempNotEqual;
        String  tempNotEquals= greaterThanEquals;
        greaterThanEquals = MirrorThanEquals;
        MirrorThanEquals = tempNotEquals;
        if(StringUtils.isNotBlank(MirrorThanEquals) || StringUtils.isNotBlank(MirrorThan)){
            String endS = null;
            Float end = null;
            Integer endNum = null;
            if(StringUtils.isNotBlank(MirrorThanEquals)){
                endS = MirrorThanEquals;
            }else {
                endS = MirrorThan;
            }
            if(StringUtils.isNotBlank(greaterThanEquals) || StringUtils.isNotBlank(greaterThan)){
//                   小于/=某值 且 大于某值
                String beginS = greaterThan;
                if(StringUtils.isNotBlank(greaterThan)){
                    beginS = greaterThan;
                }else {
//                       小于/=某值且大于等于某值
                    beginS = greaterThanEquals;
                }
//                当处于中间的时候随机给一个小于或者给一个大于
                if (((int)(Math.random()*2))%2==0){
//                反例大于
                    if(JudgeString.isDouble(endS)||JudgeString.isDouble(beginS)){
                        Float begin = Float.valueOf(beginS);
                        end = 10*begin;
                        return ((float)(Math.random() * (end - begin) + begin))+"";
                    }else if(IsDateFormat.isRqFormat(beginS)){
                        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                        ZoneId zoneId = ZoneId.systemDefault();
                        LocalDate st = null;
                        try {
                            st = yyyyMMdd.parse(beginS).toInstant().atZone(zoneId).toLocalDate();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return DateTimeSource.getInstance().randomFutureDate(st,"yyyyMMdd");
                    }else {
                        Integer begin = Integer.valueOf(beginS);
                        endNum = 10*begin;
                        int rz =(int)(Math.random() * (endNum - begin) + begin);
                        while (rz == begin){
                            rz = ((int)(Math.random() * (endNum - begin) + begin));
                        }
                        return rz+"";
                    }

                }else {
//                    反例小于
                    if(JudgeString.isDouble(endS)||JudgeString.isDouble(beginS)){
                        end = Float.valueOf(endS);
                        return ((float)(Math.random() * end))+"";
                    }else if(IsDateFormat.isRqFormat(endS)){
                        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                        ZoneId zoneId = ZoneId.systemDefault();
                        LocalDate ed = null;
                        try {
                            ed = yyyyMMdd.parse(endS).toInstant().atZone(zoneId).toLocalDate();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return DateTimeSource.getInstance().randomPastDate(ed,"yyyyMMdd");
                    }
                    else {
                        endNum = Integer.valueOf(endS);
                        return ((int)(Math.random() * endNum))+"";
                    }
                }
            }else {
//                   小于某值或小于等于(待考究)
                if(JudgeString.isDouble(endS)){
                    end = Float.valueOf(endS);
                    return ((float)(Math.random() * end))+"";
                }else if(IsDateFormat.isRqFormat(endS)){
                    SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                    ZoneId zoneId = ZoneId.systemDefault();
                    LocalDate ed = null;
                    try {
                        ed = yyyyMMdd.parse(endS).toInstant().atZone(zoneId).toLocalDate();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return DateTimeSource.getInstance().randomPastDate(ed,"yyyyMMdd");
                }
                else {
                    endNum = Integer.valueOf(endS);
                    return ((int)(Math.random() * endNum))+"";
                }
            }
        }else {
            if(StringUtils.isNotBlank(greaterThan) || StringUtils.isNotBlank(greaterThanEquals)){
                String beginS = null;
                if(StringUtils.isNotBlank(greaterThan)){
//                       大于 某值
                    beginS = greaterThan;
                    if(JudgeString.isDouble(beginS)){
                        Float begin = Float.valueOf(beginS);
                        Float end = 10*begin;
                        return ((float)(Math.random() * (end - begin) + begin))+"";
                    }else if(IsDateFormat.isRqFormat(beginS)){
                        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                        ZoneId zoneId = ZoneId.systemDefault();
                        LocalDate st = null;
                        try {
                            st = yyyyMMdd.parse(beginS).toInstant().atZone(zoneId).toLocalDate();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return DateTimeSource.getInstance().randomFutureDate(st,"yyyyMMdd");
                    }else {
                        Integer begin = Integer.valueOf(beginS);
                        Integer endNum = 10*begin;
                        int rz =(int)(Math.random() * (endNum - begin) + begin);
                        while (rz == begin){
                            rz = ((int)(Math.random() * (endNum - begin) + begin));
                        }
                        return rz+"";
                    }

                }else {
//                       大于等于某值
                    beginS = greaterThanEquals;
                    if(JudgeString.isDouble(beginS)){
                        Float begin = Float.valueOf(beginS);
                        Float end = 10*begin;
                        return ((float)(Math.random() * (end - begin) + begin))+"";
                    }else if (IsDateFormat.isRqFormat(beginS)){
                        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                        ZoneId zoneId = ZoneId.systemDefault();
                        LocalDate st = null;
                        try {
                            st = yyyyMMdd.parse(beginS).toInstant().atZone(zoneId).toLocalDate().minusDays(1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return DateTimeSource.getInstance().randomFutureDate(st,"yyyyMMdd");
                    }
                    else {
                        Integer begin = Integer.valueOf(beginS);
                        Integer endNum = 10*begin;
                        return ((int)(Math.random() * (endNum - begin) + begin+1))+"";
                    }
                }
            }else {
//                   全都无值
                return "";
            }
        }
    }

}
