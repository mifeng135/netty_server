package core.proto;

import core.util.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseProtoFile {


    private static final String begin = "m{";
    private static final String end = "}";


    private static final String TYPE_BOOL = "bool";
    private static final String TYPE_INT32 = "int32";
    private static final String TYPE_INT64 = "int64";
    private static final String TYPE_DOUBLE = "double";
    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_UNIT32 = "unit32";
    private static final String TYPE_UNIT64 = "unit64";
    private static final String TYPE_SINT32 = "sint32";
    private static final String TYPE_SINT64 = "sint64";
    private static final String TYPE_STRING = "string";
    private static final String TYPE_REPEATED = "repeated";
    private static final String TYPE_MAP = "map";


    public static void parse(String fileName) {
        List<String> fileList = FileUtil.protoFileList(fileName, "proto_file");
        Map<String, List<ProtoProperty>> mapClass = new HashMap<>();
        List<ProtoProperty> classFieldList = null;
        String className = "";
        boolean isBegin = false;
        boolean isEnd = false;
        for (String str : fileList) {
            if (!isBegin) {
                isBegin = checkBegin(str);
                if (isBegin) {
                    className = parseClassName(str);
                    classFieldList = new ArrayList<>();
                    continue;
                }
            }
            if (isBegin) {
                isEnd = checkEnd(str);
                if (isEnd) {
                    isBegin = false;
                }
            }
            if (isBegin) {
                classFieldList.add(parseProperty(str));
                continue;
            }
            if (isEnd) {
                mapClass.put(className, classFieldList);
                className = "";
            }
        }
        int dd = 0;
    }

    public static String parseClassName(String str) {
        String[] list = str.split(" ");
        List<String> arrayList = new ArrayList<>();
        for (String value : list) {
            if (value.length() > 0) {
                arrayList.add(value);
            }
        }
        if (arrayList.size() > 0) {
            return arrayList.get(1);
        }
        return "";
    }


    public static ProtoProperty parseProperty(String str) {
        String property = str.split("=")[0];
        String[] list = property.split(" ");
        List<String> arrayList = new ArrayList<>();
        for (String value : list) {
            if (value.length() > 0) {
                arrayList.add(value);
            }
        }

        ProtoProperty protoProperty = new ProtoProperty();
        if (arrayList.size() == 2) {
            protoProperty.setType(arrayList.get(0));
            protoProperty.setName(arrayList.get(1));
        }
        if (arrayList.size() == 3) {
            protoProperty.setType(arrayList.get(0));
            protoProperty.setClassName(arrayList.get(1));
            protoProperty.setName(arrayList.get(2));
        }
        return protoProperty;
    }

    public static boolean checkBegin(String str) {
        if (str.length() <= 0) {
            return false;
        }
        String first = str.charAt(0) + "";
        String last = str.charAt(str.length() - 1) + "";

        if (begin.equals(first + last)) {
            return true;
        }
        return false;
    }

    public static boolean checkEnd(String str) {
        if (str.length() <= 0) {
            return false;
        }
        String last = str.charAt(str.length() - 1) + "";
        if (last.equals(end)) {
            return true;
        }
        return false;
    }
}
