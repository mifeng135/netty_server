package core.proto;

import core.util.FileUtil;

import java.io.File;
import java.util.*;

public class ParseProtoFile {

    private static final String begin = "m{";
    private static final String end = "}";

    public static final String mapCodeBegin = "<";
    public static final String mapCodeEnd = ">";

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


    private static final String writePath = "game_proto_bean/src/protocal";
    private static final String readPath = "/proto_file";

    public static void init() {
        File importDir = new File(System.getProperty("user.dir"), writePath);
        for (File file : importDir.listFiles()) {
            if (file.isDirectory()) {

            }
            parse(file.getName());
        }
    }


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
                parseProperty(classFieldList, str);
                continue;
            }
            if (isEnd) {
                mapClass.put(className, classFieldList);
                className = "";
            }
        }

        Set<String> importClass = parseImportClass(mapClass);
    }


    public static Set<String> parseImportClass(Map<String, List<ProtoProperty>> mapClass) {
        Set<String> hasClassList = mapClass.keySet();
        Map<String, Integer> importClassMap = new HashMap<>();
        for (Map.Entry<String, List<ProtoProperty>> entry : mapClass.entrySet()) {
            List<ProtoProperty> protoPropertyList = entry.getValue();
            for (ProtoProperty property : protoPropertyList) {
                String className = property.getClassName();
                if (className != null && className.length() > 0) {
                    if (!hasClassList.contains(className)) {
                        importClassMap.putIfAbsent(className, 1);
                    }
                }
            }
        }
        return importClassMap.keySet();
    }

    public static String parseClassName(String str) {
        String[] list = str.split(" ");
        List<String> arrayList = new ArrayList<>();
        for (String value : list) {
            value = value.trim();
            if (value.length() > 0) {
                arrayList.add(value);
            }
        }
        if (arrayList.size() > 0) {
            return arrayList.get(1);
        }
        return "";
    }


    public static void parseProperty(List<ProtoProperty> protoProperties, String str) {
        String property = str.split("=")[0];

        if (property.indexOf(mapCodeBegin) > 0) {
            int beingIndex = property.indexOf(mapCodeBegin);
            int endIndex = property.indexOf(mapCodeEnd);
            String subStr = property.substring(beingIndex + 1, endIndex);
            String name = property.substring(endIndex + 1, property.length() - 1).trim();
            String[] subList = subStr.split(",");
            String mapKey = subList[0].trim();
            String mapClass = subList[1].trim();
            ProtoProperty protoProperty = new ProtoProperty();
            protoProperty.setType(TYPE_MAP);
            protoProperty.setMapKey(mapKey);
            protoProperty.setClassName(mapClass);
            protoProperty.setName(name);
            protoProperties.add(protoProperty);
        } else {
            String[] list = property.split(" ");
            List<String> arrayList = new ArrayList<>();
            for (String value : list) {
                value = value.trim();
                if (value.length() > 0) {
                    arrayList.add(value);
                }
            }
            ProtoProperty protoProperty = new ProtoProperty();
            if (arrayList.size() == 2) {
                protoProperty.setType(arrayList.get(0));
                protoProperty.setName(arrayList.get(1));
                protoProperties.add(protoProperty);
            }
            if (arrayList.size() == 3) {
                protoProperty.setType(arrayList.get(0));
                protoProperty.setClassName(arrayList.get(1));
                protoProperty.setName(arrayList.get(2));
                protoProperties.add(protoProperty);
            }
        }

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
