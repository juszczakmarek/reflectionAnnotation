/**
 * Napisz program który dla osoby Jan Kowalski, wiek 30
 * wygeneruje następującą reprezentację: {"name":"Jan Kowalski", age:30}
 */
package mju.reflectionAnnotation.Ex01serializacja;

import java.io.Serializable;
import java.lang.reflect.Field;

public class Serializacja {
    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException, InstantiationException {
        Osoba o1 = new Osoba();
        Class clazz = o1.getClass();

        Field fieldName = clazz.getDeclaredField("name");
        fieldName.setAccessible(true);
        fieldName.set(o1, "Jan Kowalski");
        fieldName = clazz.getDeclaredField("age");
        fieldName.setAccessible(true);
        fieldName.set(o1, 20);

        System.out.println(o1.toString());
        String message = toJson(o1);
        System.out.println(message);

        String className = o1.getClass().getName();
        Osoba o2 = fromJson(message, className);
        System.out.println(o2.toString());

//        System.out.println(o.getClass().getName());
    }

    //Class.fromName -stworzyc instancje klasy osoba z uzyciem Class.forName
    private static Osoba fromJson(String json, String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Class clazz = Class.forName("mju.reflectionAnnotation.Ex01serializacja.Osoba");
        Osoba osoba = (Osoba) clazz.newInstance();

        String[] fieldsAsString = json.split(",");
        fieldsAsString[0]=fieldsAsString[0].replace("{","");
        fieldsAsString[fieldsAsString.length-1]=fieldsAsString[fieldsAsString.length-1].replace("}","");

        for (int i=0;i<fieldsAsString.length;i++) {
            String[] localJsonLine = fieldsAsString[i].split(":");
            String fieldName = localJsonLine[0].replace("\"", "").trim();
            String fieldValue = localJsonLine[1].replace("\"", "").trim();

            Field field = osoba.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            if (i==0) {
                field.set(osoba,fieldValue);
            } else {
                field.set(osoba,Integer.parseInt(String.valueOf(fieldValue)));
            }
        }

//        for (String incomingJsonLine : fieldsAsString) {
//            String[] localJsonLine = incomingJsonLine.split(":");
//            String fieldName = localJsonLine[0].replace("\"", "");
//            String fieldValue = localJsonLine[1].replace("\"", "").trim();
//
//            Field field = osoba.getClass().getDeclaredField(fieldName);
//            field.setAccessible(true);
//            field.set(osoba,fieldValue);
//        }

        return osoba;
    }


    //zamiana dowolneoo obiektu na JSON
    private static String toJson(Object o) throws IllegalAccessException {
        Class clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{");
        for (Field field : fields) {
            field.setAccessible(true);
            stringBuilder.append("\"");
            stringBuilder.append(field.getName());
            stringBuilder.append("\":\"");
            stringBuilder.append(field.get(o));
            stringBuilder.append("\",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.append("\"}");

        return stringBuilder.toString();
    }
}

class Osoba implements Serializable{
    private String name;
    private int age;

    public String toString(){
        return "Osoba: " + name + " " + age;
    }
}