package org.apache.myfaces.blank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author tongzhenke
 * @Date 2020/12/23 18:19
 */
public enum TestEnum {

    HANDLE_NO("已推送", 0),
    HANDLEING("处置中", 1),
    HANDLE_FINISH("已处置", 2),
    ;

    private String desc;
    private Integer code;

    TestEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public static TestEnum getByCode(Integer code) {
        for (TestEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return TestEnum.HANDLEING;
    }

    /**
     * 请求返回的Enum集合数据
     *
     * @return
     */
    public static List<Map<String, Object>> getEnumList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (TestEnum testEnum : TestEnum.values()) {
            Map item = new HashMap<String, Object>();
            item.put("code", testEnum.code);
            item.put("desc", testEnum.desc);
            list.add(item);
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(TestEnum.getEnumList());
    }

}
