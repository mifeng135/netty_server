package core.annotation.proto;



import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class ParseProtoFile {

    private static final String writePath = "proto_file/";
    private static final String fileSuffix = ".proto";
    private static final String protoBegin = "syntax = proto3;";


    public static void createProtoFile() {
        File rootDir = new File(System.getProperty("user.dir"), writePath);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }

        Map<String, List<ProtoInfo>> stringListMap = ProtoA.getInstance().getClassMap();
        for (Map.Entry<String, List<ProtoInfo>> entry : stringListMap.entrySet()) {
            List<ProtoInfo> protoInfoList = entry.getValue();
            protoInfoList.sort(new Comparator<ProtoInfo>() {
                @Override
                public int compare(ProtoInfo o1, ProtoInfo o2) {
                    String s1 = o1.getCls().getPackage().getName();
                    String s2 = o2.getCls().getPackage().getName();
                    if (s1.length() > s2.length()) {
                        return 1;
                    }
                    return -1;
                }
            });
        }
        for (Map.Entry<String, List<ProtoInfo>> entry : stringListMap.entrySet()) {
            List<ProtoInfo> protoInfoList = entry.getValue();
            File protoFile = new File(rootDir, entry.getKey() + fileSuffix);
            if (protoFile.exists()) {
                protoFile.delete();
            }
            try {
                BufferedWriter builder = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(protoFile), "UTF-8"));
                builder.append(protoBegin);
                builder.newLine();

                List<String> importList = parseImportString(protoInfoList);
                for (String str : importList) {
                    builder.append(str);
                }
                builder.newLine();

                for (ProtoInfo info : protoInfoList) {
                    Class cls = info.getCls();
                    String name = cls.getSimpleName();

                    StringBuilder msgBuilder = new StringBuilder();
                    msgBuilder.append("message " + name + " {" + "\n");
                    Field[] fields = cls.getDeclaredFields();
                    msgBuilder.append(initStr(fields));
                    msgBuilder.append("}\n");
                    builder.append(msgBuilder.toString());
                    builder.newLine();
                }
                builder.flush();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String initStr(Field[] fields) {
        StringBuilder builder = new StringBuilder();
        int index = 1;
        for (Field field : fields) {
            String fileName = field.getName();
            Class<?> curFieldType = field.getType();
            if (curFieldType.equals(List.class)) {
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                Class<?> actualTypeArgument = (Class<?>) genericType.getActualTypeArguments()[0];
                String simpleName = actualTypeArgument.getSimpleName();
                builder.append("\t" + getProtoKey("List") + " "  + simpleName + " " + fileName + "" + " = " + index + ";" + "\n");
            } else if (curFieldType.equals(Map.class)) {

            } else {
                builder.append("\t" + getProtoKey(field.getType().getSimpleName()) + " " + fileName + "" + " = " + index + ";" + "\n");
            }
            index++;
        }
        return builder.toString();
    }



    public static String getProtoKey(String key) {
        String returnValue = key;
        switch (key) {
            case "int":
                returnValue = "int32";
                break;
            case "float":
                returnValue = "float";
                break;
            case "double":
                returnValue = "double";
                break;
            case "boolean":
                returnValue = "bool";
                break;
            case "String":
                returnValue = "string";
                break;
            case "List":
                returnValue = "repeated";
                break;
            case "Map":
                returnValue = "map";
                break;
        }
        return returnValue;
    }
    public static List<String> parseImportString(List<ProtoInfo> data) {
        List<String> importList = new ArrayList<>();
        for (ProtoInfo info : data) {
            String[] strings = info.getImportFile();
            if (strings != null) {
                for (int i = 0; i < strings.length; i++) {
                    String importStr = "import " + "\"" + strings[i] + fileSuffix + "\"";
                    if (!importList.contains(importStr)) {
                        importList.add(importStr);
                    }
                }
            }
        }
        return importList;
    }
}
